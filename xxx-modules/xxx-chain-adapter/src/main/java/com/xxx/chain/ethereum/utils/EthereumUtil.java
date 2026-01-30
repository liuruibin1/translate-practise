package com.xxx.chain.ethereum.utils;

import com.xxx.chain.adapter.bo.ChainBlockBO;
import com.xxx.chain.adapter.bo.CryptoAccountBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.adapter.bo.TransferTransactionBO;
import com.xxx.chain.constant.PrivateKey;
import com.xxx.chain.enumerate.ChainTransactionStatusEnum;
import com.xxx.chain.ethereum.abi.ERC20;
import com.xxx.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.web3j.abi.DefaultFunctionReturnDecoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static com.xxx.chain.ethereum.abi.ERC20.FUNC_TRANSFER;

/**
 * 以太坊工具类
 * 提供以太坊区块链相关的工具方法
 *
 * @author system
 * @since 1.0.0
 */
@Slf4j
public final class EthereumUtil {

    // 常量定义
    private static final String ETHEREUM_ADDRESS_PATTERN = "^0x[0-9a-fA-F]{40}$";
    private static final String ERC20_TRANSFER_EVENT_SIGNATURE = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";

    // 重试配置
    private static final int MAX_RETRY_COUNT = 10;
    private static final long BATCH_DELAY_MS = 1000;

    // HTTP客户端配置
    private static final int CONNECT_TIMEOUT_SECONDS = 30;
    private static final int READ_TIMEOUT_SECONDS = 60;
    private static final int WRITE_TIMEOUT_SECONDS = 60;

    // 线程池配置
    private static final int THREAD_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 20;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final int QUEUE_CAPACITY = 1000;

    // 私有构造函数，防止实例化
    private EthereumUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== 地址相关方法 ====================

    /**
     * 使用 web3j 尝试创建 Credentials，若抛异常则视为非法私钥。
     * privateKeyHex 可带 0x 前缀或不带。
     */
    public static boolean isValidPrivateKeyWithWeb3j(String privateKeyHex) {
        if (privateKeyHex == null) return false;
        try {
            // Credentials.create 会在内部解析并构建公钥/地址
            Credentials creds = Credentials.create(privateKeyHex);
            // 可进一步检查地址合法性（非空）
            String address = creds.getAddress();
            return address != null && !address.isEmpty();
        } catch (Exception e) {
            // 任何异常都认为私钥非法/不可用
            return false;
        }
    }

    /**
     * 验证以太坊地址格式
     *
     * @param address 地址字符串
     * @return 是否为有效的以太坊地址
     */
    public static boolean isValidAddress(String address) {
        return StringUtils.hasText(address) && Pattern.matches(ETHEREUM_ADDRESS_PATTERN, address);
    }

    /**
     * 转换为校验和地址
     *
     * @param address 原始地址
     * @return 校验和地址
     */
    public static String toChecksumAddress(String address) {
        try {
            return Keys.toChecksumAddress(address);
        } catch (Exception e) {
            log.warn("Failed to convert address to checksum: {}", address, e);
            return address;
        }
    }

    // ==================== 账户相关方法 ====================

    /**
     * 生成私钥和地址
     *
     * @return 账户信息
     * @throws InvalidAlgorithmParameterException 算法参数异常
     * @throws NoSuchAlgorithmException           算法不存在异常
     * @throws NoSuchProviderException            提供者不存在异常
     */
    public static CryptoAccountBO generatePrivateKeyAndAddress()
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        String privateKey = ecKeyPair.getPrivateKey().toString(16);
        String address = Keys.toChecksumAddress(Keys.getAddress(ecKeyPair.getPublicKey()));

        if (StringUtils.hasText(privateKey) && StringUtils.hasText(address)) {
            CryptoAccountBO walletInfo = new CryptoAccountBO();
            walletInfo.setPrivateKey(privateKey);
            walletInfo.setAddress(address);
            return walletInfo;
        }

        throw new IllegalStateException("Failed to generate valid private key and address");
    }

    /**
     * 根据私钥获取地址
     *
     * @param privateKey 私钥
     * @return 地址
     * @throws IllegalArgumentException 私钥无效时抛出
     */
    public static String getAddressByPrivateKey(String privateKey) {
        try {
            Credentials credentials = Credentials.create(privateKey);
            return Keys.toChecksumAddress(credentials.getAddress());
        } catch (Exception e) {
            log.error("Failed to get address from private key", e);
            throw new IllegalArgumentException("Invalid private key", e);
        }
    }

    // ==================== Web3j客户端管理 ====================

    /**
     * 根据RPC配置创建Web3j客户端
     *
     * @param enableProxy 是否启用代理
     * @param proxyHost   代理主机
     * @param proxyPort   代理端口
     * @param rpcUrl      RPC地址
     * @return Web3j客户端
     */
    public static Web3j createWeb3jClient(boolean enableProxy, String proxyHost, int proxyPort, String rpcUrl) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if (enableProxy && StringUtils.hasText(proxyHost)) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            httpClientBuilder.proxy(proxy);
        }

        return Web3j.build(new HttpService(rpcUrl, httpClientBuilder.build()));
    }

    // ==================== 余额查询方法 ====================

    /**
     * 查询ETH余额
     *
     * @param web3j          Web3j客户端
     * @param accountAddress 账户地址
     * @return 余额字符串
     * @throws RuntimeException 查询失败时抛出
     */
    public static String getEthBalance(Web3j web3j, String accountAddress) {
        try {
            Request<?, EthGetBalance> request = web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST);
            EthGetBalance response = request.send();
            BigInteger balance = response.getBalance();
            return balance.toString();
        } catch (IOException e) {
            log.error("Failed to get ETH balance for address: {}", accountAddress, e);
            throw new RuntimeException("Failed to get ETH balance", e);
        }
    }

    /**
     * 查询ERC20代币余额
     *
     * @param web3j          Web3j客户端
     * @param accountAddress 账户地址
     * @param erc20Address   代币合约地址
     * @return 余额字符串
     * @throws RuntimeException 查询失败时抛出
     */
    public static String getErc20Balance(Web3j web3j, String accountAddress, String erc20Address) {
        try {
            Credentials emptyCredentials = Credentials.create(PrivateKey.EMPTY);
            ERC20 erc20 = ERC20.load(erc20Address, web3j, emptyCredentials, new DefaultGasProvider());
            BigInteger balance = erc20.balanceOf(accountAddress).send();
            return balance.toString();
        } catch (Exception e) {
            log.error("Failed to get ERC20 balance for address: {} and contract: {}", accountAddress, erc20Address, e);
            throw new RuntimeException("Failed to get ERC20 balance", e);
        }
    }

    // ==================== 区块相关方法 ====================

    /**
     * 获取最新区块信息
     *
     * @param rpcConfig RPC配置
     * @return 区块信息
     * @throws Exception 获取失败时抛出异常
     */
    public static ChainBlockBO getLatestBlock(RpcConfigBO rpcConfig) throws Exception {
        Web3j web3j = createWeb3jClient(
                rpcConfig.isEnableProxy(),
                rpcConfig.getProxyHost(),
                rpcConfig.getProxyPort(),
                rpcConfig.getRpcUrl()
        );

        try {
            Request<?, EthBlock> request = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false);
            EthBlock.Block block = request.send().getBlock();

            ChainBlockBO blockInfo = new ChainBlockBO();
            blockInfo.setBlockNumber(block.getNumber().longValue());
            blockInfo.setBlockHash(block.getHash());
            blockInfo.setBlockTimestamp(block.getTimestamp().longValue());

            return blockInfo;
        } finally {
            web3j.shutdown();
        }
    }

    // ==================== 交易状态查询 ====================

    /**
     * 查询交易状态
     *
     * @param rpcConfig RPC配置
     * @param txHash    交易哈希
     * @return 交易状态
     * @throws IOException 网络异常
     */
    public static ChainTransactionStatusEnum getTransactionStatus(RpcConfigBO rpcConfig, String txHash) throws IOException {
        Web3j web3j = createWeb3jClient(
                rpcConfig.isEnableProxy(),
                rpcConfig.getProxyHost(),
                rpcConfig.getProxyPort(),
                rpcConfig.getRpcUrl()
        );

        try {
            Request<?, EthGetTransactionReceipt> request = web3j.ethGetTransactionReceipt(txHash);
            EthGetTransactionReceipt response = request.send();
            Optional<TransactionReceipt> receipt = response.getTransactionReceipt();

            if (receipt.isPresent()) {
                TransactionReceipt txReceipt = receipt.get();
                if ("0x1".equals(txReceipt.getStatus()) && txReceipt.isStatusOK()) {
                    return ChainTransactionStatusEnum.SUCCESSFUL;
                } else {
                    return ChainTransactionStatusEnum.FAILED;
                }
            }

            return ChainTransactionStatusEnum.UNKNOWN;
        } finally {
            web3j.shutdown();
        }
    }

    // ==================== 交易抓取方法 ====================

    /**
     * 抓取ETH转账交易
     *
     * @param transferTransactionList 交易列表（输出参数）
     * @param rpcConfig               RPC配置
     * @param fromBlockNumber         起始区块号
     * @param toBlockNumber           结束区块号
     * @throws Exception 抓取失败时抛出异常
     */
    public static void fetchEthTransferTransactions(
            List<TransferTransactionBO> transferTransactionList,
            RpcConfigBO rpcConfig,
            long fromBlockNumber,
            long toBlockNumber) throws Exception {

        Web3j web3j = createWeb3jClient(
                rpcConfig.isEnableProxy(),
                rpcConfig.getProxyHost(),
                rpcConfig.getProxyPort(),
                rpcConfig.getRpcUrl()
        );

        try {
            List<Long> blockNumbers = LongStream.rangeClosed(fromBlockNumber, toBlockNumber)
                    .boxed()
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            Set<Long> failedBlocks = fetchEthTransactionsWithRetry(transferTransactionList, web3j, blockNumbers);

            if (!failedBlocks.isEmpty()) {
                throw new Exception(String.format("Failed to fetch %d blocks: %s", failedBlocks.size(), failedBlocks));
            }
        } finally {
            web3j.shutdown();
        }
    }

    /**
     * 抓取ERC20转账交易
     *
     * @param transferTransactionList 交易列表（输出参数）
     * @param rpcConfig               RPC配置
     * @param fromBlockNumber         起始区块号
     * @param toBlockNumber           结束区块号
     * @param contractAddresses       合约地址列表
     * @throws IOException 抓取失败时抛出异常
     */
    public static void fetchErc20TransferTransactions(
            List<TransferTransactionBO> transferTransactionList,
            RpcConfigBO rpcConfig,
            BigInteger fromBlockNumber,
            BigInteger toBlockNumber,
            List<String> contractAddresses) throws IOException {

        Web3j web3j = createWeb3jClient(
                rpcConfig.isEnableProxy(),
                rpcConfig.getProxyHost(),
                rpcConfig.getProxyPort(),
                rpcConfig.getRpcUrl()
        );

        try {
            DefaultBlockParameter fromBlock = DefaultBlockParameter.valueOf(fromBlockNumber);
            DefaultBlockParameter toBlock = DefaultBlockParameter.valueOf(toBlockNumber);

            EthFilter filter = new EthFilter(fromBlock, toBlock, contractAddresses);
            filter.addSingleTopic(ERC20_TRANSFER_EVENT_SIGNATURE);

            Request<?, EthLog> request = web3j.ethGetLogs(filter);
            List<EthLog.LogResult> logs = request.send().getLogs();

            for (EthLog.LogResult logResult : logs) {
                Object logObject = logResult.get();
                if (logObject instanceof EthLog.LogObject) {
                    processErc20TransferLog(transferTransactionList, (EthLog.LogObject) logObject);
                }
            }
        } finally {
            web3j.shutdown();
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 带重试的ETH交易抓取
     */
    private static Set<Long> fetchEthTransactionsWithRetry(
            List<TransferTransactionBO> transferTransactionList,
            Web3j web3j,
            List<Long> blockNumbers) throws InterruptedException {

        Set<Long> failedBlocks = ConcurrentHashMap.newKeySet();
        List<Long> retryBlocks = new ArrayList<>(blockNumbers);
        int retryCount = 0;

        while (!retryBlocks.isEmpty() && retryCount < MAX_RETRY_COUNT) {
            Set<Long> currentFailedBlocks = fetchEthTransactions(transferTransactionList, web3j, retryBlocks);
            retryBlocks = new ArrayList<>(currentFailedBlocks);

            if (!retryBlocks.isEmpty()) {
                Thread.sleep(BATCH_DELAY_MS);
                retryCount++;
            }
        }

        failedBlocks.addAll(retryBlocks);
        return failedBlocks;
    }

    /**
     * 抓取ETH交易
     */
    private static Set<Long> fetchEthTransactions(
            List<TransferTransactionBO> transferTransactionList,
            Web3j web3j,
            List<Long> blockNumbers) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(blockNumbers.size());
        Set<Long> failedBlocks = ConcurrentHashMap.newKeySet();
        ExecutorService executor = createThreadPool();

        try {
            for (long blockNumber : blockNumbers) {
                executor.submit(() -> {
                    try {
                        fetchBlockTransactions(transferTransactionList, web3j, blockNumber);
                    } catch (Exception e) {
                        log.error("Failed to fetch block: {}", blockNumber, e);
                        failedBlocks.add(blockNumber);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        } finally {
            executor.shutdown();
        }

        return failedBlocks;
    }

    /**
     * 抓取单个区块的交易
     */
    private static void fetchBlockTransactions(
            List<TransferTransactionBO> transferTransactionList,
            Web3j web3j,
            long blockNumber) {

        try {
            DefaultBlockParameter blockParam = new DefaultBlockParameterNumber(BigInteger.valueOf(blockNumber));
            Request<?, EthBlock> request = web3j.ethGetBlockByNumber(blockParam, true);
            EthBlock response = request.send();

            if (response != null && response.getBlock() != null) {
                processEthTransactions(transferTransactionList, response.getBlock());
            }
        } catch (Exception e) {
            log.error("Failed to fetch block transactions for block: {}", blockNumber, e);
            throw new RuntimeException("Failed to fetch block transactions", e);
        }
    }

    /**
     * 处理ETH交易
     */
    private static void processEthTransactions(List<TransferTransactionBO> transferTransactionList, EthBlock.Block block) {
        List<EthBlock.TransactionResult> transactions = block.getTransactions();

        for (EthBlock.TransactionResult transactionResult : transactions) {
            if (transactionResult instanceof EthBlock.TransactionObject) {
                EthBlock.TransactionObject transactionObject = (EthBlock.TransactionObject) transactionResult;
                Transaction transaction = transactionObject.get();

                TransferTransactionBO transferTx = createTransferTransactionBO(transaction, block);
                transferTransactionList.add(transferTx);
            }
        }
    }

    /**
     * 处理ERC20转账日志
     */
    private static void processErc20TransferLog(
            List<TransferTransactionBO> transferTransactionList,
            EthLog.LogObject logObject) {

        List<String> topics = logObject.getTopics();
        if (CollectionUtils.isNotEmpty(topics) && topics.size() == 3) {
            long currentTime = System.currentTimeMillis();

            TransferTransactionBO transferTx = new TransferTransactionBO();
            transferTx.setContractAddress(Keys.toChecksumAddress(logObject.getAddress()));
            transferTx.setFromAddress(parseAddress(topics.get(1)));
            transferTx.setToAddress(parseAddress(topics.get(2)));
            transferTx.setAmountRaw(parseUint256(logObject.getData()));
            transferTx.setBlockNumber(logObject.getBlockNumber().longValue());
            transferTx.setBlockHash(logObject.getBlockHash());
            transferTx.setTransactionHash(logObject.getTransactionHash());
            transferTx.setTransactionTimestamp(currentTime / 1000);
            transferTx.setTransactionTimestampUtc(currentTime);
            transferTx.setTransactionStatus(ChainTransactionStatusEnum.UNKNOWN.getStatus());
            transferTx.setIsNative(false);

            transferTransactionList.add(transferTx);
        }
    }

    /**
     * 创建转账交易对象
     */
    private static TransferTransactionBO createTransferTransactionBO(Transaction transaction, EthBlock.Block block) {
        TransferTransactionBO transferTx = new TransferTransactionBO();
        transferTx.setContractAddress("");
        transferTx.setFromAddress(toChecksumAddress(transaction.getFrom()));
        transferTx.setToAddress(toChecksumAddress(transaction.getTo()));
        transferTx.setAmountRaw(transaction.getValue().toString());
        transferTx.setBlockNumber(block.getNumber().longValue());
        transferTx.setBlockHash(block.getHash());
        transferTx.setTransactionHash(transaction.getHash());
        transferTx.setTransactionTimestamp(block.getTimestamp().longValue());
        transferTx.setTransactionTimestampUtc(block.getTimestamp().longValue() * 1000);
        transferTx.setTransactionStatus(ChainTransactionStatusEnum.UNKNOWN.getStatus());
        transferTx.setIsNative(true);

        return transferTx;
    }

    /**
     * 创建线程池
     */
    private static ExecutorService createThreadPool() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE, r -> {
            Thread t = new Thread(r, "ethereum-fetch-" + r.hashCode());
            t.setDaemon(true);
            return t;
        });
    }

    // ==================== 数据解析方法 ====================

    /**
     * 解析地址
     */
    public static String parseAddress(String addressHex) {
        try {
            List<TypeReference<?>> typeReferences = Collections.singletonList(new TypeReference<Address>() {
            });
            List<Type> typeList = DefaultFunctionReturnDecoder.decode(addressHex, Utils.convert(typeReferences));

            if (!ObjectUtils.isEmpty(typeList) && typeList.size() == 1) {
                return Keys.toChecksumAddress(typeList.get(0).getValue().toString());
            }
        } catch (Exception e) {
            log.warn("Failed to parse address: {}", addressHex, e);
        }
        return null;
    }

    /**
     * 解析Uint256
     */
    public static String parseUint256(String uint256Hex) {
        try {
            List<TypeReference<?>> typeReferences = Collections.singletonList(new TypeReference<Uint256>() {
            });
            List<Type> typeList = DefaultFunctionReturnDecoder.decode(uint256Hex, Utils.convert(typeReferences));

            if (!ObjectUtils.isEmpty(typeList) && typeList.size() == 1) {
                return typeList.get(0).getValue().toString();
            }
        } catch (Exception e) {
            log.warn("Failed to parse uint256: {}", uint256Hex, e);
        }
        return null;
    }

    public static Web3j getWeb3jByRpcUrl(boolean enableProxy, String rpcUrl) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .connectTimeout(Duration.of(10, ChronoUnit.SECONDS));
        if (enableProxy) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            httpClientBuilder.proxy(proxy);
        }
        //
        return Web3j.build(new HttpService(rpcUrl, httpClientBuilder.build()));
    }

    public static String transferERC20(Web3j web3j, String erc20Address, String toAddress, String transferAmountRaw, String fromPrivateKey) throws IOException {
        //
        Credentials credentials = Credentials.create(fromPrivateKey);
        //from余额
        String fromBalanceRaw = getErc20Balance(web3j, credentials.getAddress(), erc20Address);
        if (new BigDecimal(fromBalanceRaw).compareTo(new BigDecimal(transferAmountRaw)) < 0) {
            return null;
        }
        //gas
        BigInteger gasPrice = getGasPrice(web3j);
        BigInteger gasLimit = BigInteger.valueOf(100000);
        //nonce
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //data
        final Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER,
                Arrays.asList(
                        new Address(toAddress),
                        new Uint256(new BigInteger(transferAmountRaw))),
                Collections.emptyList());
        String encodeData = FunctionEncoder.encode(function);
        //
        //生成RawTransaction交易对象
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, erc20Address, encodeData);
        //使用Credentials对象对RawTransaction对象进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        return ethSendTransaction.getTransactionHash();
    }

    public static String transferETH(Web3j web3j, String toAddress, String transferAmountRaw, String fromPrivateKey) throws IOException {
        //gas
        BigInteger gasPrice = getGasPrice(web3j);
        BigInteger gasLimit = BigInteger.valueOf(30000);
        //
        Credentials credentials = Credentials.create(fromPrivateKey);
        //from余额
        String fromBalanceRaw = getEthBalance(web3j, credentials.getAddress());
        BigInteger fromGasFee = new BigInteger(fromBalanceRaw).subtract(new BigInteger(transferAmountRaw));
        //大于gas
        if (fromGasFee.compareTo(gasPrice) < 0) {
            return null;
        }
        //nonce
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        //data
        String encodeData = "";
        //
        //生成RawTransaction交易对象
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, new BigInteger(transferAmountRaw), encodeData);
        //使用Credentials对象对RawTransaction对象进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        return ethSendTransaction.getTransactionHash();
    }

    private static BigInteger getGasPrice(Web3j web3j) throws IOException {
        //gas价格
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        //增加 5% 的价格
        return gasPrice.add(ethGasPrice.getGasPrice().divide(BigInteger.valueOf(20)));
    }


}
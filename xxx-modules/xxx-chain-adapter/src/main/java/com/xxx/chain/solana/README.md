# Solana 链交易监控服务

本项目为 Solana 区块链提供了完整的交易监控解决方案，支持监控 SOL 和 USDT 代币的转账交易。

## 功能特性

- **多用户地址监控**: 支持同时监控多个用户地址
- **SOL 转账监控**: 监控原生 SOL 代币的转账交易
- **USDT 转账监控**: 监控 Solana 上的 USDT 代币转账
- **实时区块监控**: 自动监控新区块并解析交易
- **批量交易处理**: 支持批量查询和处理交易
- **可配置监控参数**: 支持配置最小监控金额、网络类型等
- **统计信息收集**: 提供详细的监控统计信息
- **手动同步支持**: 支持手动同步指定区块范围

## 架构设计

### 核心组件

1. **SolanaConfig**: 配置管理类，包含网络、代币、监控等配置
2. **SolanaRpcClient**: RPC 客户端，负责与 Solana 网络通信
3. **SolanaTransactionParser**: 交易解析器，解析交易数据并识别转账事件
4. **SolanaMultiUserTransactionMonitor**: 主监控服务，协调各个组件工作

### 数据流

```
Solana网络 → RPC客户端 → 交易解析器 → 事件过滤 → 业务处理 → 统计收集
```

## 快速开始

### 1. 配置 Solana 网络

```java
SolanaConfig config = new SolanaConfig();

// 配置网络
SolanaConfig.Network network = config.getNetwork();
network.setNetworkType("mainnet-beta");
network.setRpcUrl("https://api.mainnet-beta.solana.com");
network.setConfirmations(32);

// 配置代币
SolanaConfig.Token.Sol sol = config.getToken().getSol();
sol.setMinAmount("1000000"); // 0.001 SOL

SolanaConfig.Token.Usdt usdt = config.getToken().getUsdt();
usdt.setMinAmount("1000000"); // 1 USDT

// 配置监控
SolanaConfig.Monitor monitor = config.getMonitor();
monitor.setInterval(15000L); // 15秒监控间隔
```

### 2. 实现用户服务接口

```java
public class MyUserService implements SolanaMultiUserTransactionMonitor.UserCryptoAccountService {
    
    @Override
    public boolean isAddressMonitored(String address) {
        // 实现地址监控逻辑
        return checkAddressInDatabase(address);
    }
    
    @Override
    public String getUserIdByAddress(String address) {
        // 实现地址到用户ID的映射
        return getUserFromDatabase(address);
    }
    
    @Override
    public ServiceStats getServiceStats() {
        // 实现统计信息获取
        return getStatsFromDatabase();
    }
}
```

### 3. 启动监控服务

```java
// 创建监控服务
SolanaMultiUserTransactionMonitor monitor = new SolanaMultiUserTransactionMonitor(config, userService);

// 启动监控
monitor.startMonitoring();

// 获取监控统计
Map<String, Object> stats = monitor.getMonitoringStats();

// 停止监控
monitor.stopMonitoring();
```

## 配置说明

### 网络配置

| 参数 | 说明 | 默认值 |
|------|------|--------|
| networkType | 网络类型 (mainnet-beta/testnet/devnet) | mainnet-beta |
| rpcUrl | RPC节点URL | https://api.mainnet-beta.solana.com |
| confirmations | 区块确认数 | 32 |
| connectionTimeout | 连接超时时间(ms) | 30000 |
| readTimeout | 读取超时时间(ms) | 60000 |

### 代币配置

#### SOL 配置
- **Mint地址**: `So11111111111111111111111111111111111111112`
- **精度**: 9位小数
- **最小监控金额**: 可配置

#### USDT 配置
- **Mint地址**: `Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB`
- **精度**: 6位小数
- **最小监控金额**: 可配置

### 监控配置

| 参数 | 说明 | 默认值 |
|------|------|--------|
| enabled | 是否启用监控 | true |
| interval | 监控间隔(ms) | 15000 |
| monitorSol | 是否监控SOL | true |
| monitorUsdt | 是否监控USDT | true |
| batchSize | 批量查询大小 | 100 |
| maxRetries | 最大重试次数 | 3 |

## 交易事件类型

### SOL 转账事件
- **事件类型**: `SOL_TRANSFER`
- **触发条件**: 系统程序转账指令
- **数据字段**: 发送方、接收方、金额、手续费等

### 代币转账事件
- **事件类型**: `TOKEN_TRANSFER`
- **触发条件**: SPL代币程序转账指令
- **数据字段**: 代币类型、发送方、接收方、金额、手续费等

## 性能优化

### 批量处理
- 支持批量查询交易信息，减少RPC调用次数
- 可配置批量大小，平衡性能和内存使用

### 异步处理
- 使用线程池异步处理交易事件
- 支持配置线程数量，适应不同负载

### 错误处理
- 完善的异常处理机制
- 支持重试机制，提高服务稳定性

## 监控和统计

### 实时统计
- 交易次数统计
- 交易金额统计
- 队列状态监控
- 区块同步状态

### 日志记录
- 详细的调试日志
- 错误日志记录
- 性能监控日志

## 扩展性

### 添加新代币
1. 在 `SolanaConfig.Token` 中添加新代币配置
2. 在 `SolanaTransactionParser` 中添加新代币的解析逻辑
3. 在监控服务中添加新代币的过滤逻辑

### 自定义事件处理
1. 实现 `TransactionEventProcessor` 接口
2. 在 `handleTransactionEvent` 方法中添加自定义业务逻辑
3. 支持数据库存储、消息推送、风控检查等

## 注意事项

### 网络选择
- 主网: 生产环境使用，需要真实的SOL支付手续费
- 测试网: 开发测试使用，可以免费获取测试代币
- 开发网: 本地开发使用

### 手续费考虑
- Solana交易需要支付SOL作为手续费
- 建议在测试网进行充分测试后再部署到主网

### 节点选择
- 可以使用公共RPC节点进行测试
- 生产环境建议使用私有节点或付费服务
- 考虑节点的稳定性和响应速度

## 故障排除

### 常见问题

1. **连接超时**: 检查网络配置和RPC节点状态
2. **解析失败**: 检查交易数据格式和解析逻辑
3. **内存溢出**: 调整批量大小和队列容量
4. **性能问题**: 优化监控间隔和线程配置

### 调试建议

1. 启用DEBUG级别日志
2. 使用测试网进行功能验证
3. 监控系统资源使用情况
4. 定期检查错误日志

## 版本历史

- **v1.0.0**: 初始版本，支持SOL和USDT监控
- 支持多用户地址监控
- 支持实时区块监控
- 支持批量交易处理

## 贡献指南

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目采用 [许可证名称] 许可证。

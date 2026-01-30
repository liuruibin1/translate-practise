package com.xxx.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    // private static HttpClient HTTP_CLIENT_INSTANCE = null;
    // private static final CloseableHttpClient HTTP_CLIENT;
    // public static final CloseableHttpAsyncClient HTTP_ASYNC_CLIENT;
    // private static final RequestConfig REQUEST_CONFIG;

    private static final String UN_URL_REGEX = "[^a-zA-Z0-9\\._~:/\\?#&=]";

    private static final String URL_DECODE_REGEX = "%(?![0-9a-fA-F]{2})";

    //    static {
    //        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    //        // 总连接池数量
    //        connectionManager.setMaxTotal(150);
    //        // 可为每个域名设置单独的连接池数量
    //        //connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("xx.xx.xx.xx")), 80);
    //        // setConnectTimeout：设置建立连接的超时时间
    //        // setConnectionRequestTimeout：从连接池中拿连接的等待超时时间
    //        // setSocketTimeout：发出请求后等待对端应答的超时时间
    //        REQUEST_CONFIG = RequestConfig
    //                .custom()
    //                .setConnectTimeout(6000)
    //                .setConnectionRequestTimeout(3000)
    //                .setSocketTimeout(6000)
    //                .build();
    //        // 重试处理器，StandardHttpRequestRetryHandler
    //        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
    //        //同步http client
    //        HTTP_CLIENT = HttpClients.custom()
    //                .setMaxConnTotal(16)
    //                .setMaxConnPerRoute(32)
    //                .setConnectionManager(connectionManager)
    //                .setDefaultRequestConfig(REQUEST_CONFIG)
    //                .setRetryHandler(retryHandler)
    //                .build();
    //        //异步http client
    //        HTTP_ASYNC_CLIENT = HttpAsyncClients
    //                .custom()
    //                .setMaxConnTotal(16)
    //                .setMaxConnPerRoute(32)
    //                .setDefaultRequestConfig(REQUEST_CONFIG)
    //                .build();
    //    }


    public static CloseableHttpClient createHttpClient(boolean enableProxy, String proxyHost, int proxyPort) {
        // 连接池配置
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(32);              // 最大连接数
        connectionManager.setDefaultMaxPerRoute(32);    // 每个路由(目标主机)的最大连接数

        // 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)            // 连接超时(ms)
                .setSocketTimeout(6000)             // 数据传输超时(ms)
                .setConnectionRequestTimeout(3000)  // 从池获取连接超时(ms)
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setProxy(enableProxy ? new HttpHost(proxyHost, proxyPort) : null)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static CloseableHttpClient createAdvancedHttpClient(boolean enableProxy, String proxyHost, int proxyPort) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // SSL配置（信任所有证书，仅限测试环境）
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2", "TLSv1.3"},         // 支持的协议
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        // 连接池配置
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(32);
        connectionManager.setDefaultMaxPerRoute(32);
        connectionManager.setValidateAfterInactivity(30_000); // 30秒空闲连接验证

        // 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(6000)
                .setConnectionRequestTimeout(3000)
                .setRedirectsEnabled(true)
                .setRelativeRedirectsAllowed(true)
                .build();

        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLSocketFactory(sslSocketFactory)
                .setConnectionManager(connectionManager)
                .setProxy(enableProxy ? new HttpHost(proxyHost, proxyPort) : null)
                .setDefaultRequestConfig(requestConfig)
                .setRedirectStrategy(new LaxRedirectStrategy()) // 宽松的重定向策略
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)) // 重试3次
                .setConnectionManagerShared(false) // 不共享连接管理器
                .setConnectionTimeToLive(60, TimeUnit.SECONDS) // 连接存活时间
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36") // 自定义User-Agent
                .disableCookieManagement() // 禁用Cookie管理（按需启用）
                .disableAuthCaching()      // 禁用认证缓存
                .build();
    }

    //    private static HttpClient getHttpClient(boolean enableProxy, String proxyHost, int proxyPort) {
    //        if (HTTP_CLIENT_INSTANCE == null) {
    //            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    //            connectionManager.setMaxTotal(1000);
    //            connectionManager.setDefaultMaxPerRoute(100);
    //            // Create an HttpClient with the given custom dependencies and configuration.
    //            HTTP_CLIENT_INSTANCE = HttpClients.custom()
    //                    .setConnectionManager(connectionManager)
    //                    .setProxy(enableProxy ? new HttpHost(proxyHost, proxyPort) : null)
    //                    .setDefaultRequestConfig(RequestConfig.custom()
    //                            .setExpectContinueEnabled(true)
    //                            .setConnectTimeout(10000)
    //                            .setConnectionRequestTimeout(6000)
    //                            .build())
    //                    .build();
    //        }
    //        return HTTP_CLIENT_INSTANCE;
    //    }

    //public static String doGet(String url, boolean httpProxyEnable, String httpProxyHost, int httpProxyPort) throws Exception {
    //    return doGet(url, null, null, httpProxyEnable, httpProxyHost, httpProxyPort);
    //}

    //public static String doGet(String url, Map<String, Object> requestParams) throws Exception {
    //    return doGet(url, requestParams, null, false, "", -1);
    //}

    //public static String doGet(String url, Map<String, Object> requestParams, Map<String, String> headerParams) throws Exception {
    //    return doGet(url, requestParams, headerParams, false, "", -1);
    //}

    public static String doGet(
            String url,
            Map<String, Object> requestParams,
            Map<String, String> headerParams,
            boolean httpProxyEnable,
            String httpProxyHost,
            int httpProxyPort) throws Exception {
        HttpClient httpClient = createHttpClient(httpProxyEnable, httpProxyHost, httpProxyPort);
        URIBuilder uriBuilder = new URIBuilder(new URI(url));
        if (ObjectUtils.isNotNull(requestParams) && MapUtils.isNotEmpty(requestParams)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            requestParams.forEach((key, value) -> {
                if (value instanceof List<?> list) {
                    for (Object item : list) {
                        nameValuePairList.add(new BasicNameValuePair(key, String.valueOf(item)));
                    }
                } else {
                    nameValuePairList.add(new BasicNameValuePair(key, value.toString()));
                }
            });
            uriBuilder.addParameters(nameValuePairList);
        }
        final HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_16_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
        if (MapUtils.isNotEmpty(headerParams)) {
            headerParams.forEach(httpGet::setHeader);
        }
        return httpClient.execute(httpGet, response -> {
            HttpEntity entity = response.getEntity();
            String resultString = null;
            if (null != entity) {
                resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
            return resultString;
        });
    }

    public static String doPostJSON(
            String url,
            Map<String, Object> requestParams,
            Map<String, String> headerParams,
            boolean httpProxyEnable,
            String httpProxyHost,
            int httpProxyPort) throws Exception {
        HttpClient httpClient = createHttpClient(httpProxyEnable, httpProxyHost, httpProxyPort);
        final HttpPost httpPost = new HttpPost(new URI(url));
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_16_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
        if (MapUtils.isNotEmpty(headerParams)) {
            headerParams.forEach(httpPost::setHeader);
        }

        httpPost.setHeader("Content-Type", "application/json");
        if (ObjectUtils.isNotNull(requestParams)) {
            String jsonString = JSONUtil.mapToString(requestParams);
            if (ObjectUtils.isNotNull(jsonString) && StringUtils.isNotEmpty(jsonString)) {
                httpPost.setEntity(new StringEntity(jsonString, StandardCharsets.UTF_8));
            }
        }

        return httpClient.execute(httpPost, response -> {
            HttpEntity entity = response.getEntity();
            String resultString = null;
            if (null != entity) {
                resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
            return resultString;
        });
    }

    //public static String doPost(
    //        String uri,
    //        boolean httpProxyEnable,
    //        String httpProxyHost,
    //        int httpProxyPort,
    //        byte[] bytes) {
    //    return doPost(uri, httpProxyEnable, httpProxyHost, httpProxyPort, null, bytes);
    //}

    //public static String doPost(
    //        String url,
    //        boolean httpProxyEnable,
    //        String httpProxyHost,
    //        int httpProxyPort,
    //        Map<String, String> headerParams,
    //        byte[] bytes) {
    //    HttpClient httpClient = createHttpClient(httpProxyEnable, httpProxyHost, httpProxyPort);
    //    CloseableHttpResponse response = null;
    //    try {
    //        HttpPost httpPost = new HttpPost(url);
    //        httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_16_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
    //        if (MapUtils.isNotEmpty(headerParams)) {
    //            headerParams.forEach(httpPost::setHeader);
    //        }
    //        HttpEntity httpEntity = new ByteArrayEntity(bytes);
    //        httpPost.setEntity(httpEntity);
    //        response = (CloseableHttpResponse) httpClient.execute(httpPost);
    //        int statusCode = response.getStatusLine().getStatusCode();
    //        if (HttpStatus.SC_OK == statusCode) {
    //            HttpEntity entity = response.getEntity();
    //            if (null != entity) {
    //                return EntityUtils.toString(entity, "utf-8");
    //            }
    //        }
    //    } catch (Exception e) {
    //        LOGGER.error(e.getMessage(), e);
    //    } finally {
    //        try {
    //            if (null != response) {
    //                response.close();
    //            }
    //        } catch (IOException e) {
    //            LOGGER.error(e.getMessage(), e);
    //        }
    //    }
    //    return null;
    //}

    public static String doPost(
            String url,
            boolean httpProxyEnable,
            String httpProxyHost,
            int httpProxyPort,
            Map<String, String> headerParams,
            Map<String, Object> requestParams) {
        HttpClient httpClient = createHttpClient(httpProxyEnable, httpProxyHost, httpProxyPort);
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_16_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");

            // 设置额外的 Header
            if (MapUtils.isNotEmpty(headerParams)) {
                headerParams.forEach(httpPost::setHeader);
            }

            // **设置表单参数**
            if (MapUtils.isNotEmpty(requestParams)) {
                List<NameValuePair> paramList = requestParams.entrySet().stream()
                        .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue().toString()))
                        .collect(Collectors.toList());

                HttpEntity httpEntity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                httpPost.setEntity(httpEntity);
            }

            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return null; // 确保方法有返回值
    }

    public static String doPostJSON(
            String url,
            boolean httpProxyEnable,
            String httpProxyHost,
            int httpProxyPort,
            Map<String, String> headerParams,
            String jsonPayload) throws IOException, URISyntaxException {
        HttpClient httpClient = createHttpClient(httpProxyEnable, httpProxyHost, httpProxyPort);
        CloseableHttpResponse response;
        final HttpPost httpPost = new HttpPost(new URI(url));
        //httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_16_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
        if (headerParams != null) {
            headerParams.forEach(httpPost::setHeader);
        }
        httpPost.setHeader("Content-Type", "application/json");
        StringEntity stringEntity = new StringEntity(jsonPayload, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);

        //return httpClient.execute(httpPost, response -> {
        //    HttpEntity entity = response.getEntity();
        //    String resultString = null;
        //    if (null != entity) {
        //        resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        //    }
        //    EntityUtils.consume(entity);
        //    return resultString;
        //});
        response = (CloseableHttpResponse) httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK == statusCode) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        }
        try {
            response.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null; // 确保方法有返回值
    }

    public static String doPostFormData(
            String url,
            boolean httpProxyEnable,
            String httpProxyHost,
            int httpProxyPort,
            Map<String, Object> formData,
            Map<String, String> headers) {
        CloseableHttpClient httpClient = createHttpClient(httpProxyEnable, httpProxyHost, httpProxyPort);
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try {
            // ======================
            // 设置 headers
            // ======================
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            // 默认 Content-Type
            if (headers == null || !headers.containsKey("Content-Type")) {
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }

            // ======================
            // 表单参数填充
            // ======================
            if (formData != null && !formData.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<>();
                for (Map.Entry<String, Object> entry : formData.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));
            }

            // 执行请求
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;

        } catch (Exception e) {
            log.error("POST FormData request error: {}", e.getMessage(), e);
            throw new RuntimeException(e);

        } finally {
            try {
                if (response != null) response.close();
                httpClient.close();
            } catch (IOException ioe) {
                log.error("Error closing HttpClient: {}", ioe.getMessage(), ioe);
            }
        }
    }

    public static String replaceUnURLStr(String url) {
        Pattern pattern = Pattern.compile(UN_URL_REGEX);
        Matcher matcher = pattern.matcher(url);
        return matcher.replaceAll("");
    }

    public static void main(String[] args) {
        //try (CloseableHttpClient httpClient = createHttpClient(true, "127.0.0.1", 7890)) {
        //    HttpGet request = new HttpGet("http://test.pay.ballaratpay.com/");
        //
        //    try (CloseableHttpResponse response = httpClient.execute(request)) {
        //        System.out.println("Status: " + response.getStatusLine());
        //        String content = EntityUtils.toString(response.getEntity());
        //        System.out.println("Response: " + content);
        //    }
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //String url = "http://test-pay.ballaratpay.com/rama/pay";
        //Map<String, String> headerParameterMap = new HashMap<String, String>();
        //headerParameterMap.put("Content-Type", "application/x-www-form-urlencoded");
        //Map<String, String> formParams = new HashMap<>();
        //formParams.put("app_id", "T1755007588");
        //formParams.put("order_no", "Q202507150801548810907077369");  // 使用 curl 中的值
        //formParams.put("order_time", "1752546715");  // 使用 curl 中的值
        //formParams.put("order_amount", "500.00");    // 使用 curl 中的值
        //formParams.put("product_name", "payin");     // 使用 curl 中的值
        //formParams.put("user_id", "457065606");      // 使用 curl 中的值
        //formParams.put("sign", "D05D80F6C2E21C31DA54DE2FD39A3EC6");  // 使用 curl 中的值
        //String json = doPost(url,false,"127.0.0.1",7890,headerParameterMap,formParams);
        //System.out.println(json);
        Map<String, Object> requestParamMap = new HashMap<>();

        String url = "https://api.les-ponts.com/api/sms/send";
        Map<String, String> headerParameterMap = new HashMap<>();
        headerParameterMap.put("Content-Type", "application/json;charset=utf-8");
        requestParamMap.put("accesskey", "10534");
        requestParamMap.put("secret", "1ma1QpiI");  // 使用 curl 中的值
        requestParamMap.put("mobile", "18649933397");  // 使用 curl 中的值
        requestParamMap.put("content", "您好，您的订单已发货");    // 使用 curl 中的值
        try {
            String json = doPostJSON(url, requestParamMap, headerParameterMap, false, "127.0.0.1", 7890);
            System.out.println(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
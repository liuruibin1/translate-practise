package com.xxx.common.core.utils;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;

import java.util.Map;
import java.util.concurrent.Future;

public class HttpAsyncClientUtil {

    public static CloseableHttpAsyncClient createHttpClient(boolean enableProxy, String proxyHost, int proxyPort) throws IOReactorException {
        // IO 反应器配置
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors()) // 通常设置为CPU核心数
                .setConnectTimeout(5000) // 连接超时(ms)
                .setSoTimeout(5000)      // Socket超时(ms)
                .setSoKeepAlive(true)    // 启用TCP Keep-Alive
                .build();

        // 连接池配置
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(ioReactorConfig));

        connManager.setMaxTotal(200);          // 最大连接数
        connManager.setDefaultMaxPerRoute(50); // 每个路由(目标主机)的最大连接数

        // 创建异步客户端
        return HttpAsyncClients.custom()
                .setConnectionManager(connManager)
                .setProxy(enableProxy ? new HttpHost(proxyHost, proxyPort) : null)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(3000) // 从池中获取连接的超时时间
                        .build())
                .build();
    }

    /**
     * 异步POST请求 - 使用回调
     *
     * @param client HTTP异步客户端
     * @param url 请求URL
     * @param jsonData JSON数据
     * @param headers 请求头
     * @param callback 回调处理器
     * @return Future<HttpResponse>
     */
    public static Future<HttpResponse> postAsyncWithCallback(
            CloseableHttpAsyncClient client,
            String url,
            String jsonData,
            Map<String, String> headers,
            FutureCallback<HttpResponse> callback
    ) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体
        if (jsonData != null && !jsonData.isEmpty()) {
            StringEntity entity = new StringEntity(jsonData, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
        }

        // 设置请求头
        if (headers != null) {
            headers.forEach(httpPost::setHeader);
        }

        return client.execute(httpPost, callback);
    }

    //    public static CloseableHttpAsyncClient createAdvancedHttpClient(boolean enableProxy, String proxyHost, int proxyPort) throws IOReactorException {
    //        IOReactorConfig ioConfig = IOReactorConfig.custom()
    //                .setIoThreadCount(4) // 根据负载调整，通常4-8个
    //                .setSelectInterval(100) // 选择器轮询间隔(ms)
    //                .setSoReuseAddress(true) // 启用地址重用
    //                .setTcpNoDelay(true)     // 禁用Nagle算法
    //                .setSoKeepAlive(true)
    //                .setConnectTimeout(3000)
    //                .setSoTimeout(5000)
    //                .build();
    //
    //        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(
    //                new DefaultConnectingIOReactor(ioConfig),
    //                RegistryBuilder.<ConnectionSocketFactory>create()
    //                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
    //                        .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
    //                        .build(),
    //                PoolConcurrencyPolicy.LAX,
    //                PoolReusePolicy.LIFO,
    //                TimeValue.ofMinutes(5), // 连接存活时间
    //                null);
    //
    //        connManager.setMaxTotal(500);
    //        connManager.setDefaultMaxPerRoute(100);
    //        connManager.setValidateAfterInactivity(TimeValue.ofSeconds(30)); // 空闲连接验证间隔
    //
    //        return HttpAsyncClients.custom()
    //                .setConnectionManager(connManager)
    //                .setDefaultIOReactorConfig(ioConfig)
    //                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, TimeValue.ofSeconds(1))) // 重试策略
    //                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
    //                .setConnectionTimeToLive(TimeValue.ofMinutes(5)) // 连接TTL
    //                .setDefaultRequestConfig(RequestConfig.custom()
    //                        .setConnectTimeout(3000)
    //                        .setSocketTimeout(5000)
    //                        .setConnectionRequestTimeout(2000)
    //                        .setContentCompressionEnabled(true) // 启用压缩
    //                        .build())
    //                .setMaxConnPerRoute(100)
    //                .setMaxConnTotal(500)
    //                .setKeepAliveStrategy((response, context) -> 60000) // Keep-Alive持续时间(ms)
    //                .build();
    //    }

    public static void main(String[] args) throws Exception {
        CloseableHttpAsyncClient httpClient = createHttpClient(true, "127.0.0.1", 7890);
        httpClient.start();
        try {
            HttpGet request = new HttpGet("https://example.com/api");
            httpClient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse response) {
                    System.out.println("Response: " + response.getStatusLine());
                    // 处理响应
                }

                @Override
                public void failed(Exception ex) {
                    System.err.println("Request failed: " + ex.getMessage());
                }

                @Override
                public void cancelled() {
                    System.out.println("Request cancelled");
                }
            });
            // 等待异步操作完成
            Thread.sleep(5000);
        } finally {
            httpClient.close();
        }
    }

}
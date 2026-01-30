package com.xxx.gateway.utils;

import com.xxx.common.core.text.Convert;
import com.xxx.common.core.utils.JSONUtil;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.common.core.vo.BaseResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

public class WebFluxUtils {

    /**
     * 获取String参数
     */
    public static String getParameter(ServerWebExchange exchange, String name) {
        return getParamMap(getRequest(exchange)).get(name);
    }


    /**
     * 获取String参数
     */
    public static String getParameter(ServerWebExchange exchange, String name, String defaultValue) {
        return Convert.toStr(getParameter(exchange, name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(ServerWebExchange exchange, String name) {
        return Convert.toInt(getParameter(exchange, name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(ServerWebExchange exchange, String name, Integer defaultValue) {
        return Convert.toInt(getParameter(exchange, name), defaultValue);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(ServerWebExchange exchange, String name) {
        return Convert.toBool(getParameter(exchange, name));
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(ServerWebExchange exchange, String name, Boolean defaultValue) {
        return Convert.toBool(getParameter(exchange, name), defaultValue);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
    public static Map<String, String[]> getParams(ServletRequest request) {
    final Map<String, String[]> map = request.getParameterMap();
    return Collections.unmodifiableMap(map);
    }*/

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServerHttpRequest}
     * @return Map
     */
    public static Map<String, String> getParamMap(ServerHttpRequest request) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        return queryParams.toSingleValueMap();
    }

    /**
     * 获取request
     */
    public static ServerHttpRequest getRequest(ServerWebExchange exchange) {
        return exchange.getRequest();
    }

    /**
     * 获取response
     */
    public static ServerHttpResponse getResponse(ServerWebExchange exchange) {
        return exchange.getResponse();
    }


    /*public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }*/

    public static String getHeader(HttpHeaders httpHeaders, String name) {
        return httpHeaders.getFirst(name);
    }

    public static HttpHeaders getHeaders(ServerHttpRequest request) {
        return request.getHeaders();
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
    public static void renderString(HttpServletResponse response, String string) {
    try {
    response.setStatus(200);
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().print(string);
    } catch (IOException e) {
    e.printStackTrace();
    }
    }*/

    /**
     * 是否是Ajax异步请求
     *
     * @param exchange
     */
    public static boolean isAjaxRequest(ServerWebExchange exchange) {

        ServerHttpRequest request = getRequest(exchange);
        HttpHeaders headers = getHeaders(request);

        String accept = getHeader(headers, "accept");
        if (accept != null && accept.contains("application/json")) {
            return true;
        }

        String xRequestedWith = getHeader(headers, "X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
            return true;
        }
        String uri = request.getURI().toString();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }
        String ajax = getParameter(exchange, "__ajax");
        return StringUtils.inStringIgnoreCase(ajax, "json", "xml");
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param value    响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value) {
        return webFluxResponseWriter(response, HttpStatus.OK, value, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param code     响应状态码
     * @param value    响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code) {
        return webFluxResponseWriter(response, HttpStatus.OK, value, code);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param status   http状态码
     * @param code     响应状态码
     * @param value    响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(
            ServerHttpResponse response,
            HttpStatus status,
            Object value,
            int code
    ) {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param code        响应状态码
     * @param value       响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(
            ServerHttpResponse response,
            String contentType,
            HttpStatus status,
            Object value,
            int code
    ) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        //R<?> result = R.fail(code, value.toString());
        BaseResponse baseResponse = BaseResponse.error(code, value.toString());
        DataBuffer dataBuffer = response
                .bufferFactory()
                .wrap(Objects.requireNonNull(JSONUtil.objectToString(baseResponse)).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, BaseResponse baseResponse) {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK, baseResponse);
    }

    public static Mono<Void> webFluxResponseWriter(
            ServerHttpResponse response,
            String contentType,
            HttpStatus status,
            BaseResponse baseResponse
    ) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        DataBuffer dataBuffer = response
                .bufferFactory()
                .wrap(Objects.requireNonNull(JSONUtil.objectToString(baseResponse)).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
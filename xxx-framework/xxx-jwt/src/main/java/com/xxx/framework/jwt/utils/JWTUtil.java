package com.xxx.framework.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JWTUtil {

    public static final String PREFIX = "Bearer ";

    public static boolean verify(String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                //.withIssuer(issuer)
                .build();
        DecodedJWT decodedjwt = verifier.verify(token);
        //return new Date().before(decodedjwt.getExpiresAt());
        return decodedjwt != null;
    }

    public static String getClaimByTokenClaimKeyAsString(String token, String claimKey) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Claim claim = decodedJWT.getClaim(claimKey);
            return claim.asString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getClaimByTokenClaimKeyAsLong(String token, String claimKey) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Claim claim = decodedJWT.getClaim(claimKey);
            return Long.parseLong(claim.asString());
        } catch (Exception e) {
            return null;
        }
    }

    //public static String buildToken(
    //        String claimKey,
    //        Object claimValue,
    //        String secret,
    //        String issuer,
    //        long tokenValidDuration,
    //        TimeUnit timeUnit) {
    //    Map<String, Object> claimMap = new HashMap<>();
    //    claimMap.put(claimKey, claimValue);
    //    return buildToken(claimMap, secret, issuer, tokenValidDuration, timeUnit);
    //}

    public static String buildToken(
            Map<String, Object> claimMap,
            String secret,
            String issuer,
            long tokenValidDuration,
            TimeUnit timeUnit) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        builder.withHeader(Map.of("alg", "HS256"));
        //.withIssuer(issuer)
        //.withIssuedAt(new Date())
        //.withExpiresAt(getTokenExpireDatetime(tokenValidDuration, timeUnit));
        for (Map.Entry<String, Object> stringEntry : claimMap.entrySet()) {
            builder.withClaim(stringEntry.getKey(), stringEntry.getValue().toString());
        }
        return builder.sign(algorithm);
    }

    public static Date getTokenExpireDatetime(long tokenValidDuration, TimeUnit timeUnit) {
        return new Date(System.currentTimeMillis() + timeUnit.toMillis(tokenValidDuration));
    }

    /*
    public static String getUsername(ServerWebExchange exchange) throws RuntimeException {
        String token = getToken(exchange);
        String username = getUsernameByToken(token);
        if (username != null && username.trim().length() != 0) {
            return username;
        } else {
            throw new RuntimeException("未获取到用户");
        }
    }

    public static String getClaimByName(ServerWebExchange exchange, String name) {
        String token = getToken(exchange);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(name).asString();
    }

    public static String getToken(HttpServletRequest request) throws RuntimeException {
        String token = request.getHeader(REQUEST_HEADER_TOKEN_NAME);
        return replaceTokenPrefix(token);
    }
    */

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request) throws RuntimeException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return replaceTokenPrefix(authorization);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(PREFIX)) {
            token = token.replaceFirst(JWTUtil.PREFIX, "");
        }
        return token;
    }

    /*public static void main(String[] args) {
        String claimKey = "userName";
        Object claimValue = "admin";
        String secret = "zywlajreniljnilun_";
        long tokenValidDuration = 100000000L;
        String issuer = "zywl";
        String token = buildToken(claimKey, claimValue, secret, issuer, tokenValidDuration, TimeUnit.MINUTES);
        System.out.println("token = %s" + token);
    }*/

    public static void main(String[] args) {
        Algorithm algorithm = Algorithm.HMAC256("");
        String token = JWT.create()
                .withHeader(Map.of("alg", "HS256")) // 仅保留 alg 字段，去掉 typ
                .withClaim("userId", 1003)
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 10000))
                .sign(algorithm);
        System.out.println("JWT Token: " + token);
    }

}
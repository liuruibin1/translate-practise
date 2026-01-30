package com.xxx.base.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.xxx.base.vo.Response;
import com.xxx.common.core.exception.BusinessRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import static com.xxx.common.core.enumerate.BizErrorEnum._500_002;
import static com.xxx.common.core.enumerate.BizErrorEnum._500_500;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 BusinessException
     */
    @ExceptionHandler(BusinessRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Response> handleBusinessException(BusinessRuntimeException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return Mono.just(Response.error(_500_500));
    }

    /**
     * 处理 Token 无效异常（JWT 相关异常）
     */
    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<Response> handleJWTVerificationException(JWTVerificationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return Mono.just(Response.error(_500_002));
    }

    ///**
    // * 处理所有 RuntimeException
    // */
    //@ExceptionHandler(RuntimeException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //public Mono<Response> handleRuntimeException(RuntimeException ex) {
    //    return Mono.just(Response.error(_990009));
    //}

    /**
     * 处理所有 Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Response> handleException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return Mono.just(Response.error(_500_500));
    }
}

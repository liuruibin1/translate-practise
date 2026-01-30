package com.xxx.base.vo;

import com.xxx.common.core.enumerate.BizErrorEnum;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.vo.BaseResponse;
import com.xxx.open.enumerate.OpenApiBizErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Response extends BaseResponse {

    private BizErrorEnum bizError = null;

    public Response() {
    }

    public Response(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MESSAGE_TAG, msg);
    }

    public Response(boolean success, int code, String msg, Object data) {
        super.put(SUCCESS_TAG, success);
        super.put(CODE_TAG, code);
        super.put(MESSAGE_TAG, msg);
        if (ObjectUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    public static Response success() {
        return Response.success(SUCCESS_MESSAGE);
    }

    public static Response success(String msg) {
        return Response.success(msg, null);
    }

    public static Response success(Object data) {
        return Response.success(SUCCESS_MESSAGE, data);
    }

    public static Response success(String msg, Object data) {
        return new Response(true, SUCCESS_CODE, msg, data);
    }

    public static Response error(BizErrorEnum bizError) {
        Response response = new Response(false, bizError.getCode(), bizError.getDescriptionEn(), null);
        response.setBizError(bizError);
        return response;
    }

    public static Response error(BizErrorEnum bizError, String... args) {
        return new Response(false, bizError.getCode(), String.format(bizError.getDescriptionEn(), (Object[]) args), null);
    }

    public static Response error(OpenApiBizErrorEnum openBizError) {
        return new Response(false, openBizError.getCode(), openBizError.getDescription(), null);
    }

    public static Response error(String msg) {
        return Response.error(msg, null);
    }

    public static Response error(String msg, Object data) {
        return new Response(false, ERROR_CODE, msg, data);
    }

    public static Response error() {
        return Response.error("error");
    }

}
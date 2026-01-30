package com.xxx.auth.response;

import com.xxx.base.vo.Response;
import com.xxx.common.core.enumerate.BizErrorEnum;
import com.xxx.common.core.utils.ObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends Response {

    public static final String ACCESS_TOKEN_TAG = "accessToken";

    public static final String REFRESH_TOKEN_TAG = "refreshToken";

    private BizErrorEnum bizError = null;

    private String accessToken = null;

    public LoginResponse(boolean success, int code, String msg, Object data, String accessToken) {
        super.put(SUCCESS_TAG, success);
        super.put(CODE_TAG, code);
        super.put(MESSAGE_TAG, msg);
        if (ObjectUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
        super.put(ACCESS_TOKEN_TAG, accessToken);
    }

    public static LoginResponse success(String accessToken) {
        return new LoginResponse(true, SUCCESS_CODE, null, null, accessToken);
    }

    public static LoginResponse error(BizErrorEnum bizError) {
        LoginResponse response = new LoginResponse(false, bizError.getCode(), bizError.getDescriptionCn(), null, null);
        response.setBizError(bizError);
        return response;
    }

    public static LoginResponse error(String msg) {
        return LoginResponse.error(msg, null);
    }

    public static LoginResponse error(String msg, Object data) {
        return new LoginResponse(false, ERROR_CODE, msg, data, null);
    }

}
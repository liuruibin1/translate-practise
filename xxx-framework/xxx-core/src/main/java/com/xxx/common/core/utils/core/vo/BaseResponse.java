package com.xxx.common.core.utils.core.vo;

import com.xxx.common.core.utils.core.utils.ObjectUtils;
import com.xxx.common.core.utils.core.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseResponse extends HashMap<String, Object> {

    public static final int SUCCESS_CODE = 200;

    //public static final int WARN = 999998;

    public static final int ERROR_CODE = 999999;

    /**
     * 成功标识
     */
    public static final String SUCCESS_TAG = "success";

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MESSAGE_TAG = "message";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    public static final String SUCCESS_MESSAGE = "successfully";

    /**
     * 初始化一个新创建的 BaseResponse 对象，使其表示一个空消息。
     */
    public BaseResponse() {
    }

    /**
     * 初始化一个新创建的 BaseResponse 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public BaseResponse(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MESSAGE_TAG, msg);
    }

    /**
     * 初始化一个新创建的 BaseResponse 对象
     *
     * @param success 成功标识
     * @param code    状态码
     * @param msg     返回内容
     * @param data    数据对象
     */
    public BaseResponse(boolean success, int code, String msg, Object data) {
        super.put(SUCCESS_TAG, success);
        super.put(CODE_TAG, code);
        super.put(MESSAGE_TAG, msg);
        if (ObjectUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static BaseResponse success() {
        return BaseResponse.success(SUCCESS_MESSAGE);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static BaseResponse success(Object data) {
        return BaseResponse.success(SUCCESS_MESSAGE, data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static BaseResponse success(String msg) {
        return BaseResponse.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static BaseResponse success(String msg, Object data) {
        return new BaseResponse(true, SUCCESS_CODE, msg, data);
    }

//    /**
//     * 返回警告消息
//     *
//     * @param msg 返回内容
//     * @return 警告消息
//     */
//    public static BaseResponse warn(String msg) {
//        return BaseResponse.warn(msg, null);
//    }

//    /**
//     * 返回警告消息
//     *
//     * @param msg  返回内容
//     * @param data 数据对象
//     * @return 警告消息
//     */
//    public static BaseResponse warn(String msg, Object data) {
//        return new BaseResponse(false, WARN, msg, data);
//    }

    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static BaseResponse error() {
        return BaseResponse.error("error");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 错误消息
     */
    public static BaseResponse error(String msg) {
        return BaseResponse.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static BaseResponse error(String msg, Object data) {
        return new BaseResponse(false, ERROR_CODE, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 错误消息
     */
    public static BaseResponse error(int code, String msg) {
        return new BaseResponse(false, code, msg, null);
    }

//    /**
//     * 返回错误消息
//     *
//     * @param code 状态码
//     * @param msg  返回内容
//     * @return 错误消息
//     */
//    public static BaseResponse error(BizErrorEnum bizError) {
//        return new BaseResponse(false, bizError.getCode(), bizError.getLabelEn(), null);
//    }

    /**
     * 判定
     *
     * @param success  成功标识
     * @param msg 消息
     * @return 判定结果
     */
    public static BaseResponse decide(boolean success, String msg) {
        return decide(success, msg, null);
    }

    /**
     * 判定
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static BaseResponse decide(boolean success, String msg, Object data) {
        if (success) {
            if (ObjectUtils.isNotNull(data)) {
                return success(msg, data);
            } else {
                if (StringUtils.isNotEmpty(msg)) {
                    return success(msg);
                } else {
                    return success();
                }
            }
        } else {
            return error(msg);
        }
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess() {
        return Objects.equals(SUCCESS_CODE, this.get(CODE_TAG)) && Objects.equals(true, this.get(SUCCESS_TAG));
    }

//    /**
//     * 是否为警告消息
//     *
//     * @return 结果
//     */
//    public boolean isWarn() {
//        return Objects.equals(WARN, this.get(CODE_TAG)) && Objects.equals(false, this.get(SUCCESS_TAG));
//    }

    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError() {
        return Objects.equals(ERROR_CODE, this.get(CODE_TAG)) && Objects.equals(false, this.get(SUCCESS_TAG));
    }

    public Object setData(Object value) {
        return put(DATA_TAG, value);
    }

    public Object getData() {
        return get(DATA_TAG);
    }

    public void setMessage(String msg) {
        put(MESSAGE_TAG, msg);
    }

    public String getMessage() {
        return get(MESSAGE_TAG).toString();
    }

    public void setSuccess(boolean success) {
        put(SUCCESS_TAG, success);
    }

    /*public Boolean getSuccess() {
        return (Boolean) get(SUCCESS_TAG);
    }*/

    public void setCode(int code) {
        put(CODE_TAG, code);
    }

    public Integer getCode() {
        return Integer.valueOf(get(CODE_TAG).toString());
    }

    /**
     * 链式调用
     *
     * @param key KEY
     * @param value VALUE
     * @return 响应结果
     */
    @Override
    public BaseResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}

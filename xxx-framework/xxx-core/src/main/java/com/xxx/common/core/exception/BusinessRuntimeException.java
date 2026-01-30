package com.xxx.common.core.exception;

import com.xxx.common.core.enumerate.BizErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessRuntimeException extends RuntimeException {

    private final int code;

    public BusinessRuntimeException(String message) {
        super(message);
        this.code = BizErrorEnum._500_500.getCode();
    }

    public BusinessRuntimeException(BizErrorEnum bizErrorEnum) {
        super(bizErrorEnum.getDescriptionEn());
        this.code = bizErrorEnum.getCode();
    }

}
package com.xxx.common.core.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xxx.common.core.converter.TimestampToDateConverter;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("用户ID")
    @ColumnWidth(20)
    private Long id;

    @ExcelProperty("手机号码")
    @ColumnWidth(15)
    private String phoneNumber;

    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String email;

    @ExcelProperty("推荐码")
    @ColumnWidth(20)
    private String referralCode;

    @ExcelProperty("VIP等级")
    @ColumnWidth(15)
    private Integer vipLevel;

    @ExcelProperty("总金额")
    @ColumnWidth(30)
    private String totalBalance;

    @ExcelProperty("可用余额")
    @ColumnWidth(30)
    private String availableBalance;

    @ExcelProperty("总盈亏金额")
    @ColumnWidth(30)
    private String profitOrLoss;

    @ExcelProperty(value = "注册时间", converter = TimestampToDateConverter.class)
    @ColumnWidth(20)
    private Long createTsMs;

}

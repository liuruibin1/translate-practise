package com.xxx.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdListOfLongRemarkDTO {
    private List<Long> idList;
    private String remark;
}

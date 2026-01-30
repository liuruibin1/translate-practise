package com.xxx.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdListOfLongIsEnabledDTO {
    private List<Long> idList;
    private Boolean isEnabled;
}

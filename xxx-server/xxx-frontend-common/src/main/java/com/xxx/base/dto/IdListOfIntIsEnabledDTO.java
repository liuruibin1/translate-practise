package com.xxx.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdListOfIntIsEnabledDTO {
    private List<Integer> idList;
    private Boolean isEnabled;
}

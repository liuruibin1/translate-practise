package com.xxx.base.dto;

import lombok.Data;

import java.util.List;

@Data
public class IdListOfStringIsEnabledDTO {
    private List<String> idList;
    private Boolean isEnabled;
}

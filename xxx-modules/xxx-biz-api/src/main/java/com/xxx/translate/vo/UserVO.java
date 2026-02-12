package com.xxx.translate.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xxx.entity.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserVO extends User {
}

package com.xxs.aispringbooot.pojo.vo;
import lombok.Data;
@Data
public class UserLoginVo {
    private String token;
    private String roleType;
    private UserInfoVo userInfo;
}

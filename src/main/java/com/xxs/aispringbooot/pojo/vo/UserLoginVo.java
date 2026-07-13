package com.xxs.aispringbooot.pojo.vo;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserLoginVo {
    private String token;
    private String roleType;
    private UserInfoVo userInfo;
}

package com.xxs.aispringbooot.pojo.vo;
import lombok.Data;
@Data
public class UserInfoVo {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer gender;
    private String genderDisplayName;
    private String birthday;
    private Integer userType;
    private String userTypeDisplayName;
    private Integer status;
    private String statusDisplayName;
    private String displayName;
    private String createdAt;
    private String updatedAt;
}

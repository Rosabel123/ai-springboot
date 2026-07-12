package com.xxs.aispringbooot.pojo.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class UserLoginDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 12, message = "用户名长度必须在1到12之间")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 12, message = "密码长度必须在6到12之间")
    private String password;
}

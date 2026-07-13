package com.xxs.aispringbooot.controller;

import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.pojo.dto.UserLoginDTO;
import com.xxs.aispringbooot.pojo.vo.UserLoginVo;
import com.xxs.aispringbooot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public Result login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        Result<UserLoginVo> result = userService.login(userLoginDTO);
        return result;
    }
}

package com.xxs.aispringbooot.controller;

import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.pojo.DTO.UserLoginDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @PostMapping("/login")
    public Result login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        System.out.println(userLoginDTO.getUsername());
        System.out.println(userLoginDTO.getPassword());
        return Result.success();
    }
}

package com.xxs.aispringbooot.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.pojo.dto.UserLoginDTO;
import com.xxs.aispringbooot.pojo.dto.UserRegisterDTO;
import com.xxs.aispringbooot.pojo.entity.User;
import com.xxs.aispringbooot.pojo.vo.UserInfoVo;
import com.xxs.aispringbooot.pojo.vo.UserLoginVo;
import jakarta.validation.Valid;

public interface UserService extends IService<User> {
    UserLoginVo login(@Valid UserLoginDTO userLoginDTO);
    UserInfoVo register(@Valid UserRegisterDTO userRegisterDTO);
    UserInfoVo getUserById(Long userId);
}

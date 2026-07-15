package com.xxs.aispringbooot.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.common.utils.JwtTokenUtil;
import com.xxs.aispringbooot.pojo.dto.UserLoginDTO;
import com.xxs.aispringbooot.pojo.dto.UserRegisterDTO;
import com.xxs.aispringbooot.pojo.vo.UserInfoVo;
import com.xxs.aispringbooot.pojo.vo.UserLoginVo;
import com.xxs.aispringbooot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVo> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVo userLoginVo = userService.login(userLoginDTO);
        return Result.success(userLoginVo);
    }
    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/add")
    public Result<UserInfoVo> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        UserInfoVo userInfoVo = userService.register(userRegisterDTO);
        return Result.success(userInfoVo);
    }
    /**
     * 获取用户信息接口
     * @return
     */
    @GetMapping("/current")
    public Result<UserInfoVo> getCurrentUser() {
        // 如何从token中解析出用户的id
        String token = JwtTokenUtil.getCurrentToken();
        DecodedJWT jwt = JwtTokenUtil.verifyToken(token);
        Long userId = jwt.getClaim("userId").asLong();
        // 调用service层获取用户详情
        UserInfoVo userInfoVo = userService.getUserById(userId);
        return Result.success(userInfoVo);
    }

}

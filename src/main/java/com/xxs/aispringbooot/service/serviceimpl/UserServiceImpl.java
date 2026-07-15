package com.xxs.aispringbooot.service.serviceimpl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.common.enums.UserStatus;
import com.xxs.aispringbooot.common.enums.UserType;
import com.xxs.aispringbooot.common.exception.BusinessException;
import com.xxs.aispringbooot.common.utils.JwtTokenUtil;
import com.xxs.aispringbooot.mapper.UserMapper;
import com.xxs.aispringbooot.pojo.dto.UserLoginDTO;
import com.xxs.aispringbooot.pojo.dto.UserRegisterDTO;
import com.xxs.aispringbooot.pojo.entity.User;
import com.xxs.aispringbooot.pojo.vo.UserInfoVo;
import com.xxs.aispringbooot.pojo.vo.UserLoginVo;
import com.xxs.aispringbooot.service.UserService;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserLoginVo login(@Valid UserLoginDTO userLoginDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userLoginDTO.getUsername())
                    .or()
                    .eq(User::getEmail, userLoginDTO.getUsername());

        User user = getOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String inputPassword = userLoginDTO.getPassword().trim();
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        if (!user.isActive()) {
            throw new BusinessException("用户已被禁用");
        }
        //生成jwt token
        String token = JwtTokenUtil.generateToken(user.getId(),user.getUsername(),user.getUserType());
        UserInfoVo userInfoVo = UserInfoVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .gender(user.getGender())
                .genderDisplayName(genderDisplayName(user.getGender()))
                .birthday(user.getBirthday())
                .userType(user.getUserType())
                .userTypeDisplayName(user.getUserTypeDisplayName())
                .status(user.getStatus())
                .statusDisplayName(user.getStatusDisplayName())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        UserLoginVo userLoginVo = UserLoginVo.builder()
                .token(token)
                .roleType(user.getUserType().toString())
                .userInfo(userInfoVo)
                .build();
        return userLoginVo;
    }

    @Override
    public UserInfoVo register(UserRegisterDTO userRegisterDTO) {
        //hutool依赖里面的JSONUtil类，用于将对象转换为JSON字符串，然后打印出来
        System.out.println(JSONUtil.parseObj(userRegisterDTO));
        //验证密码是否一致
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }
        //检查用户名是否存在
        LambdaQueryWrapper<User> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(User::getUsername, userRegisterDTO.getUsername());
        Long count = count(usernameWrapper);
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        //检查邮箱是否存在
        LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(User::getEmail, userRegisterDTO.getEmail());
        count = count(emailWrapper);
        if (count > 0) {
            throw new BusinessException("邮箱已存在");
        }
        //用户类型
        if(!UserType.isValidCode(userRegisterDTO.getUserType())){
            throw new BusinessException("无效的用户类型");
        }
        //创建用户
        //密码加密处理
        String encryptedPassword = passwordEncoder.encode(userRegisterDTO.getPassword().trim());
        //创建用户
        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .email(userRegisterDTO.getEmail())
                .password(encryptedPassword)
                .nickname(userRegisterDTO.getNickname())
                .phone(userRegisterDTO.getPhone())
                .gender(userRegisterDTO.getGender())
                .birthday(userRegisterDTO.getBirthDate())
                .userType(userRegisterDTO.getUserType())
                .status(UserStatus.NORMAL.getCode())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        //保存用户
        save(user);
        //返回用户信息
        UserInfoVo userInfoVo = UserInfoVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .gender(user.getGender())
                .genderDisplayName(genderDisplayName(user.getGender()))
                .birthday(user.getBirthday())
                .userType(user.getUserType())
                .userTypeDisplayName(user.getUserTypeDisplayName())
                .status(user.getStatus())
                .statusDisplayName(user.getStatusDisplayName())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        return userInfoVo;
    }

    //性别转换的方法
    public static String genderDisplayName(Integer gender) {
        if (gender == null) {
            return "未知";
        }
        switch(gender){
            case 0 :
                return "未知";
            case 1 :
                return "男";
            case 2 :
                return "女";
            default :
                return "未知";
        }
    }
    public UserInfoVo getUserById(Long userId){
        User user = userMapper.selectById(userId);
        if (user ==null){
            throw new BusinessException("用户不存在");
        }
        UserInfoVo userInfoVo = UserInfoVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .gender(user.getGender())
                .genderDisplayName(genderDisplayName(user.getGender()))
                .birthday(user.getBirthday())
                .userType(user.getUserType())
                .userTypeDisplayName(user.getUserTypeDisplayName())
                .status(user.getStatus())
                .statusDisplayName(user.getStatusDisplayName())
                .displayName(user.getDisplayName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        return userInfoVo;
    }
}
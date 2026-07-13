package com.xxs.aispringbooot.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.common.exception.BusinessException;
import com.xxs.aispringbooot.common.utils.JwtTokenUtil;
import com.xxs.aispringbooot.mapper.UserMapper;
import com.xxs.aispringbooot.pojo.dto.UserLoginDTO;
import com.xxs.aispringbooot.pojo.entity.User;
import com.xxs.aispringbooot.pojo.vo.UserInfoVo;
import com.xxs.aispringbooot.pojo.vo.UserLoginVo;
import com.xxs.aispringbooot.service.UserService;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
}
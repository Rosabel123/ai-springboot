package com.xxs.aispringbooot.service.serviceimpl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxs.aispringbooot.common.enums.ResultCode;
import com.xxs.aispringbooot.common.exception.BusinessException;
import com.xxs.aispringbooot.mapper.ConsultationSessionMapper;
import com.xxs.aispringbooot.mapper.UserMapper;
import com.xxs.aispringbooot.pojo.dto.ConsultationSessionCreateDTO;
import com.xxs.aispringbooot.pojo.entity.ConsultationSession;
import com.xxs.aispringbooot.pojo.entity.User;
import com.xxs.aispringbooot.service.ConsultationSessionService;
import com.xxs.aispringbooot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsultationSessionServiceImpl extends ServiceImpl<ConsultationSessionMapper, ConsultationSession> implements ConsultationSessionService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ConsultationSession createSession(Long userId, ConsultationSessionCreateDTO createDTO) {
        //验证用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        } else {
            //创建会话
            ConsultationSession session = ConsultationSession.builder()
                    .userId(userId)
                    .sessionTitle(createDTO.getSessionTitle())
                    .startedAt(LocalDateTime.now())
                    .build();
            //如果未提供标题
            if(StrUtil.isBlank(createDTO.getSessionTitle())){
                session.setSessionTitle("AI小助手-" + DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
            }
            //插入会话到数据库
            save(session);
            return session;
        }
    }
}
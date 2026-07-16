package com.xxs.aispringbooot.service.serviceimpl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxs.aispringbooot.mapper.ConsultationMessageMapper;
import com.xxs.aispringbooot.pojo.entity.ConsultationMessage;
import com.xxs.aispringbooot.service.ConsultationMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConsultationMessageServiceImpl extends ServiceImpl<ConsultationMessageMapper, ConsultationMessage> implements ConsultationMessageService {
    @Override
    public ConsultationMessage saveUserMessage(Long sessionId, String content, String emotionTag) {
        //构建用户消息实体
        ConsultationMessage userMessage = ConsultationMessage.builder()
                .sessionId(sessionId)
                .senderType(1)
                .messageType(1)
                .content(content)
                .emotionTag(emotionTag)
                .createdAt(LocalDateTime.now())
                .build();
        save(userMessage);
        return userMessage;
    }
}

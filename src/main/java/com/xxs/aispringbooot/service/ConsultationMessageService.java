package com.xxs.aispringbooot.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxs.aispringbooot.pojo.entity.ConsultationMessage;
import com.xxs.aispringbooot.pojo.entity.ConsultationSession;

public interface ConsultationMessageService extends IService<ConsultationMessage> {
    public ConsultationMessage saveUserMessage(Long sessionId,String content,String emotionTag);
}

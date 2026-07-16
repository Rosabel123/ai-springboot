package com.xxs.aispringbooot.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxs.aispringbooot.pojo.dto.ConsultationSessionCreateDTO;
import com.xxs.aispringbooot.pojo.entity.ConsultationSession;
import org.springframework.stereotype.Service;

public interface ConsultationSessionService extends IService<ConsultationSession> {
    public ConsultationSession createSession(Long userId, ConsultationSessionCreateDTO createDTO);
}

package com.xxs.aispringbooot.service.serviceimpl;

import com.xxs.aispringbooot.pojo.dto.ConsultationSessionCreateDTO;
import com.xxs.aispringbooot.pojo.entity.ConsultationMessage;
import com.xxs.aispringbooot.pojo.entity.ConsultationSession;
import com.xxs.aispringbooot.pojo.vo.StructOutPutVo;
import com.xxs.aispringbooot.service.ConsultationMessageService;
import com.xxs.aispringbooot.service.ConsultationSessionService;
import com.xxs.aispringbooot.service.PsychologicalSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PsychologicalSupportServiceImpl implements PsychologicalSupportService {
    @Autowired
    private ConsultationMessageService consultationMessageService;
    @Autowired
    private ConsultationSessionService consultationSessionService;
    @Override
    public StructOutPutVo startSession(Long userId, ConsultationSessionCreateDTO createDTO) {
        //创建数据库会话记录
        ConsultationSession consultationSession = consultationSessionService.createSession(userId, createDTO);
        //将初始用户消息保存到message表
        ConsultationMessage userMessage = consultationMessageService.saveUserMessage(consultationSession.getId(), createDTO.getInitialMessage(), null);


        //创建会话信息
        String sessionId = "session_" + consultationSession.getId();


        StructOutPutVo structOutPutVo = StructOutPutVo.builder()
                .sessionId(sessionId)
                .userHash(userId)
                .initialMessage(createDTO.getInitialMessage())
                .startTime(System.currentTimeMillis())
                .expiryTime(System.currentTimeMillis() + 86400000L)
                .status("ACTIVE")
                .build();
        return structOutPutVo;
    }
}

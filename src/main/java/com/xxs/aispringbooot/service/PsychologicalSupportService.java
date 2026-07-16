package com.xxs.aispringbooot.service;

import com.xxs.aispringbooot.pojo.dto.ConsultationSessionCreateDTO;
import com.xxs.aispringbooot.pojo.vo.StructOutPutVo;

public interface PsychologicalSupportService {
    public StructOutPutVo startSession(Long userId, ConsultationSessionCreateDTO createDTO);
}

package com.xxs.aispringbooot.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.common.utils.JwtTokenUtil;
import com.xxs.aispringbooot.pojo.dto.ConsultationSessionCreateDTO;
import com.xxs.aispringbooot.pojo.vo.StructOutPutVo;
import com.xxs.aispringbooot.service.PsychologicalSupportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/psychological-chat")
public class PsychologicalChatController {
    @Autowired
    private PsychologicalSupportService psychologicalSupportService;
    @PostMapping("/session/start")
    public Result<StructOutPutVo> startSession(@Valid @RequestBody ConsultationSessionCreateDTO createDTO){
        //获取当前用户
        String token =  JwtTokenUtil.getCurrentToken();
        DecodedJWT decodedJWT = JwtTokenUtil.verifyToken(token);
        Long userId = decodedJWT.getClaim("userId").asLong();
        StructOutPutVo structOutPutVo = psychologicalSupportService.startSession(userId,createDTO);
        return Result.success(structOutPutVo);
    }
}

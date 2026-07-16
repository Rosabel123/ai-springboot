package com.xxs.aispringbooot.pojo.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import java.sql.Time;
@Builder
@Data
public class StructOutPutVo {
    private String sessionId;
    private Long userHash;
    private String initialMessage;
    private Long startTime;
    private Long expiryTime;
    private String status;
}
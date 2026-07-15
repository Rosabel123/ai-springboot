package com.xxs.aispringbooot.common.utils;

import cn.hutool.json.JSONUtil;
import com.xxs.aispringbooot.common.Result;
import com.xxs.aispringbooot.common.enums.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {
    //过滤器中的异常响应
    public static void writeError(HttpServletResponse response, ResultCode resultCode) {
        //根据不同的结果码，设置不同的响应状态码
        int statusCode = switch (resultCode) {
            case UNAUTHORIZED, ACCESS_UNAUTHORIZED, TOKEN_INVALID, TOKEN_EXPIRED, TOKEN_BLOCKED ->
                    HttpStatus.UNAUTHORIZED.value();
            case TOKEN_ACCESS_FORBIDDEN -> HttpStatus.FORBIDDEN.value();
            default -> HttpStatus.BAD_REQUEST.value();
        };
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = response.getWriter()){
             String jsonResponse = JSONUtil.toJsonStr(Result.error(resultCode.getCode(),resultCode.getMsg(),null));
             writer.print(jsonResponse);
             writer.flush();//确保将响应写入到客户端响应体中
        }catch (IOException e){
            System.out.println("响应异常"+e.getMessage());
        }
    }
}

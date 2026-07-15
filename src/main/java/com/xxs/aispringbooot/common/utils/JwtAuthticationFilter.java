package com.xxs.aispringbooot.common.utils;

import com.xxs.aispringbooot.common.enums.ResultCode;
import com.xxs.aispringbooot.config.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthticationFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        //如果是公共路径，直接放行
        return SecurityConfig.isPublicPath(requestUri);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        //获取请求的URI和方法
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        //获取token
        String token = JwtTokenUtil.extractTokenFromRequest(request);
        if(StringUtils.hasText(token)){

        }else {
            //token为空，清理spring security上下文
            clearSecurityContext();
            ResponseUtil.writeError(response, ResultCode.ACCESS_UNAUTHORIZED);
            return;
        }
    }
    //清理spring security上下文
    private void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}

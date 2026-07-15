package com.xxs.aispringbooot.common.utils;

import cn.hutool.json.JSONUtil;
import com.xxs.aispringbooot.common.enums.ResultCode;
import com.xxs.aispringbooot.common.enums.UserStatus;
import com.xxs.aispringbooot.config.SecurityConfig;
import com.xxs.aispringbooot.pojo.vo.UserInfoVo;
import com.xxs.aispringbooot.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
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
            JwtTokenUtil.TokenVerificationResult validationResult = JwtTokenUtil.validateToken(token);
            if (validationResult !=null && validationResult.isValid()){
                    //3.查询用户信息验证用户的状态
                UserInfoVo userInfoVo = userService.getUserById(validationResult.getUserId());
                System.out.println(JSONUtil.parseObj(userInfoVo));
                if (userInfoVo != null && UserStatus.NORMAL.getCode().equals(userInfoVo.getStatus())) {
                    // 4. 创建Spring Security认证对象
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + validationResult.getRoleType())
                    );

                // 创建UsernamePasswordAuthenticationToken对象
                    UsernamePasswordAuthenticationToken authcation = new UsernamePasswordAuthenticationToken(
                            validationResult.getUsername(), // 用户名作为主体
                            null,
                            authorities
                    );
                    //设置认证信息到Spring Securtiry上下文
                    SecurityContextHolder.getContext().setAuthentication(authcation);
                    request.setAttribute("jwtToken",token);
                } else {
                    clearSecurityContext();
                    ResponseUtil.writeError(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
                    return;
                }
            }else{
                clearSecurityContext();
                ResponseUtil.writeError(response,ResultCode.TOKEN_INVALID);
                return;
            }
        }else {
            //token为空，清理spring security上下文
            clearSecurityContext();
            ResponseUtil.writeError(response, ResultCode.ACCESS_UNAUTHORIZED);
            return;
        }
        //继续过滤器链
        filterChain.doFilter(request,response);
    }
    //清理spring security上下文
    private void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}

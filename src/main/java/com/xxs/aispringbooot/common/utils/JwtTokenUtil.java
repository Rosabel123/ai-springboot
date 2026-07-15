package com.xxs.aispringbooot.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.xxs.aispringbooot.config.JwtConfig;
import com.xxs.aispringbooot.controller.UserController;
import com.xxs.aispringbooot.service.serviceimpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.swing.*;
import java.util.Date;
@Component
public class JwtTokenUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    //生成token的方法
    public static String generateToken(Long userId,String username,Integer userType) {
        try {
        //生成token的逻辑
        //获取jwt的配置信息
        JwtConfig jwtConfig = getJwtConfig();
        //生成签名的算法
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        //生成过期时间
        Date expiration = new Date(System.currentTimeMillis()+ jwtConfig.getExpiration());
        //生成token
        String token = jwtConfig.getTokenPrefix() + JWT.create()
                .withClaim("userId",userId)
                .withClaim("username",username)
                .withClaim("roleType",userType)
                .withExpiresAt(expiration)//设置过期时间
                .withIssuedAt(new Date())//设置签发时间
                .withIssuer(jwtConfig.getIssuer())//设置签发者
                .sign(algorithm);//设置签名算法
        return token;
        } catch (Exception e) {
            throw new RuntimeException("生成token失败",e);
        }
    }
    //提取token的方法
    public static String extractTokenFromRequest(HttpServletRequest request) {
        if(request == null ){
            return null;
        }
        String tokenHeader = request.getHeader("token");
        if(StringUtils.hasText(tokenHeader)){
            // 移除 Token 前缀（如 "Bearer "）
            JwtConfig jwtConfig = getJwtConfig();
            String tokenPrefix = jwtConfig.getTokenPrefix();
            if (tokenPrefix != null && !tokenPrefix.isEmpty() && tokenHeader.startsWith(tokenPrefix)) {
                return tokenHeader.substring(tokenPrefix.length());
            }
            return tokenHeader;
        }
        return null;
    }
    //用于在静态工具类中获取Spring容器管理的Bean
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JwtTokenUtil.applicationContext = applicationContext;
    }
    //验证token
    public static TokenVerificationResult validateToken(String token){
        DecodedJWT jwt = verifyToken(token);
        Long userId = jwt.getClaim("userId").asLong();
        String username = jwt.getClaim("username").asString();
        //角色类型
        Integer roleType =null;
        try {
            roleType = jwt.getClaim("roleType").asInt();
        } catch (Exception e) {
            String roleTypeStr = jwt.getClaim("roleType").asString();
            if (StringUtils.hasText(roleTypeStr)) {
                roleType = Integer.valueOf(roleTypeStr);
            }
        }
        if (userId != null && StringUtils.hasText(username) && roleType!=null){
            return new TokenVerificationResult(userId,username,roleType,true);
        }
        return null;
    }
    //获取当前token
    public static String getCurrentToken(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String token = (String) request.getAttribute("jwtToken");
            if (token != null) {
                return token;
            }

            // 备用方案： 从请求头直接获取
            String headerToken = extractTokenFromRequest(request);
            return headerToken;
        }
        return null;
    }
    //验证token有效性
    public static DecodedJWT verifyToken(String token){
        if (!StringUtils.hasText(token)){
            throw new JWTVerificationException("token不能为空");
        }
        //token解码
        JwtConfig jwtConfig = getJwtConfig();
        Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(jwtConfig.getIssuer())
                .build();
        return verifier.verify(token);
    }
    //验证结果封装类
    @Getter
    public static class TokenVerificationResult{
        private final Long userId;
        private final String username;
        private final Integer roleType;
        private final boolean valid;
        public TokenVerificationResult(Long userId,String username,Integer roleType,boolean valid){
            this.userId = userId;
            this.username = username;
            this.roleType = roleType;
            this.valid = valid;
        }
    }
    private static JwtConfig getJwtConfig() {
        return applicationContext.getBean(JwtConfig.class);
    }
//    Spring 包扫描扫到 @Component 标记的 JwtTokenUtil
//    实例化 new JwtTokenUtil()，创建 Bean 对象
//    Spring 检测到该类实现了 ApplicationContextAware 接口
//    自动回调重写的 setApplicationContext(ApplicationContext ctx)
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        // 把框架传入的全局容器，赋值给静态变量
//        JwtTokenUtil.applicationContext = applicationContext;
//    }
//    执行完毕后，静态变量 applicationContext 不再是 null，里面持有整个 Spring 容器。
//    重点：这一步是容器初始化阶段自动执行，你不用手动调用。
//    二、接口请求调用阶段（登录时触发）
//    前端调用登录接口 → UserController → UserServiceImpl.login ()
//    Service 执行静态方法：JwtTokenUtil.generateToken(userId, username, userType)
//    方法内执行 JwtConfig jwtConfig = getJwtConfig();
//    进入私有静态方法 getJwtConfig()
//    private static JwtConfig getJwtConfig() {
//        // 使用已经赋值好的静态applicationContext，从容器拿JwtConfig Bean
//        return applicationContext.getBean(JwtConfig.class);
//    }
//applicationContext.getBean(JwtConfig.class) 去容器缓存中取出提前创建好的 JwtConfig 对象
//    拿到配置后，读取 secret、expiration、tokenPrefix 等 yml 配置，生成 JWT 令牌
}

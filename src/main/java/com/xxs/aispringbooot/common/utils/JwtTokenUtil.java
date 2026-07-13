package com.xxs.aispringbooot.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xxs.aispringbooot.config.JwtConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Date;

public class JwtTokenUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    //生成token的方法
    public static String generateToken(String userId,String username,Integer roleType) {
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
                .withClaim("roleType",roleType)
                .withExpiresAt(expiration)//设置过期时间
                .withIssuedAt(new Date())//设置签发时间
                .withIssuer(jwtConfig.getIssuer())//设置签发者
                .sign(algorithm);//设置签名算法
        return token;
        } catch (Exception e) {
            throw new RuntimeException("生成token失败",e);
        }
    }
    //用于在静态工具类中获取Spring容器管理的Bean
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JwtTokenUtil.applicationContext = applicationContext;
    }
    private static JwtConfig getJwtConfig() {
        return applicationContext.getBean(JwtConfig.class);
    }
}

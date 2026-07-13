package com.xxs.aispringbooot.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xxs.aispringbooot.config.JwtConfig;
import com.xxs.aispringbooot.controller.UserController;
import com.xxs.aispringbooot.service.serviceimpl.UserServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
                .withClaim("userType",userType)
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

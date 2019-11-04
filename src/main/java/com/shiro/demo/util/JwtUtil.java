package com.shiro.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "jwt")
@Slf4j
@Data
public class JwtUtil {

    /**
     * 密钥
     */
    private String secret;
    /**
     * 有效期限
     */
    private int expire;
    /**
     * 存储 token
     */
    private String header;

    /**
     * 生成token
     * @param username
     * @return
     */
    public String generateToken(String username) {
        //头部信息
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        // expire time
        Calendar nowTime = Calendar.getInstance();
        //有10天有效期
        nowTime.add(Calendar.DATE, getExpire());
        Date expiresDate = nowTime.getTime();
        //载荷信息
        /*Claims claims = Jwts.claims();
        claims.put("username",username);*/
        String token = Jwts.builder()
                .setHeader(headers)//头部
                .setSubject(username)//载荷，重要信息
                .setExpiration(expiresDate)//过期时间，属于载荷
                .setIssuedAt(new Date())//签发时间，属于载荷
                .signWith(SignatureAlgorithm.HS256, getSecret())//签发的私密和算法
                .compact();
        return token;
    }
    // 从token中获取用户名
    public  String getUsername(String token){
        return getTokenBody(token).getSubject();
    }

    // 是否已过期
    public  boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private  Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    public  Map getTokenHead(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getHeader();
    }
    public  String getTokenSi(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getSignature();
    }
}

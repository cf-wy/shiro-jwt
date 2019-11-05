package com.shiro.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {

    /**
     * 密钥
     */
    private static final String secret="fadada";
    /**
     * 有效期限
     */
    private static final int expire=1;//有效期为1小时

    /**
     * 生成token
     * @param username
     * @return
     */
    public static String generateToken(String username) {
        //头部信息
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        // expire time
        Calendar nowTime = Calendar.getInstance();
        //有1小时有效期
        nowTime.add(Calendar.HOUR_OF_DAY,expire);
        Date expiresDate = nowTime.getTime();
        //载荷信息
        /*Claims claims = Jwts.claims();
        claims.put("username",username);*/
        String token = Jwts.builder()
                .setHeader(headers)//头部
                .setSubject(username)//载荷，重要信息
                .setExpiration(expiresDate)//过期时间，属于载荷
                .setIssuedAt(new Date())//签发时间，属于载荷
                .signWith(SignatureAlgorithm.HS256, secret)//签发的私密和算法
                .compact();
        return token;
    }
    // 从token中获取用户名
    public  static String getUsername(String token){
        return getTokenBody(token).getSubject();
    }
    public  static Date getIssuedAt(String token){
        return getTokenBody(token).getIssuedAt();
    }

    // 是否已过期
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private  static Claims getTokenBody(String token){
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

    public static boolean verifyToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parse(token);
            return true;
        }catch (Exception e){
            log.error("无效的token",e);
        }
        return false;
    }
}

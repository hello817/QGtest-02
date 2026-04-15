package com.qgtest.diary.utils;

import com.qgtest.diary.common.BizException;
import com.qgtest.diary.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtUtils {
    @Autowired
    JwtConfig jwtConfig;

    //转换key格式
    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    //生成token
    public String generateToken(Long userId, String account){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        claims.put("account",account);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtConfig.getExpiration()))
                .signWith(getSecretKey())
                .compact();
    }
    //解析Token
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //验证Token是否有效
    public boolean validateToken(String token){
        try{
            parseToken(token);
            return true;
        } catch (BizException e) {
            return false;
        }
    }
    //获取id
    public Long getIdByToken(String token){
        Claims claims = parseToken(token);
        Integer userIdInt = claims.get("userId", Integer.class);
        return userIdInt != null ? userIdInt.longValue() : null;
    }
    public String getAccountByToken(String token){
        Claims claims = parseToken(token);
        return claims.get("account",String.class);
    }
}

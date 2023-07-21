package com.lwj.verify.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lwj.verify.property.VerifyProperty;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public  class JWTUtil {
    private final VerifyProperty verifyProperty;

    public JWTUtil(VerifyProperty verifyProperty) {
        this.verifyProperty = verifyProperty;
    }

    /**
     * 生成token
     *
     * @param map //传入payload
     * @return 返回token
     */
    public String getToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 7);
        builder.withExpiresAt(DateUtil.offset(new Date(), verifyProperty.getExpire().getField(), verifyProperty.getExpire().getOffset()));
        return builder.sign(Algorithm.HMAC256(verifyProperty.getSignature()));
    }

    /**
     * 生成token
     *
     * @param map //传入payload
     * @param day token有效天数
     * @return 返回token
     */
    public String getToken(Map<String, String> map, Integer day) {
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 7);
        builder.withExpiresAt(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * day)));
        return builder.sign(Algorithm.HMAC256(verifyProperty.getSignature()));
    }

    /**
     * 验证token
     *
     * @param token
     */
    public void verify(String token) {
        com.auth0.jwt.JWT.require(Algorithm.HMAC256(verifyProperty.getSignature())).build().verify(token);
    }

    /**
     * 获取token中payload
     *
     * @param token
     * @return
     */
    public DecodedJWT getToken(String token) {
        return com.auth0.jwt.JWT.require(Algorithm.HMAC256(verifyProperty.getSignature())).build().verify(token);
    }

}

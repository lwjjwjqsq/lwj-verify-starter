package com.lwj.verify.inter;

import com.lwj.verify.property.VerifyProperty;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@AllArgsConstructor
public class GetTokenInterfaceDefault implements GetTokenInterface {
    private final VerifyProperty verifyProperty;
    @Override
    public String getToken(HttpServletRequest request) {
        // 获取请求头
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            // 判断是否是token
            if (verifyProperty.getTokenKey().equals(name)) {
                return value;
            }
        }
        return null;
    }
}

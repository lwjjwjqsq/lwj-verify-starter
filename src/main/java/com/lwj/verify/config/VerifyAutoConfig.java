package com.lwj.verify.config;


import com.lwj.verify.aop.VerifyAOP;
import com.lwj.verify.inter.GetTokenInterfaceDefault;
import com.lwj.verify.inter.NotLoginInterfaceDefault;
import com.lwj.verify.property.VerifyProperty;
import com.lwj.verify.util.JWTUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(VerifyProperty.class)
public class VerifyAutoConfig {

    @Bean
    public JWTUtil jwtUtil(VerifyProperty verifyProperty) {
        return new JWTUtil(verifyProperty);
    }

    @Bean
    public GetTokenInterfaceDefault getTokenInterface(VerifyProperty v) {
        return new GetTokenInterfaceDefault(v);
    }

    @Bean
    public NotLoginInterfaceDefault notLoginInterfaceDefault(){
        return new NotLoginInterfaceDefault();
    }

    @Bean
    public VerifyAOP verifyAOP(JWTUtil jwtUtil) {
        return new VerifyAOP(jwtUtil);
    }
}

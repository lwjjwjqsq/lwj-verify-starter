package com.lwj.verify.property;

import cn.hutool.core.date.DateField;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 校验配置信息
 */

@Data
@ConfigurationProperties(prefix = "spring.verify")
public class VerifyProperty {
    /**
     * 密钥
     */
    private String signature;

    /**
     * 过期时间
     */
    private ExpireTime expire;

    /**
     * token key请求头
     */
    private String tokenKey = "Authority";



    @Data
    public static class ExpireTime {
        private DateField field;
        private Integer offset;
    }
}

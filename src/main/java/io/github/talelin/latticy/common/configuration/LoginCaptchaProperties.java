package io.github.talelin.latticy.common.configuration;

import io.github.talelin.latticy.common.util.CaptchaUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Gadfly
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login-captcha")
public class LoginCaptchaProperties {
    /**
     * aes 密钥
     */
    private String secret = CaptchaUtil.getRandomString(32);
    /**
     * aes 偏移量
     */
    private String iv = CaptchaUtil.getRandomString(16);
    /**
     * 启用验证码
     */
    private Boolean enabled = Boolean.FALSE;
}

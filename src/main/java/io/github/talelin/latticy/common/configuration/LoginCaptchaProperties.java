package io.github.talelin.latticy.common.configuration;

import io.github.talelin.latticy.common.util.CaptchaUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Gadfly
 */
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login-captcha")
public class LoginCaptchaProperties {
    /**
     * aes 密钥
     */
    private String secret;
    /**
     * aes 偏移量
     */
    private String iv;
    /**
     * 启用验证码
     */
    private Boolean enabled = Boolean.FALSE;

    public void setSecret(String secret) {
        this.secret = CaptchaUtil.getRandomString(32);
        if (StringUtils.hasText(secret)) {
            byte[] bytes = secret.getBytes();
            if (bytes.length == 16 || bytes.length == 24 || bytes.length == 32) {
                this.secret = secret;
            } else {
                log.warn("AES密钥必须为128/192/256bit，输入的密钥为{}bit，已启用随机密钥{}", bytes.length * 8, this.secret);
            }
        }
    }

    public void setIv(String iv) {
        this.iv = CaptchaUtil.getRandomString(16);
        if (StringUtils.hasText(iv)) {
            byte[] bytes = iv.getBytes();
            if (bytes.length == 16) {
                this.iv = iv;
            } else {
                log.warn("AES初始向量必须为128bit，输入的密钥为{}bit，已启用随机向量{}", bytes.length * 8, this.iv);
            }
        }
    }

}

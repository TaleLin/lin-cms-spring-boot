package io.github.talelin.latticy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gadfly
 * 登录验证码视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCaptchaVO {
    /**
     * 加密后的验证码
     */
    private String tag;
    /**
     * 验证码图片地址，可使用base64
     */
    private String image;
}

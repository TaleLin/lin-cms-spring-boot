package io.github.talelin.latticy.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gadfly
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

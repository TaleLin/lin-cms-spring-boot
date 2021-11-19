package io.github.talelin.latticy.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gadfly
 * @since 2021-11-19 15:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCaptchaBO {
    private String captcha;
    private Long expired;
}

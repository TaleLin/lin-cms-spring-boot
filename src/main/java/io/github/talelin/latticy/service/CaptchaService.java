package io.github.talelin.latticy.service;

import io.github.talelin.latticy.vo.LoginCaptchaVO;

/**
 * 验证码服务
 *
 * @author Gadfly
 */
public interface CaptchaService {
    /**
     * 生成验证码
     *
     * @return 验证码VO
     */
    LoginCaptchaVO generateCaptcha();

    /**
     * 校验验证码
     */
    boolean verify(String captcha, String tag);

    /**
     * 检查验证码标识是否有效
     *
     * @param tag 验证码标识
     * @return 是否有效
     */
    boolean checkCaptchaTagValid(String tag);

    /**
     * 验证码加入黑名单
     *
     * @param tag 验证码标识
     */
    void invalidateCaptchaTag(String tag);
}

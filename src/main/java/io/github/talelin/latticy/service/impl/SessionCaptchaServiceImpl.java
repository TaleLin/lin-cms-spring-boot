package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.vo.LoginCaptchaVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author Gadfly
 */
@Slf4j
@Service
public class SessionCaptchaServiceImpl extends LocalCaptchaServiceImpl {
    @Override
    public boolean verify(String captcha, String tag) {
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            String code = (String) session.getAttribute("captcha");
            Long invalidTime = (Long) session.getAttribute("captchaInvalidTime");
            session.removeAttribute("captcha");
            session.removeAttribute("captchaInvalidTime");
            return invalidTime != null && invalidTime >= System.currentTimeMillis() && captcha.equalsIgnoreCase(code);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkCaptchaTagValid(String tag) {
        return true;
    }

    @Override
    public void invalidateCaptchaTag(String tag) {
    }

    @Override
    @SneakyThrows
    public LoginCaptchaVO generateCaptcha() {
        String code = this.getRandomString(RANDOM_STR_NUM);
        String base64String = this.getRandomCodeBase64(code);
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.setAttribute("captcha", code);
        session.setAttribute("captchaInvalidTime", System.currentTimeMillis() + 5 * 60 * 1000);
        return new LoginCaptchaVO("session", "data:image/png;base64," + base64String);
    }
}

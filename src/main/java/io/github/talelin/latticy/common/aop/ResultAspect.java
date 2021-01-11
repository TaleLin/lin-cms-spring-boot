package io.github.talelin.latticy.common.aop;

import io.github.talelin.latticy.common.configuration.CodeMessageConfiguration;
import io.github.talelin.latticy.vo.UnifyResponseVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 处理返回结果为 UnifyResponseVO 的 Controller
 * message 默认为 null，在此处通过 code 设置为对应消息
 *
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 * @author Juzi@TaleLin
 */
@Aspect
@Component
public class ResultAspect {
    @AfterReturning(returning = "result", pointcut = "execution(public * io.github.talelin.latticy.controller..*.*(..))")
    public void doAfterReturning(UnifyResponseVO<String> result) {
        int code = result.getCode();
        String messageOfVO = result.getMessage();
        // code-message.properties 中配置的 message
        String messageOfConfiguration = CodeMessageConfiguration.getMessage(code);

        // 如果 code-message.properties 中指定了相应的 message 并且 UnifyResponseVO 的 message 为null
        // 则使用 messageOfConfiguration 替换 messageOfVO
        if (StringUtils.hasText(messageOfConfiguration) && !StringUtils.hasText(messageOfVO)) {
            result.setMessage(messageOfConfiguration);
        }
    }
}

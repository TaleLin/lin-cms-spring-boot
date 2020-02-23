package io.github.talelin.merak.extensions.message;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageInfoCollector implements BeanPostProcessor {

    private Map<String, Message> messageMap = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : methods) {
            Message message = AnnotationUtils.findAnnotation(method, Message.class);
            if (message != null) {
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String identity = className + "#" + methodName;
                messageMap.put(identity, message);
            }
        }
        return bean;
    }

    public Map<String, Message> getMessageMap() {
        return messageMap;
    }
}

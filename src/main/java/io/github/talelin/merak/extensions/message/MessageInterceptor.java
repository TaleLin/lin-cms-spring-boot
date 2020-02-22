package io.github.talelin.merak.extensions.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Slf4j
public class MessageInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private WsHandler wsHandler;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Message message = method.getAnnotation(Message.class);
            if (message != null) {
                this.handle(message);
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }

    private void handle(Message message) {
        EventMessage eventMessage = new EventMessage(message.event(), message.value());
        TextMessage textMessage = new TextMessage(eventMessage.toJson());
        try {
            wsHandler.broadCast(message.event(), textMessage);
        } catch (IOException e) {
            log.info("", e);
        }
    }
}

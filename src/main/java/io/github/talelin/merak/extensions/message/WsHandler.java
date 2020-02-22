package io.github.talelin.merak.extensions.message;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface WsHandler {

    void handleOpen(WebSocketSession session);

    void handleClose(WebSocketSession session);

    /**
     * 处理消息
     *
     * @param session 会话
     * @param message 接收的消息
     */
    void handleMessage(WebSocketSession session, String message);

    /**
     * 发送消息
     *
     * @param session 当前会话
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(WebSocketSession session, String message) throws IOException;

    void broadCast(String message) throws IOException;

    void broadCast(TextMessage message) throws IOException;

    void broadCast(String event, TextMessage message) throws IOException;

    void handleError(WebSocketSession session, Throwable error);
}

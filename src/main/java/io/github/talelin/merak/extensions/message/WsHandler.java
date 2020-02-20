package io.github.talelin.merak.extensions.message;

import javax.websocket.Session;
import java.io.IOException;

public interface WsHandler {

    void handleOpen(Session session);

    void handleClose(Session session);

    /**
     * 处理消息
     *
     * @param session 会话
     * @param message 接收的消息
     */
    void handleMessage(Session session, String message);

    /**
     * 发送消息
     *
     * @param session 当前会话
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(Session session, String message) throws IOException;

    void broadCast(String message) throws IOException;

    void handleError(Session session, Throwable error);
}

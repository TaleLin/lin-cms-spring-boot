package io.github.talelin.latticy.module.message;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public interface WsHandler {

    /**
     * 会话开始回调
     *
     * @param session 会话
     */
    void handleOpen(WebSocketSession session);

    /**
     * 会话结束回调
     *
     * @param session 会话
     */
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

    /**
     * 发送消息
     *
     * @param userId  用户id
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(Integer userId, TextMessage message) throws IOException;

    /**
     * 发送消息
     *
     * @param userId  用户id
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(Integer userId, String message) throws IOException;

    /**
     * 发送消息
     *
     * @param session 当前会话
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(WebSocketSession session, TextMessage message) throws IOException;

    /**
     * 广播
     *
     * @param message 字符串消息
     * @throws IOException 异常
     */
    void broadCast(String message) throws IOException;

    /**
     * 对某个分组广播
     *
     * @param groupId 分组id
     * @param message 消息
     * @throws IOException 异常
     */
    void broadCastToGroup(Integer groupId, String message) throws IOException;

    /**
     * 广播
     *
     * @param message 文本消息
     * @throws IOException 异常
     */
    void broadCast(TextMessage message) throws IOException;

    /**
     * 对某个分组广播
     *
     * @param groupId 分组id
     * @param message 文本消息
     * @throws IOException 异常
     */
    void broadCastToGroup(Integer groupId, TextMessage message) throws IOException;

    /**
     * 处理会话异常
     *
     * @param session 会话
     * @param error   异常
     */
    void handleError(WebSocketSession session, Throwable error);

    /**
     * 获得所有的 websocket 会话
     *
     * @return 所有 websocket 会话
     */
    Set<WebSocketSession> getSessions();

    /**
     * 得到当前连接数
     *
     * @return 连接数
     */
    int getConnectionCount();
}

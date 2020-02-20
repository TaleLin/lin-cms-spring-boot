package io.github.talelin.merak.extensions.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Controller
@ServerEndpoint("/ws/message")
@Slf4j
public class WebsocketServerEndpoint {

    private static WsHandler wsHandler;

    @Autowired
    public void setWsHandler(WsHandler wsHandler) {
        WebsocketServerEndpoint.wsHandler = wsHandler;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        wsHandler.handleOpen(session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        wsHandler.handleClose(session);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        wsHandler.handleMessage(session, message);
    }

    /**
     * 出现错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        wsHandler.handleError(session, error);
    }

}

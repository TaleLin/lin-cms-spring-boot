package io.github.talelin.latticy.module.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

/**
 * @author pedro@TaleLin
 */
public class MessageWebSocketHandler implements WebSocketHandler {

    @Autowired
    private WsHandler wsHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        wsHandler.handleOpen(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) webSocketMessage;
            wsHandler.handleMessage(session, textMessage.getPayload());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        wsHandler.handleError(session, throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        wsHandler.handleClose(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

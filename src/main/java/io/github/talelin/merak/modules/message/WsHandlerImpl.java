package io.github.talelin.merak.modules.message;

import io.github.talelin.merak.model.UserDO;
import io.github.talelin.merak.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WsHandlerImpl implements WsHandler {

    private final AtomicInteger connectionCount = new AtomicInteger(0);

    private CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private GroupService groupService;

    @Override
    public void handleOpen(WebSocketSession session) {
        sessions.add(session);
        int cnt = connectionCount.incrementAndGet();
        log.info("a new connection opened，current online count：{}", cnt);
    }

    @Override
    public void handleClose(WebSocketSession session) {
        sessions.remove(session);
        int cnt = connectionCount.decrementAndGet();
        log.info("a connection closed，current online count：{}", cnt);
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        // 只处理前端传来的文本消息，并且直接丢弃了客户端传来的消息
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen())
                continue;
            sendMessage(session, message);
        }
    }

    @Override
    public void broadCast(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen())
                continue;
            session.sendMessage(message);
        }
    }

    @Override
    public void broadCastToGroup(Long groupId, String message) throws IOException {
        this.broadCastToGroup(groupId, new TextMessage(message));
    }

    @Override
    public void broadCastToGroup(Long groupId, TextMessage message) throws IOException {
        List<Long> userIds = groupService.getGroupUserIds(groupId);
        for (WebSocketSession session : sessions) {
            if (!session.isOpen())
                continue;
            Map<String, Object> attributes = session.getAttributes();
            if (!attributes.containsKey("user"))
                continue;
            UserDO user = (UserDO) attributes.get("user");
            boolean matched = userIds.stream().anyMatch(id -> id.equals(user.getId()));
            if (!matched)
                continue;
            session.sendMessage(message);
        }
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id： {}", error.getMessage(), session.getId());
        log.error("", error);
    }

    public CopyOnWriteArraySet<WebSocketSession> getSessions() {
        return sessions;
    }

    public int getConnectionCount() {
        return connectionCount.get();
    }
}

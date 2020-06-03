package io.github.talelin.latticy.module.message;

import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
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
    public void sendMessage(Integer userId, TextMessage message) throws IOException {
        Optional<WebSocketSession> userSession = sessions.stream().filter(session -> {
            if (!session.isOpen()) {
                return false;
            }
            Map<String, Object> attributes = session.getAttributes();
            if (!attributes.containsKey(MessageConstant.USER_KEY)) {
                return false;
            }
            UserDO user = (UserDO) attributes.get(MessageConstant.USER_KEY);
            return user.getId().equals(userId);
        }).findFirst();
        if (userSession.isPresent()) {
            userSession.get().sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Integer userId, String message) throws IOException {
        sendMessage(userId, new TextMessage(message));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                continue;
            }
            sendMessage(session, message);
        }
    }

    @Override
    public void broadCast(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                continue;
            }
            session.sendMessage(message);
        }
    }

    @Override
    public void broadCastToGroup(Integer groupId, String message) throws IOException {
        this.broadCastToGroup(groupId, new TextMessage(message));
    }

    @Override
    public void broadCastToGroup(Integer groupId, TextMessage message) throws IOException {
        List<Integer> userIds = groupService.getGroupUserIds(groupId);
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                continue;
            }
            Map<String, Object> attributes = session.getAttributes();
            if (!attributes.containsKey(MessageConstant.USER_KEY)) {
                continue;
            }
            UserDO user = (UserDO) attributes.get(MessageConstant.USER_KEY);
            boolean matched = userIds.stream().anyMatch(id -> id.equals(user.getId()));
            if (!matched) {
                continue;
            }
            session.sendMessage(message);
        }
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id： {}", error.getMessage(), session.getId());
        log.error("", error);
    }

    @Override
    public CopyOnWriteArraySet<WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public int getConnectionCount() {
        return connectionCount.get();
    }
}

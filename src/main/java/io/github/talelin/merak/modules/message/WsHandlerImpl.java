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

    private static final AtomicInteger connectionCount = new AtomicInteger(0);

    private static CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private GroupService groupService;

    @Override
    public void handleOpen(WebSocketSession session) {
        sessions.add(session);
        int cnt = connectionCount.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
    }

    @Override
    public void handleClose(WebSocketSession session) {
        sessions.remove(session);
        int cnt = connectionCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        log.info("来自客户端{}-{}的消息：{}", session.getId(),
                session.getAttributes().get("username"),
                message);
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
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
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        log.error("", error);
    }
}

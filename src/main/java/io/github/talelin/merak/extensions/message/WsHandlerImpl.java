package io.github.talelin.merak.extensions.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class WsHandlerImpl implements WsHandler {

    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<>();

    @Override
    public void handleOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
    }

    @Override
    public void handleClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    @Override
    public void handleMessage(Session session, String message) {
        log.info("来自客户端{}的消息：{}", session.getId(), message);
    }

    @Override
    public void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        for (Session session : SessionSet) {
            if (session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }

    @Override
    public void handleError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        log.error("", error);
    }
}

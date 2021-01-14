package ru.mherarsh.socket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class WebSocketSessionStorage {
    private final ConcurrentMap<String, WebSocketSession> sessionStorage = new ConcurrentHashMap<>();

    public void addSession(WebSocketSession session) {
        sessionStorage.putIfAbsent(session.getId(), session);
    }

    public void closeSessions(String sessionId) {
        var session = sessionStorage.get(sessionId);
        if (session != null) {
            try {
                session.close(CloseStatus.POLICY_VIOLATION);
            } catch (IOException ignored) {
            }
        }
    }
}

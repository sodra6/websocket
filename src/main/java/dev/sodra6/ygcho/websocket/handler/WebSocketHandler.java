package dev.sodra6.ygcho.websocket.handler;

import dev.sodra6.ygcho.websocket.service.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final SocketService socketService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String id = session.getId();
        sessions.put(id, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String id = session.getId();
        socketService.close(session);
        sessions.remove(id);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(sessions.get(session.getId()) != null) {
            sessions.get(session.getId()).sendMessage(new TextMessage(message.getPayload()));
            socketService.recvHandle(session, message.getPayload());
        }
    }

    public void notifyDataChanged(Map<String, Object> updatedData) {
        Iterator<String> keyList = updatedData.keySet().iterator();
        try {
            while (keyList.hasNext()) {
                String key = keyList.next();
                if (sessions.get(key).isOpen()) {
                    String jsonString = updatedData.get(key).toString();
                    sessions.get(key).sendMessage(new TextMessage(jsonString));
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
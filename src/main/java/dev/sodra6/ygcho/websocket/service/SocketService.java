package dev.sodra6.ygcho.websocket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketService {

    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    public void recvHandle(WebSocketSession session, String command) {
        //map.get(session);
    }

    public void close(WebSocketSession session) throws IOException {
        session.close();
    }
}

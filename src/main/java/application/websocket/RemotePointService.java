package application.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RemotePointService {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(RemotePointService.class.getSimpleName());

    @NotNull
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    @NotNull
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registerUser(@NotNull Long userId,
                             @NotNull WebSocketSession webSocketSession) {
        sessions.put(userId, webSocketSession);
    }

    public boolean isConnected(@NotNull Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(long userId)
    {
        sessions.remove(userId);
    }

    public void cutDownConnection(long userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public void sendMessageToUser(long userId, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("No game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("Session is closed or not exists");
        }
        final WebSocketMessage<String> webSocketMessage;
        try {
            webSocketMessage = new TextMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            LOGGER.error("Can't write message to JSON. type: " + message.getType() + ", data: " + message.getData());
            return;
        }
        try {
            webSocketSession.sendMessage(webSocketMessage);
        } catch (IOException e) {
            LOGGER.error("Can't send web socket message: " + webSocketMessage.getPayload(), e);
        }
    }

    @Nullable
    public WebSocketSession get(long userId) {
        return sessions.get(userId);
    }

    public boolean contains(@NotNull WebSocketSession session) {
        return sessions.containsValue(session);
    }
}

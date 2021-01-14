package ru.mherarsh.socket;

import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import ru.mherarsh.messagesystem.MsClientStorageImpl;

@Component
public class SessionChannelInterceptor implements ChannelInterceptor {
    private final MsClientStorageImpl mqClientStorage;

    public SessionChannelInterceptor(@Lazy MsClientStorageImpl mqClientStorage) {
        this.mqClientStorage = mqClientStorage;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == StompCommand.CONNECT) {
            var nativeHeaders = message.getHeaders().get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
            @SuppressWarnings("unchecked")
            var clientId = (String) nativeHeaders.getFirst("clientId");

            if (clientId == null || clientId.isBlank()) {
                throw new IllegalArgumentException("Not set client id");
            }
            mqClientStorage.putClient(accessor.getSessionId(), clientId);
        } else if (accessor.getCommand() == StompCommand.DISCONNECT) {
            mqClientStorage.deleteClient(accessor.getSessionId());
        }

        return message;
    }
}
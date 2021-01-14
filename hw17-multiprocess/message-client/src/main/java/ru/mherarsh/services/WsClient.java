package ru.mherarsh.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.mherarsh.config.WsClientProperties;
import ru.otus.messagesystem.MessagesHandler;
import ru.otus.messagesystem.message.Message;

import java.lang.reflect.Type;

@Service
public class WsClient {
    private static final Logger logger = LoggerFactory.getLogger(WsClient.class);

    private final WsClientProperties properties;
    private final MessagesHandler messagesHandler;
    private final StompSession session;

    public WsClient(WsClientProperties properties, WebSocketStompClient stompClient, MessagesHandler messagesHandler) throws Exception {
        this.properties = properties;
        this.messagesHandler = messagesHandler;

        var stompHeaders = getStompHeaders();
        session = connectToServer(stompClient, stompHeaders);
        subscribeToNewMessages();
    }

    public void sendMessage(Object data){
        session.send(properties.getProducerEndpoint(), data);
    }

    public String getClientId(){
        return properties.getClientId();
    }

    public String getReceiverId(){
        return properties.getReceiverId();
    }

    private void subscribeToNewMessages() {
        session.subscribe(properties.getSubscriptionEndpoint(), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                var message = (Message) payload;

                if (message != null) {
                    messagesHandler.handle(message)
                            .ifPresent(responseMessage -> sendMessage(responseMessage));
                }
            }
        });
    }

    private StompSession connectToServer(WebSocketStompClient stompClient, StompHeaders stompHeaders) throws Exception {
        return stompClient.connect(properties.getServerEndpoint(), new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                logger.error(exception.getMessage(), exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                logger.error(exception.getMessage(), exception);
            }
        }, new Object()).get();
    }

    private StompHeaders getStompHeaders() {
        var headers = new StompHeaders();
        headers.add("clientId", properties.getClientId());
        return headers;
    }
}

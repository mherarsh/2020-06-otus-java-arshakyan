package ru.mherarsh.messagesystem;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.mherarsh.messagesystem.handlers.UserGetAllResponseHandler;
import ru.mherarsh.messagesystem.handlers.UserGetResponseHandler;
import ru.mherarsh.messagesystem.handlers.UserSaveResponseHandler;
import ru.mherarsh.service.ServerNotificationPublisher;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessagesHandler;
import ru.otus.messagesystem.message.MessageType;

@Component
public class MessagesHandlerImpl implements MessagesHandler {
    private final HandlersStore handlersStore;
    private final ServerNotificationPublisher serverNotificationPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    public MessagesHandlerImpl(ServerNotificationPublisher serverNotificationPublisher, SimpMessagingTemplate messagingTemplate) {
        this.serverNotificationPublisher = serverNotificationPublisher;
        this.messagingTemplate = messagingTemplate;
        handlersStore = new HandlersStoreImpl();
        configHandlers();
    }

    protected void configHandlers() {
        handlersStore.addHandler(MessageType.USER_GET, new UserGetResponseHandler(serverNotificationPublisher));
        handlersStore.addHandler(MessageType.USER_GET_ALL, new UserGetAllResponseHandler(serverNotificationPublisher, messagingTemplate));
        handlersStore.addHandler(MessageType.USER_SAVE, new UserSaveResponseHandler(serverNotificationPublisher));
    }

    @Override
    public HandlersStore getHandlersStore() {
        return handlersStore;
    }
}

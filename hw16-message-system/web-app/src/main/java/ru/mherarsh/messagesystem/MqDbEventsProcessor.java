package ru.mherarsh.messagesystem;

import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.messagesystem.handlers.UserGetAllHandler;
import ru.mherarsh.messagesystem.handlers.UserGetHandler;
import ru.mherarsh.messagesystem.handlers.UserSaveHandler;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.message.MessageType;

public class MqDbEventsProcessor extends MqEventProcessor {
    private final DBServiceUser dbServiceUser;

    public MqDbEventsProcessor(MessageSystem messageSystem, CallbackRegistry callbackRegistry, DBServiceUser dbServiceUser, String clientName) {
        super(clientName, "", messageSystem, callbackRegistry);
        this.dbServiceUser = dbServiceUser;

        configMqHandlers(callbackRegistry);
    }

    @Override
    protected void configMqHandlers(CallbackRegistry callbackRegistry) {
        addHandler(MessageType.USER_GET, new UserGetHandler(dbServiceUser));
        addHandler(MessageType.USER_GET_ALL, new UserGetAllHandler(dbServiceUser));
        addHandler(MessageType.USER_SAVE, new UserSaveHandler(dbServiceUser));
    }
}

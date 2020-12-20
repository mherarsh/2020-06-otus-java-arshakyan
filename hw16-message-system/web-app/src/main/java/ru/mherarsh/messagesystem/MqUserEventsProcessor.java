package ru.mherarsh.messagesystem;

import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;
import ru.mherarsh.messagesystem.handlers.CallbackHandler;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.message.MessageType;

public class MqUserEventsProcessor extends MqEventProcessor {
    public MqUserEventsProcessor(MessageSystem messageSystem, CallbackRegistry callbackRegistry, String clientName, String receiverClientName) {
        super(clientName, receiverClientName, messageSystem, callbackRegistry);

        configMqHandlers(callbackRegistry);
    }

    @Override
    public void configMqHandlers(CallbackRegistry callbackRegistry) {
        var callbackHandler = new CallbackHandler(callbackRegistry);

        addHandler(MessageType.USER_GET, callbackHandler);
        addHandler(MessageType.USER_GET_ALL, callbackHandler);
        addHandler(MessageType.USER_SAVE, callbackHandler);
    }
}

package ru.mherarsh.messagesystem;

import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.*;
import ru.otus.messagesystem.message.MessageType;

public abstract class MqEventProcessor {
    private final String clientName;
    private final String receiverClientName;
    private final MsClient msClient;
    private final HandlersStore handlersStore;

    protected MqEventProcessor(String clientName, String receiverClientName, MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        this.clientName = clientName;
        this.receiverClientName = receiverClientName;

        this.handlersStore = new HandlersStoreImpl();
        this.msClient = configMqClient(messageSystem, handlersStore, callbackRegistry);
    }

    protected abstract void configMqHandlers(CallbackRegistry callbackRegistry);

    public <T extends ResultDataType> void sendMessage(T data, MessageType msgType, MessageCallback<T> callback){
        var otuMessage = msClient.produceMessage(receiverClientName, data, msgType, callback);
        msClient.sendMessage(otuMessage);
    }

    protected void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler){
        handlersStore.addHandler(messageType, handler);
    }

    private MsClient configMqClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry){
        var client =  new MsClientImpl(clientName, messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(client);

        return client;
    }
}

package ru.mherarsh.messagesystem;

import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

public class MsClientAdapterImpl implements MsClientAdapter {
    private final MsClient msClient;
    private final MessageSystem messageSystem;

    public MsClientAdapterImpl(MsClient msClient, MessageSystem messageSystem) {
        this.msClient = msClient;
        this.messageSystem = messageSystem;
    }

    @Override
    public <T extends ResultDataType> Message sendMessage(String receiverClientName, T data, MessageType msgType, MessageCallback<T> callback) {
        var outMessage = msClient.produceMessage(receiverClientName, data, msgType, callback);
        msClient.sendMessage(outMessage);
        return outMessage;
    }

    @Override
    public boolean sendMessage(Message msg) {
        return msClient.sendMessage(msg);
    }

    @Override
    public void deleteClientFromMessageStore() {
        messageSystem.removeClient(msClient.getName());
    }
}

package ru.mherarsh.messagesystem;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

public interface MsClientAdapter {
    <T extends ResultDataType> Message sendMessage(String receiverClientName, T data, MessageType msgType, MessageCallback<T> callback);

    boolean sendMessage(Message msg);

    void deleteClientFromMessageStore();
}

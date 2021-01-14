package ru.mherarsh.service;

import org.springframework.stereotype.Service;
import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;
import ru.mherarsh.services.WsClient;
import ru.otus.messagesystem.client.CallbackId;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageId;
import ru.otus.messagesystem.message.MessageType;

@Service
public class AsyncUserServiceImpl implements AsyncUserService {
    private final WsClient wsClient;

    public AsyncUserServiceImpl(WsClient wsClient) {
        this.wsClient = wsClient;
    }

    @Override
    public void saveUser(UserDto userToSave, String sessionId) {
        sendMessage(userToSave, sessionId, MessageType.USER_SAVE);
    }

    @Override
    public void getUser(long id, String sessionId) {
        sendMessage(new UserDto(id), sessionId, MessageType.USER_GET);
    }

    @Override
    public void findAll(String sessionId) {
        sendMessage(new UserListDto(), sessionId, MessageType.USER_GET_ALL);
    }

    private <T extends ResultDataType> void sendMessage(T data, String sessionId, MessageType messageType){
        var message = MessageBuilder.buildMessage(
                wsClient.getClientId(),
                wsClient.getReceiverId(),
                data,
                messageType.getName(),
                new CallbackId(sessionId));

        wsClient.sendMessage(message);
    }
}

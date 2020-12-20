package ru.mherarsh.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;
import ru.mherarsh.messagesystem.MqEventProcessor;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.message.MessageType;

@Service
public class AsyncUserServiceImpl implements AsyncUserService{
    private final MqEventProcessor eventProcessor;

    public AsyncUserServiceImpl(@Qualifier("mqUserEventsProcessor") MqEventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void saveUser(UserDto userToSave, MessageCallback<UserDto> callback) {
        eventProcessor.sendMessage(userToSave, MessageType.USER_SAVE, callback);
    }

    @Override
    public void getUser(long id, MessageCallback<UserDto> callback) {
        eventProcessor.sendMessage(new UserDto(id), MessageType.USER_GET, callback);
    }

    @Override
    public void findAll(MessageCallback<UserListDto> callback) {
        eventProcessor.sendMessage(null, MessageType.USER_GET_ALL, callback);
    }
}

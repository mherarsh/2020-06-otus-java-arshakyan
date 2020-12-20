package ru.mherarsh.service;

import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;
import ru.otus.messagesystem.client.MessageCallback;

public interface AsyncUserService {
    void saveUser(UserDto userToSave, MessageCallback<UserDto> callback);

    void getUser(long id, MessageCallback<UserDto> callback);

    void findAll(MessageCallback<UserListDto> callback);
}

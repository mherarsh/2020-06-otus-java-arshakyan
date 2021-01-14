package ru.mherarsh.service;

import ru.mherarsh.dto.UserDto;

public interface AsyncUserService {
    void saveUser(UserDto userToSave, String sessionId);

    void getUser(long id, String sessionId);

    void findAll(String sessionId);
}

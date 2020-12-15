package ru.mherarsh.dto;

import ru.mherarsh.core.model.User;
import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;
import java.util.stream.Collectors;

public class UserListDto extends ResultDataType {
    private final List<UserDto> users;

    public UserListDto(List<User> users) {
        this.users = users.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public List<UserDto> getUsers(){
        return users;
    }
}

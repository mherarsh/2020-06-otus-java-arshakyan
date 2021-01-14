package ru.mherarsh.dto;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.ArrayList;
import java.util.List;

public class UserListDto extends ResultDataType {
    private List<UserDto> users = new ArrayList<>();

    public UserListDto(List<UserDto> users) {
        this.users = users;
    }

    public UserListDto(){

    }

    public List<UserDto> getUsers(){
        return users;
    }
}

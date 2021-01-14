package ru.mherarsh.messagesystem.handlers;


import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.model.UserBuilder;
import ru.mherarsh.dto.UserListDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;

import java.util.Optional;

public class UserGetAllHandler implements RequestHandler<UserListDto> {
    private final DBServiceUser dbServiceUser;

    public UserGetAllHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        var users = dbServiceUser.findAll();
        return Optional.of(MessageBuilder.buildReplyMessage(msg, UserBuilder.buildUserListDto(users)));
    }
}

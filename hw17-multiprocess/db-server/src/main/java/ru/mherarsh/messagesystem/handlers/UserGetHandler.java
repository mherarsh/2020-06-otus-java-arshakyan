package ru.mherarsh.messagesystem.handlers;


import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.model.UserBuilder;
import ru.mherarsh.dto.UserDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class UserGetHandler implements RequestHandler<UserDto> {
    private final DBServiceUser dbServiceUser;

    public UserGetHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserDto userDto = MessageHelper.getPayload(msg);
        UserDto[] result = new UserDto[1];
        result[0] = new UserDto();
        dbServiceUser.getUser(userDto.getId()).ifPresent(user -> result[0] = UserBuilder.buildUserDto(user));

        return Optional.of(MessageBuilder.buildReplyMessage(msg, result[0]));
    }
}

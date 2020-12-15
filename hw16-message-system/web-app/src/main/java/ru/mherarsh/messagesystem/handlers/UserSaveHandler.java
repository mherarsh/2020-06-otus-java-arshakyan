package ru.mherarsh.messagesystem.handlers;

import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.dto.UserDto;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class UserSaveHandler implements RequestHandler<UserDto> {
    private final DBServiceUser dbServiceUser;

    public UserSaveHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserDto userToSave = MessageHelper.getPayload(msg);
        var userId = dbServiceUser.saveUser(userToSave.toUser());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, new UserDto(userId)));
    }
}

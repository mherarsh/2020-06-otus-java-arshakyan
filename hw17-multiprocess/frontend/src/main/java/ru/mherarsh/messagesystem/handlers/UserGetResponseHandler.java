package ru.mherarsh.messagesystem.handlers;


import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;
import ru.mherarsh.service.ServerNotificationPublisher;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class UserGetResponseHandler implements RequestHandler<UserDto> {
    private final ServerNotificationPublisher serverNotificationPublisher;

    public UserGetResponseHandler(ServerNotificationPublisher serverNotificationPublisher) {
        this.serverNotificationPublisher = serverNotificationPublisher;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserDto data = MessageHelper.getPayload(msg);

        serverNotificationPublisher.publish(
                "/topic/getUsersResponse." + msg.getCallbackId().getId(),
                data);

        return Optional.empty();
    }
}

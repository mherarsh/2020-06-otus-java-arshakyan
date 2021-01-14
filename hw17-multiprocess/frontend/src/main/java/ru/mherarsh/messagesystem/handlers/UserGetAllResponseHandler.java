package ru.mherarsh.messagesystem.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.mherarsh.dto.UserDto;
import ru.mherarsh.dto.UserListDto;
import ru.mherarsh.service.ServerNotificationPublisher;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class UserGetAllResponseHandler implements RequestHandler<UserListDto> {
    private final ServerNotificationPublisher serverNotificationPublisher;

    public UserGetAllResponseHandler(ServerNotificationPublisher serverNotificationPublisher, SimpMessagingTemplate messagingTemplate) {
        this.serverNotificationPublisher = serverNotificationPublisher;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserListDto data = MessageHelper.getPayload(msg);

        serverNotificationPublisher.publish(
                "/topic/getUsersResponse." + msg.getCallbackId().getId(),
                data.getUsers());

        return Optional.empty();
    }
}

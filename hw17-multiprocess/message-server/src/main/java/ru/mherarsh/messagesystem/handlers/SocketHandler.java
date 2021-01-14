package ru.mherarsh.messagesystem.handlers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

@Component
public class SocketHandler implements RequestHandler<ResultDataType> {
    private final SimpMessagingTemplate template;

    public SocketHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Message> handle(Message message) {
        template.convertAndSend(String.format("/topic/consumeMessage.%s", message.getTo()), message);
        return Optional.empty();
    }
}

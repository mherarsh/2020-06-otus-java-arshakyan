package ru.otus.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.message.Message;

import java.util.Optional;

public interface MessagesHandler {
    Logger logger = LoggerFactory.getLogger(MessagesHandler.class);

    HandlersStore getHandlersStore();

    default Optional<Message> handle(Message msg) {
        try {
            var requestHandler = getHandlersStore().getHandlerByType(msg.getType());

            if (requestHandler != null) {
                return requestHandler.handle(msg);
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}

package ru.mherarsh.messagesystem;

import org.springframework.stereotype.Component;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.MessageType;

@Component
public class SingleHandlersStore implements HandlersStore {
    private final RequestHandler<? extends ResultDataType> handler;

    public SingleHandlersStore(RequestHandler<? extends ResultDataType> handler) {
        this.handler = handler;
    }

    @Override
    public RequestHandler<? extends ResultDataType> getHandlerByType(String messageTypeName) {
        return handler;
    }

    @Override
    public void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler) {
        throw new UnsupportedOperationException();
    }
}

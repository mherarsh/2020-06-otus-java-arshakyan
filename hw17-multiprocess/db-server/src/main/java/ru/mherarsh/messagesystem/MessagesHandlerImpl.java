package ru.mherarsh.messagesystem;

import org.springframework.stereotype.Component;
import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.messagesystem.handlers.UserGetAllHandler;
import ru.mherarsh.messagesystem.handlers.UserGetHandler;
import ru.mherarsh.messagesystem.handlers.UserSaveHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessagesHandler;
import ru.otus.messagesystem.message.MessageType;

@Component
public class MessagesHandlerImpl implements MessagesHandler {
    private final HandlersStore handlersStore;
    private final DBServiceUser dbServiceUser;

    public MessagesHandlerImpl(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;

        handlersStore = new HandlersStoreImpl();
        configHandlers();
    }

    protected void configHandlers() {
        handlersStore.addHandler(MessageType.USER_GET, new UserGetHandler(dbServiceUser));
        handlersStore.addHandler(MessageType.USER_GET_ALL, new UserGetAllHandler(dbServiceUser));
        handlersStore.addHandler(MessageType.USER_SAVE, new UserSaveHandler(dbServiceUser));
    }

    @Override
    public HandlersStore getHandlersStore() {
        return handlersStore;
    }
}

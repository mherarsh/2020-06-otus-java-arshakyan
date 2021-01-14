package ru.mherarsh.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.mherarsh.messagesystem.MsClientStorage;
import ru.mherarsh.messagesystem.MsClientStorageImpl;
import ru.otus.messagesystem.message.Message;

@Controller
public class WSController {
    private final MsClientStorage msClientStorage;

    public WSController(MsClientStorageImpl msClientStorage) {
        this.msClientStorage = msClientStorage;
    }

    @MessageMapping("produceMessage")
    public void produceMessage(Message message) {
        msClientStorage.getMqClientById(message.getFrom()).ifPresent(msClientAdapter -> msClientAdapter.sendMessage(message));
    }
}

package ru.otus.listener.impl;

import ru.otus.Message;
import ru.otus.provider.TimeProvider;

import java.time.LocalDateTime;

public class MessageHistory {
    private final LocalDateTime dateTime;
    private final Message oldMessage;
    private final Message newMessage;

    public MessageHistory(TimeProvider timeProvider, Message oldMessage, Message newMessage) throws CloneNotSupportedException {
        this.dateTime = timeProvider.now();
        this.oldMessage = (Message) oldMessage.clone();
        this.newMessage = (Message) newMessage.clone();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }

    @Override
    public String toString() {
        return String.format("dateTime:%s, oldMsg:%s, newMsg:%s", dateTime, oldMessage, newMessage);
    }
}

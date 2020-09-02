package ru.otus.listener.impl;

import ru.otus.Message;
import ru.otus.listener.Listener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ListenerMessageHistory implements Listener {
    private final List<MessageHistory> historyList = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        historyList.add(new MessageHistory(oldMsg, newMsg));
    }

    public List<MessageHistory> getHistoryList() {
        return historyList;
    }

    public class MessageHistory {
        private final LocalDateTime dateTime = LocalDateTime.now();
        private final Message oldMessage;
        private final Message newMessage;

        public MessageHistory(Message oldMessage, Message newMessage) {
            this.oldMessage = oldMessage;
            this.newMessage = newMessage;
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
}

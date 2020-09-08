package ru.otus.listener.impl;

import ru.otus.Message;
import ru.otus.listener.Listener;
import ru.otus.provider.TimeProvider;
import ru.otus.provider.impl.TimeProviderImpl;

import java.util.ArrayList;
import java.util.List;

public class ListenerMessageHistory implements Listener {
    private final List<MessageHistory> historyList = new ArrayList<>();
    private final TimeProvider timeProvider = new TimeProviderImpl();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        try {
            historyList.add(new MessageHistory(timeProvider, oldMsg, newMsg));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public List<MessageHistory> getHistoryList() {
        return historyList;
    }
}

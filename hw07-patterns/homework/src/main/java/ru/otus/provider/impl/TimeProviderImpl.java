package ru.otus.provider.impl;

import ru.otus.provider.TimeProvider;

import java.time.LocalDateTime;

public class TimeProviderImpl implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}

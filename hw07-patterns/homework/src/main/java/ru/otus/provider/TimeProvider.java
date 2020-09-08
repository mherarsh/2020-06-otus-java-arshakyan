package ru.otus.provider;

import java.time.LocalDateTime;

public interface TimeProvider {
    public LocalDateTime now();
}

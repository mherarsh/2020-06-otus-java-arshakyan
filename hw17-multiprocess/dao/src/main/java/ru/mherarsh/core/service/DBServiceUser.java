package ru.mherarsh.core.service;

import ru.mherarsh.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {
    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> findAll();
}

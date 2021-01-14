package ru.mherarsh.core.dao;

import ru.mherarsh.core.sessionmanager.SessionManager;
import ru.mherarsh.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    List<User> findAll();

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}

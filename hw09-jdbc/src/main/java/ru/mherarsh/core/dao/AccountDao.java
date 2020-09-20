package ru.mherarsh.core.dao;

import ru.mherarsh.core.model.Account;
import ru.mherarsh.core.sessionmanager.SessionManager;

import java.util.Optional;

/**
 * @author mherarsh
 */
public interface AccountDao {
    Optional<Account> findById(long id);

    long insertUser(Account account);

    SessionManager getSessionManager();
}

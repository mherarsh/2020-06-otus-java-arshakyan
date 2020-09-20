package ru.mherarsh.core.service;

import ru.mherarsh.core.model.Account;

import java.util.Optional;

/**
 * @author mherarsh
 */
public interface DBServiceAccount {
    long saveAccount(Account account);

    Optional<Account> getAccount(long id);
}

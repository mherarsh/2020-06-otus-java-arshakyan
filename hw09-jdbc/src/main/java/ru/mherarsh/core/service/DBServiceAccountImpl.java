package ru.mherarsh.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mherarsh.core.dao.AccountDao;
import ru.mherarsh.core.model.Account;

import java.util.Optional;

/**
 * @author mherarsh
 */
public class DBServiceAccountImpl implements DBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DBServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DBServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = accountDao.insertUser(account);
                sessionManager.commitSession();

                logger.info("created account: {}", userId);
                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long id) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> userOptional = accountDao.findById(id);

                logger.info("account: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}

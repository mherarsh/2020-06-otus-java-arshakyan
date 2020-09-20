package ru.mherarsh.jdbc.dao;

import ru.mherarsh.core.dao.AccountDao;
import ru.mherarsh.core.model.Account;
import ru.mherarsh.core.sessionmanager.SessionManager;
import ru.mherarsh.jdbc.mapper.JdbcMapper;

import java.util.Optional;

/**
 * @author mherarsh
 */
public class AccountDaoJdbcMapper implements AccountDao {
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbcMapper(JdbcMapper<Account> jdbcMapper) {
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        return jdbcMapper.findById(id);
    }

    @Override
    public long insertUser(Account account) {
        return jdbcMapper.insert(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return jdbcMapper.getSessionManager();
    }
}

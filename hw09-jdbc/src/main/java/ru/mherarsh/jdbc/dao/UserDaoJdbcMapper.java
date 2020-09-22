package ru.mherarsh.jdbc.dao;

import ru.mherarsh.core.dao.UserDao;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.sessionmanager.SessionManager;
import ru.mherarsh.jdbc.mapper.JdbcMapper;

import java.util.Optional;

/**
 * @author mherarsh
 */
public class UserDaoJdbcMapper implements UserDao {
    private final JdbcMapper<User> jdbcMapper;

    public UserDaoJdbcMapper(JdbcMapper<User> jdbcMapper) {
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcMapper.findById(id);
    }

    @Override
    public long insertUser(User user) {
        return jdbcMapper.insert(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return jdbcMapper.getSessionManager();
    }
}

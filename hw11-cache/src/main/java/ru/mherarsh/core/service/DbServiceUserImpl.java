package ru.mherarsh.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mherarsh.cachehw.HwCache;
import ru.mherarsh.core.dao.UserDao;
import ru.mherarsh.core.model.User;

import java.util.Optional;

/**
 * @author mherarsh
 */
public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;
    private final HwCache<Long, User> cache;

    public DbServiceUserImpl(UserDao userDao, HwCache<Long, User> cache) {
        this.userDao = userDao;
        this.cache = cache;
    }

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
        this.cache = null;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                sessionManager.commitSession();

                cachePut(user);

                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        if (cache != null) {
            var valueFromCache = cache.get(id);
            if(valueFromCache != null){
                return Optional.of(valueFromCache);
            }
        }

        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userFromDb = userDao.findById(id);
                userFromDb.ifPresent(this::cachePut);
                return userFromDb;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    private void cachePut(User user) {
        try {
            if (cache != null) {
                cache.put(user.getId(), user);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}

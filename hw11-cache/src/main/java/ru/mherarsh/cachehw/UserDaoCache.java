package ru.mherarsh.cachehw;

import ru.mherarsh.core.dao.UserDao;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.sessionmanager.SessionManager;

import java.util.Optional;

public class UserDaoCache implements UserDao {
    private final UserDao userDao;
    private final MyCache<Long, User> cache;

    public UserDaoCache(UserDao userDao, int heapSize) {
        this.userDao = userDao;
        this.cache = new MyCache<>(heapSize);
    }

    @Override
    public Optional<User> findById(long id) {
        var userFromCache = cache.get(id);

        if(userFromCache != null){
            return Optional.of(userFromCache);
        }

        var userFromDao = userDao.findById(id);
        userFromDao.ifPresent(user -> cache.put(id, user));

        return userFromDao;
    }

    @Override
    public long insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        userDao.insertOrUpdate(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return userDao.getSessionManager();
    }
}

package ru.mherarsh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import ru.mherarsh.cachehw.MyCache;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.core.service.DbServiceUserImpl;
import ru.mherarsh.hibernate.HibernateUtils;
import ru.mherarsh.hibernate.dao.UserDaoHibernate;
import ru.mherarsh.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openjdk.jmh.annotations.Mode.SingleShotTime;

@State(Scope.Thread)
@BenchmarkMode(SingleShotTime)
@OutputTimeUnit(MILLISECONDS)
public class HomeWorkCache {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int COUNT = 100;

    private DBServiceUser dbServiceUser;
    private DBServiceUser dbServiceUserCached;
    private List<Long> usersIdList;
    private List<Long> usersIdListCached;

    public static void main(String[] args) throws RunnerException {
        var opt = new OptionsBuilder().include(HomeWorkCache.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }

    private static SessionManagerHibernate getHibernateSessionManager() {
        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        return new SessionManagerHibernate(sessionFactory);
    }

    @Setup
    public void setup() {
        var sessionManager = getHibernateSessionManager();
        var userDao = new UserDaoHibernate(sessionManager);
        var cache = new MyCache<Long, User>();

        dbServiceUser = new DbServiceUserImpl(userDao);
        dbServiceUserCached = new DbServiceUserImpl(userDao, cache);

        usersIdList = generateUsers(dbServiceUser, COUNT);
        usersIdListCached = generateUsers(dbServiceUserCached, COUNT);
    }

    @Benchmark
    public void getUsersFromDb() {
        getUsers(dbServiceUser, usersIdList);
    }

    @Benchmark
    public void getUsersCached() {
        getUsers(dbServiceUserCached, usersIdListCached);
    }

    private void getUsers(DBServiceUser dbServiceUser, List<Long> idList) {
        idList.forEach(id -> dbServiceUser.getUser(id).ifPresent(System.out::println));
    }

    private List<Long> generateUsers(DBServiceUser dbServiceUser, int count) {
        var idList = new ArrayList<Long>();
        for (int i = 0; i < count; i++) {
            idList.add(dbServiceUser.saveUser(generateUserById(i)));
        }

        return idList;
    }

    private User generateUserById(int id) {
        var user = User.builder().name("user " + id).build();
        user.setAddress(AddressDataSet.builder().street("street " + id).build());
        user.setPhones(List.of(
                PhoneDataSet.builder().number("1111111").user(user).build(),
                PhoneDataSet.builder().number("2222222").user(user).build(),
                PhoneDataSet.builder().number("3333333").user(user).build()
        ));

        return user;
    }
}

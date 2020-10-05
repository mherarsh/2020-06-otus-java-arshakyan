package ru.mherarsh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mherarsh.cachehw.UserDaoCache;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.core.service.DbServiceUserImpl;
import ru.mherarsh.hibernate.HibernateUtils;
import ru.mherarsh.hibernate.dao.UserDaoHibernate;
import ru.mherarsh.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;
import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.openjdk.jmh.annotations.Mode.SingleShotTime;

@State(Scope.Thread)
@BenchmarkMode(SingleShotTime)
@OutputTimeUnit(MILLISECONDS)
public class HomeWorkCache {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger logger = LoggerFactory.getLogger(HomeWorkCache.class);
    private static final int heapSize = 100;
    private static final int idRange = 500;
    private static final int getIterations = 1;

    private DBServiceUser dbServiceUser;
    private DBServiceUser dbServiceUserCached;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(HomeWorkCache.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }

    private static SessionManagerHibernate getHibernateSessionManager() {
        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        return new SessionManagerHibernate(sessionFactory);
    }

    @Setup
    public void setup(){
        var sessionManager = getHibernateSessionManager();
        var userDao = new UserDaoHibernate(sessionManager);

        dbServiceUser = new DbServiceUserImpl(userDao);
        dbServiceUserCached = new DbServiceUserImpl(new UserDaoCache(userDao, heapSize));

        generateUsers(dbServiceUser, idRange);
    }

    @Benchmark
    public void getFromDb(){
        getUsers(dbServiceUser, idRange, getIterations);
    }

    @Benchmark
    public void getCached(){
        getUsers(dbServiceUserCached, idRange, getIterations);
    }

    private void getUsers(DBServiceUser dbServiceUser, int range, int iterations) {
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < range; j++) {
                var id = getRandomId(range);
                var userById = dbServiceUser.getUser(id);
                userById.ifPresent(System.out::println);
            }
        }
    }

    private int getRandomId(int range){
        var min = 0;
        var max = range-1;
        var diff = max - min;
        var random = new Random();
        return random.nextInt(diff + 1) + min;
    }

    private void generateUsers(DBServiceUser dbServiceUser, int range) {
        for (int i = 0; i < range; i++) {
            dbServiceUser.saveUser(generateUserById(i));
        }
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

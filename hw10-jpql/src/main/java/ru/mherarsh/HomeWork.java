package ru.mherarsh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.service.DbServiceUserImpl;
import ru.mherarsh.hibernate.HibernateUtils;
import ru.mherarsh.hibernate.dao.UserDaoHibernate;
import ru.mherarsh.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.List;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var sessionManager = getHibernateSessionManager();
        var userDao = new UserDaoHibernate(sessionManager);

        var dbServiceUser = new DbServiceUserImpl(userDao);

        var user = User.builder().name("user 1").build();
        user.setAddress(AddressDataSet.builder().street("street 1").build());
        user.setPhones(List.of(
                PhoneDataSet.builder().number("1111111").user(user).build(),
                PhoneDataSet.builder().number("2222222").user(user).build(),
                PhoneDataSet.builder().number("3333333").user(user).build()
        ));

        var userId = dbServiceUser.saveUser(user);

        var userById = dbServiceUser.getUser(userId);
        userById.ifPresent(System.out::println);
    }

    private static SessionManagerHibernate getHibernateSessionManager() {
        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        return new SessionManagerHibernate(sessionFactory);
    }
}

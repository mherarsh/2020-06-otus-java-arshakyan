package ru.mherarsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.hibernate.HibernateUtils;
import ru.mherarsh.hibernate.sessionmanager.SessionManagerHibernate;

@Configuration
public class HibernateConfig {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    @Bean
    public SessionManagerHibernate getHibernateSessionFactory() {
        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        return new SessionManagerHibernate(sessionFactory);
    }
}

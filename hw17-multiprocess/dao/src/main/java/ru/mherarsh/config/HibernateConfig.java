package ru.mherarsh.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mherarsh.hibernate.HibernateUtils;
import ru.mherarsh.model.AddressDataSet;
import ru.mherarsh.model.PhoneDataSet;
import ru.mherarsh.model.User;

@Configuration
public class HibernateConfig {
    public static final String HIBERNATE_CFG_FILE = "WEB-INF/hibernate.cfg.xml";

    @Bean
    public SessionFactory getSessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
    }
}

package ru.mherarsh.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.hibernate.HibernateUtils;

@Configuration
public class HibernateConfig {
    public static final String HIBERNATE_CFG_FILE = "WEB-INF/hibernate.cfg.xml";

    @Bean
    public SessionFactory getSessionFactory() {
        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
    }
}

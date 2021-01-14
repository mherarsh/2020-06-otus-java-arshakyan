package ru.mherarsh;

import com.google.gson.GsonBuilder;
import ru.mherarsh.core.model.AddressDataSet;
import ru.mherarsh.core.model.PhoneDataSet;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.core.service.DbServiceUserImpl;
import ru.mherarsh.core.service.InMemoryLoginServiceImpl;
import ru.mherarsh.core.service.TemplateProcessorImpl;
import ru.mherarsh.hibernate.HibernateUtils;
import ru.mherarsh.hibernate.dao.UserDaoHibernate;
import ru.mherarsh.hibernate.sessionmanager.SessionManagerHibernate;
import ru.mherarsh.server.UsersWebServerWithBasicSecurity;

import java.util.List;

public class WebServer {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var sessionManager = getHibernateSessionManager();
        var userDao = new UserDaoHibernate(sessionManager);
        var userService = new DbServiceUserImpl(userDao);
        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var loginService = new InMemoryLoginServiceImpl();

        generateUsers(userService);

        var usersWebServer = new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, userService, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static SessionManagerHibernate getHibernateSessionManager() {
        var sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);

        return new SessionManagerHibernate(sessionFactory);
    }

    private static void generateUsers(DBServiceUser userService) {
        for (var i = 0; i < 5; i++) {
            var user = User.builder().name("user " + i).build();
            user.setAddress(AddressDataSet.builder().street("street " + i).build());
            user.setPhones(List.of(
                    PhoneDataSet.builder().number("1111111").user(user).build()
            ));

            userService.saveUser(user);
        }
    }
}

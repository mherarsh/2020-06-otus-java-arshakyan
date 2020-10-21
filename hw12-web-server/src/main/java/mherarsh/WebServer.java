package mherarsh;

import com.google.gson.GsonBuilder;
import mherarsh.core.model.AddressDataSet;
import mherarsh.core.model.PhoneDataSet;
import mherarsh.core.model.User;
import mherarsh.core.service.DBServiceUser;
import mherarsh.core.service.DbServiceUserImpl;
import mherarsh.core.service.InMemoryLoginServiceImpl;
import mherarsh.core.service.TemplateProcessorImpl;
import mherarsh.hibernate.HibernateUtils;
import mherarsh.hibernate.dao.UserDaoHibernate;
import mherarsh.hibernate.sessionmanager.SessionManagerHibernate;
import mherarsh.server.UsersWebServerWithBasicSecurity;

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

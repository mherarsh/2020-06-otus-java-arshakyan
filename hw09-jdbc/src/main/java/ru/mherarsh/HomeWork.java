package ru.mherarsh;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mherarsh.core.dao.UserDao;
import ru.mherarsh.core.model.Account;
import ru.mherarsh.core.model.User;
import ru.mherarsh.core.service.DBServiceAccountImpl;
import ru.mherarsh.core.service.DbServiceUserImpl;
import ru.mherarsh.h2.DataSourceH2;
import ru.mherarsh.jdbc.DbExecutorImpl;
import ru.mherarsh.jdbc.dao.AccountDaoJdbcMapper;
import ru.mherarsh.jdbc.dao.UserDaoJdbcMapper;
import ru.mherarsh.jdbc.mapper.JdbcMapper;
import ru.mherarsh.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.mherarsh.jdbc.mapper.impl.EntitySQLMetaDataImpl;
import ru.mherarsh.jdbc.mapper.impl.JdbcMapperImpl;
import ru.mherarsh.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();

        var entityClassMetaData = new EntityClassMetaDataImpl<User>(User.class);
        var entitySQLMetaData = new EntitySQLMetaDataImpl<User>(entityClassMetaData);
        var jdbcMapperUser = new JdbcMapperImpl<User>(sessionManager, dbExecutor, entitySQLMetaData, entityClassMetaData);

        UserDao userDao = new UserDaoJdbcMapper(jdbcMapperUser);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser"));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
// Работа со счетом
        var dbExecutorAccount = new DbExecutorImpl<Account>();

        var entityClassMetaDataAccount = new EntityClassMetaDataImpl<Account>(Account.class);
        var entitySQLMetaDataAccount = new EntitySQLMetaDataImpl<Account>(entityClassMetaDataAccount);
        var jdbcMapperUserAccount = new JdbcMapperImpl<Account>(sessionManager, dbExecutorAccount, entitySQLMetaDataAccount, entityClassMetaDataAccount);

        var accountDao = new AccountDaoJdbcMapper(jdbcMapperUserAccount);

        var dbServiceAccount = new DBServiceAccountImpl(accountDao);
        var idAccount = dbServiceAccount.saveAccount(new Account("type", 10));
        var account = dbServiceAccount.getAccount(idAccount);

        account.ifPresentOrElse(
                crAccount -> logger.info("created account:{}", crAccount.toString()),
                () -> logger.info("account was not created")
        );
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}

package ru.mherarsh.jdbc.mapper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mherarsh.core.sessionmanager.SessionManager;
import ru.mherarsh.jdbc.DbExecutor;
import ru.mherarsh.jdbc.mapper.EntityClassMetaData;
import ru.mherarsh.jdbc.mapper.EntitySQLMetaData;
import ru.mherarsh.jdbc.mapper.JdbcMapper;
import ru.mherarsh.jdbc.sessionmanager.SessionManagerJdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author mherarsh
 */
public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public long insert(T objectData) {
        try {
            return dbExecutor.executeInsert(getConnection(),
                    entitySQLMetaData.getInsertSql(),
                    getFieldValuesList(objectData));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T objectData) {
        try {
            dbExecutor.executeInsert(getConnection(),
                    entitySQLMetaData.getUpdateSql(),
                    getFieldValuesList(objectData));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        try {
            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);

            var findResult = findById(idField.get(objectData));
            if (findResult.isEmpty()) {
                insert(objectData);
            } else {
                update(objectData);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> findById(Object id) {
        try {
            return dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return createObjectFromResultSet(rs);
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    private T createObjectFromResultSet(ResultSet rs){
        try {
            var instance = entityClassMetaData.getConstructor().newInstance();

            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            idField.set(instance, rs.getObject(idField.getName()));

            for (var filed : entityClassMetaData.getFieldsWithoutId()) {
                filed.setAccessible(true);
                filed.set(instance, rs.getObject(filed.getName()));
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> getFieldValuesList(T objectData) throws IllegalAccessException {
        var params = new ArrayList<>();
        for (var filed : entityClassMetaData.getFieldsWithoutId()) {
            filed.setAccessible(true);
            params.add(filed.get(objectData));
        }
        return params;
    }
}

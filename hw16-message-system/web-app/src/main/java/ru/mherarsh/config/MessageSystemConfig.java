package ru.mherarsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mherarsh.core.service.DBServiceUser;
import ru.mherarsh.messagesystem.MqDbEventsProcessor;
import ru.mherarsh.messagesystem.MqEventProcessor;
import ru.mherarsh.messagesystem.MqUserEventsProcessor;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;

@Configuration
public class MessageSystemConfig {
    private static final String DB_USER_SERVICE_CLIENT_NAME = "dbUserServiceClientName";
    private static final String ASYNC_USER_SERVICE_CLIENT_NAME = "asyncUserServiceClientName";

    @Bean
    public MessageSystem getMessageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry getCallbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean(name = "mqDbEventsProcessor")
    public MqEventProcessor getMqDbEventsProcessor(MessageSystem messageSystem, CallbackRegistry callbackRegistry, DBServiceUser dbServiceUser){
        return new MqDbEventsProcessor(messageSystem, callbackRegistry, dbServiceUser, DB_USER_SERVICE_CLIENT_NAME);
    }

    @Bean(name = "mqUserEventsProcessor")
    public MqEventProcessor getUserEventsProcessor(MessageSystem messageSystem, CallbackRegistry callbackRegistry){
        return new MqUserEventsProcessor(messageSystem, callbackRegistry, ASYNC_USER_SERVICE_CLIENT_NAME, DB_USER_SERVICE_CLIENT_NAME);
    }
}

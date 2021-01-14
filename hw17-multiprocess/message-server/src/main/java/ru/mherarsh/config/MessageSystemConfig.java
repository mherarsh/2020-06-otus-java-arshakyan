package ru.mherarsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mherarsh.messagesystem.MsClientAdapter;
import ru.mherarsh.messagesystem.MsClientAdapterImpl;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClientImpl;

import java.util.function.Function;

@Configuration
public class MessageSystemConfig {
    @Bean
    public MessageSystem getMessageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry getCallbackRegister(){
        return new CallbackRegistryImpl();
    }

    @Bean
    public Function<String, MsClientAdapter> msClientAdapterFactory(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry){
        return clientId -> getNewMsClientAdapter(clientId, messageSystem, handlersStore, callbackRegistry);
    }

    private MsClientAdapter getNewMsClientAdapter(String clientId, MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry) {
        var client =  new MsClientImpl(clientId, messageSystem, handlersStore, callbackRegistry);
        messageSystem.addClient(client);

        return new MsClientAdapterImpl(client, messageSystem);
    }
}

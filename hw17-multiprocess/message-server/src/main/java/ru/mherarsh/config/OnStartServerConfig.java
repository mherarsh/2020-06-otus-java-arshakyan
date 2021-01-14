package ru.mherarsh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import ru.mherarsh.starter.ClientStarter;
import ru.mherarsh.starter.ClientStarterImpl;


@Configuration
public class OnStartServerConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${ms.runClients}")
    private boolean startClients;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(startClients){
            getClientStarter().run();
        }
    }

    public ClientStarter getClientStarter(){
        return new ClientStarterImpl();
    }
}
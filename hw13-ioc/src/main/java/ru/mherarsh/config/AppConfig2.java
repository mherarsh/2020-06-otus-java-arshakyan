package ru.mherarsh.config;

import ru.mherarsh.appcontainer.api.AppComponent;
import ru.mherarsh.appcontainer.api.AppComponentsContainerConfig;
import ru.mherarsh.services.*;

@AppComponentsContainerConfig(order = 2)
public class AppConfig2 {
    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}

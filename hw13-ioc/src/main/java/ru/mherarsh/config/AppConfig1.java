package ru.mherarsh.config;

import ru.mherarsh.appcontainer.api.AppComponent;
import ru.mherarsh.appcontainer.api.AppComponentsContainerConfig;
import ru.mherarsh.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig1 {
    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }
}

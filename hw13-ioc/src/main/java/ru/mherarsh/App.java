package ru.mherarsh;

import ru.mherarsh.appcontainer.api.AppComponentsContainer;
import ru.mherarsh.config.AppConfig;
import ru.mherarsh.config.AppConfig1;
import ru.mherarsh.config.AppConfig2;
import ru.mherarsh.services.GameProcessor;
import ru.mherarsh.appcontainer.AppComponentsContainerImpl;
import ru.mherarsh.services.GameProcessorImpl;

/*
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.

PS Приложение представляет из себя тренажер таблицы умножения)
*/

public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

//        Тут можно использовать библиотеку Reflections (см. зависимости)
//        AppComponentsContainer container = new AppComponentsContainerImpl("ru.mherarsh.config");

//        Обязательный вариант
//        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

//        Приложение должно работать в каждом из указанных ниже вариантов
//        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
//        GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}

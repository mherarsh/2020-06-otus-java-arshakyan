package ru.mherarsh.appcontainer;

import org.reflections.Reflections;
import ru.mherarsh.appcontainer.api.AppComponent;
import ru.mherarsh.appcontainer.api.AppComponentsContainer;
import ru.mherarsh.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) throws Exception {
        checkConfigClass(initialConfigClass);

        processConfigs(initialConfigClass);
    }

    public AppComponentsContainerImpl(String scanPackageName) throws Exception {
        var initialConfigClass = new Reflections(scanPackageName)
                .getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(new Class[0]);

        checkConfigClass(initialConfigClass);

        processConfigs(initialConfigClass);
    }

    private void processConfigs(Class<?>[] initialConfigClass) throws Exception {
        var initialConfigClassSorted = Arrays.stream(initialConfigClass)
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());

        for (var initConfigClass : initialConfigClassSorted) {
            processConfig(initConfigClass);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) getComponentByType(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) getComponentByName(componentName);
    }

    private void processConfig(Class<?> configClass) throws Exception {
        var appConfig = getAppConfigInstance(configClass);
        var componentCreatorMethods = getComponentCreatorMethods(configClass);

        for (var creator : componentCreatorMethods) {
            addComponent(getComponentName(creator),
                    createComponent(appConfig,
                            creator,
                            getComponents(creator.getParameterTypes())
                    ));
        }
    }

    private void checkConfigClass(Class<?>[] configClasses) {
        if (configClasses.length == 0) {
            throw new IllegalArgumentException("Empty initial config classes list");
        }

        for (Class<?> aClass : configClasses) {
            if (!aClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                throw new IllegalArgumentException(String.format("Given class is not config %s", aClass.getName()));
            }
        }
    }

    private Object getAppConfigInstance(Class<?> configClass) throws Exception {
        return configClass.getDeclaredConstructor().newInstance();
    }

    private List<Method> getComponentCreatorMethods(Class<?> configClass) {
        return getAllMethods(configClass, withAnnotation(AppComponent.class)).stream()
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
    }

    private String getComponentName(Method creator) {
        return creator.getAnnotation(AppComponent.class).name();
    }

    private Object[] getComponents(Class<?>[] componentTypes) {
        var components = new Object[componentTypes.length];

        if (components.length == 0) {
            return components;
        }

        for (int i = 0; i < componentTypes.length; i++) {
            components[i] = getComponentByType(componentTypes[i]);
        }

        return components;
    }

    private Object getComponentByType(Class<?> componentType) {
        var components = appComponents.stream()
                .filter(x -> componentType.isAssignableFrom(x.getClass()))
                .collect(Collectors.toList());

        checkComponentsCount(components.size());

        return components.get(0);
    }

    private Object createComponent(Object appConfig, Method creator, Object... args) throws Exception {
        return creator.invoke(appConfig, args);
    }

    private void addComponent(String componentName, Object component) {
        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }

    private Object getComponentByName(String componentName) {
        var component = appComponentsByName.get(componentName);

        checkComponentsCount(component == null ? 0 : 1);

        return component;
    }

    private void checkComponentsCount(int componentsCount) {
        if (componentsCount == 0) {
            throw new IllegalArgumentException("can't find component");
        } else if (componentsCount > 1) {
            throw new IllegalArgumentException("failed to determine component - many declarations");
        }
    }
}

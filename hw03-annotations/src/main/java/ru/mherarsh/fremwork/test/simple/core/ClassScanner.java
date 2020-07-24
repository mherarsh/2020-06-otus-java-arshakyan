package ru.mherarsh.fremwork.test.simple.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ClassScanner {
    private final Class<?> clazz;

    public ClassScanner(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Method getAnnotatedMethod(Class<? extends Annotation> annotation) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                return method;
            }
        }
        return null;
    }

    public Method[] getAnnotatedMethods(Class<? extends Annotation> annotation) {
        final ArrayList<Method> methods = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }

        return methods.toArray(new Method[0]);
    }

    public Constructor<?> getDefaultConstructor() throws NoSuchMethodException {
        return clazz.getConstructor();
    }
}

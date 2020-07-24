package ru.mherarsh.fremwork.test.simple.core;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class PackageScanner {
    private final Reflections reflections;

    public PackageScanner(String scanPackageName) {
        reflections = new Reflections(scanPackageName);
    }

    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }
}

package ru.mherarsh.aop.dao;

public class MethodDescription {
    private final String name;
    private final String description;

    public MethodDescription(String methodName, String methodDescription) {
        name = methodName;
        description = methodDescription;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

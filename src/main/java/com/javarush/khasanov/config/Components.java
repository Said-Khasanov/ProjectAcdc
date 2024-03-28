package com.javarush.khasanov.config;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

public final class Components {
    private Components() {
    }

    private static final Map<Class<?>, Object> instantMap = new ConcurrentHashMap<>();

    @SneakyThrows
    public static <T> T get(Class<?> componentClass) {
        Object o = instantMap.get(componentClass);
        if (isNull(o)) {
            Constructor<?> constructor = componentClass.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = get(parameterTypes[i]);
            }
            Object component = constructor.newInstance(parameters);
            instantMap.put(componentClass, component);
        }
        return (T) requireNonNullElse(o, instantMap.get(componentClass));
    }
}

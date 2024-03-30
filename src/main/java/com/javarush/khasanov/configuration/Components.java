package com.javarush.khasanov.configuration;

import com.javarush.khasanov.exception.ProjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

public final class Components {
    private Components() {
    }

    private static final Map<Class<?>, Object> instantMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<?> componentClass) {
        Object o = instantMap.get(componentClass);
        if (isNull(o)) {
            Constructor<?> constructor = componentClass.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = get(parameterTypes[i]);
            }
            try {
                Object component = constructor.newInstance(parameters);
                instantMap.put(componentClass, component);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ProjectException("Не удалось создать компонент");
            }
        }
        return (T) requireNonNullElse(o, instantMap.get(componentClass));
    }
}
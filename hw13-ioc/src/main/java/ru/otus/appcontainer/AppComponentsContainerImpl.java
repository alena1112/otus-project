package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        getConfigClasses(Set.of(initialConfigClass)).forEach(this::processConfig);
    }

    public AppComponentsContainerImpl(String path) {
        Reflections reflections = new Reflections(path, Scanners.SubTypes.filterResultsBy(s -> true));
        getConfigClasses(reflections.getSubTypesOf(Object.class)).forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        try {
            checkConfigClass(configClass);

            List<Map.Entry<Method, AppComponent>> entryList = getConfigBeans(configClass);

            Object newInstance = configClass.getConstructor().newInstance();

            for (Map.Entry<Method, AppComponent> entry : entryList) {
                AppComponent appComponent = entry.getValue();
                Method method = entry.getKey();
                Object beanObj = invokeBean(newInstance, method);
                putBeanInContext(appComponent.name(), beanObj);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error while creating beans in class %s",
                    configClass.getName()), e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> foundBeans = appComponents.stream()
                .filter(bean -> {
                    Class<?> beanClass = bean.getClass();
                    return componentClass.isAssignableFrom(beanClass);
                }).collect(Collectors.toList());

        if (foundBeans.size() > 1) {
            throw new IllegalArgumentException(String.format("For given component found several instances %s. " +
                            "Try to use bean name", componentClass.getName()));
        }
        return foundBeans.size() == 1 ? (C) foundBeans.get(0) : null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private List<Class<?>> getConfigClasses(Set<Class<?>> configs) {
        Map<Class<?>, AppComponentsContainerConfig> map = new HashMap<>();
        for (Class<?> configClass : configs) {
            var annotation = configClass.getAnnotation(AppComponentsContainerConfig.class);
            if (annotation != null) {
                map.put(configClass, annotation);
            }
        }
        return map.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.comparingInt(AppComponentsContainerConfig::order)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Map.Entry<Method, AppComponent>> getConfigBeans(Class<?> configClass) {
        Map<Method, AppComponent> map = new HashMap<>();
        for (Method method : configClass.getDeclaredMethods()) {
            AppComponent annotation = method.getAnnotation(AppComponent.class);
            if (annotation != null) {
                map.put(method, annotation);
            }
        }
        return map.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.comparingInt(AppComponent::order)))
                .collect(Collectors.toList());
    }

    private Object invokeBean(Object configInstance, Method method) throws IllegalAccessException, InvocationTargetException {
        Parameter[] parameters = method.getParameters();
        Object[] paramsObj = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            paramsObj[i] = getAppComponent(parameters[i].getType());
            if (paramsObj[i] == null) {
                throw new IllegalArgumentException(String.format("Couldn't create the bean %s. " +
                        "Bean dependency %s was not found", method.getName(), parameters[i].getType()));
            }
        }
        return method.invoke(configInstance, paramsObj);
    }

    private void putBeanInContext(String beanName, Object beanObj) {
        if (appComponentsByName.containsKey(beanName)) {
            throw new IllegalArgumentException(String.format("Bean with name %s already exists", beanName));
        }
        appComponentsByName.put(beanName, beanObj);
        appComponents.add(beanObj);
    }
}

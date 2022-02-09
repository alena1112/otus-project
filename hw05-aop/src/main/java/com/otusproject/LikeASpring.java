package com.otusproject;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class LikeASpring {
    private final List<String> cashResults;

    public LikeASpring() {
        cashResults = loadCashData();
    }

    public TestLoggingInterface createTestLogging() {
        TestLoggingInterface testLogging = new TestLogging();
        return (TestLoggingInterface)
                Proxy.newProxyInstance(
                        LikeASpring.class.getClassLoader(),
                        new Class[]{TestLoggingInterface.class},
                        (proxy, method, args) -> {
                            if (isMethodNeedLogging(method)) {
                                System.out.printf("executed method: %s, param: %d\n", method.getName(), args.length);
                            }
                            return method.invoke(testLogging, args);
                        }
                );
    }

    private List<String> loadCashData() {
        List<String> cashResults = new ArrayList<>();
        Method[] methods = TestLogging.class.getDeclaredMethods();
        for (Method method : methods) {
            if (Arrays.stream(method.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation.annotationType() == Log.class)) {
                cashResults.add(generateMethodName(method));
            }
        }
        return cashResults;
    }

    private boolean isMethodNeedLogging(Method interfaceMethod) {
        return cashResults.contains(generateMethodName(interfaceMethod));
    }

    private String generateMethodName(Method method) {
        return method.getName() +
                Arrays.stream(method.getParameterTypes())
                        .map(Type::getTypeName)
                        .collect(Collectors.joining(",", "(", ")"));
    }
}

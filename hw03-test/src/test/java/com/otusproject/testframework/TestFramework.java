package com.otusproject.testframework;

import com.otusproject.testframework.annotations.After;
import com.otusproject.testframework.annotations.Before;
import com.otusproject.testframework.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestFramework {

    public void runTestFramework(List<String> testClassNames) throws TestFrameworkException {
        List<Class<?>> testClasses = findClasses(testClassNames);

        if (testClasses.size() == 0) {
            throw new TestFrameworkException("Could not find any classes");
        }

        for (Class<?> testClass : testClasses) {
            System.out.printf("\n--------------Start %s--------------\n", testClass.getSimpleName());

            Method[] methods = testClass.getDeclaredMethods();
            if (methods.length == 0) {
                throw new TestFrameworkException("Could not find any test methods");
            }

            List<Method> beforeMethods = getMethodsByAnnotation(methods, Before.class);
            List<Method> afterMethods = getMethodsByAnnotation(methods, After.class);
            List<Method> testMethods = getMethodsByAnnotation(methods, Test.class);

            int successfulTestsCount = 0;
            for (int i = 0; i < testMethods.size(); i++) {
                Method testMethod = testMethods.get(i);
                System.out.printf("%d. Start running test %s\n", i + 1, testMethod.getName());
                if (runTestMethod(testMethod, beforeMethods, afterMethods, testClass)) {
                    successfulTestsCount++;
                }
            }

            System.out.printf("\nAll test count %d. Successful test %d. Failed test %d\n", testMethods.size(),
                    successfulTestsCount, testMethods.size() - successfulTestsCount);
            System.out.printf("--------------End %s--------------\n", testClass.getSimpleName());
        }
    }

    private boolean runTestMethod(Method testMethod, List<Method> beforeMethods, List<Method> afterMethods, Class<?> testClass) throws TestFrameworkException {
        Object testClassObject = createTestClassObject(testClass);
        try {
            invokeMethod(testClassObject, beforeMethods);
            invokeMethod(testClassObject, List.of(testMethod));
            System.out.printf("Test %s passed successfully!\n", testMethod.getName());
        } catch (TestFrameworkException e) {
            System.out.printf("Test %s failed! Error: %s\n", testMethod.getName(), e.getMessage());
            return false;
        } finally {
            invokeMethod(testClassObject, afterMethods);
        }
        return true;
    }

    private List<Class<?>> findClasses(List<String> testClassNames) throws TestFrameworkException {
        List<Class<?>> result = new ArrayList<>();
        for (String className : testClassNames) {
            Class<?> testClass;
            try {
                testClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new TestFrameworkException("Test class %s not found", className);
            }
            result.add(testClass);
        }
        return result;
    }

    private <T extends Annotation> List<Method> getMethodsByAnnotation(Method[] methods, Class<T> annotation) throws TestFrameworkException {
        return Arrays.stream(methods)
                .filter(m -> m.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }

    private Method validateMethod(Object testClassObject, Method method) throws TestFrameworkException {
        if (method.canAccess(testClassObject) && method.getReturnType() == void.class && method.getParameterCount() == 0) {
            return method;
        } else {
            throw new TestFrameworkException("Method %s can be public, void and without params", method.getName());
        }
    }

    private Object createTestClassObject(Class<?> testClass) throws TestFrameworkException {
        Object testClassObject;
        try {
            Constructor<?> constructor = testClass.getConstructor();
            testClassObject = constructor.newInstance();

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new TestFrameworkException("Could not run test class %s. Check class constructor", testClass.getSimpleName());
        }
        return testClassObject;
    }

    private void invokeMethod(Object testClassObject, List<Method> methods) throws TestFrameworkException {
        for (Method method : methods) {
            try {
                validateMethod(testClassObject, method).invoke(testClassObject);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof AssertionError) {
                    throw new TestFrameworkException(e.getTargetException().getMessage());
                } else {
                    throw new TestFrameworkException("Could not run test class %s. Check class method %s",
                            testClassObject.getClass().getSimpleName(), method.getName());
                }
            } catch (IllegalAccessException e) {
                throw new TestFrameworkException("Could not run test class %s. Check class method %s",
                        testClassObject.getClass().getSimpleName(), method.getName());
            }
        }
    }
}

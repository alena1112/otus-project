package com.otusproject;

import com.otusproject.testframework.TestFramework;
import com.otusproject.testframework.TestFrameworkException;

import java.util.Arrays;

public class RunTestApp {
    public static void main(String[] args) {
        args = new String[]{"com.otusproject.clinic.VeterinaryClinicTest", "com.otusproject.clinic.TestTest"};

        TestFramework testFramework = new TestFramework();
        try {
            testFramework.runTestFramework(Arrays.asList(args));
        } catch (TestFrameworkException e) {
            System.out.println(e.getMessage());
        }
    }
}

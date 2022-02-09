package com.otusproject;

public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param) {
        System.out.println("I'm here with one param!");
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println("I'm here with two params!");
    }

    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("I'm here with three params!");
    }
}

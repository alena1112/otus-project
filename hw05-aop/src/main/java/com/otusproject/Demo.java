package com.otusproject;

public class Demo {
    public static void main(String[] args) {
        LikeASpring app = new LikeASpring();
        TestLoggingInterface logging = app.createTestLogging();
        logging.calculation(6);
        logging.calculation(6, 5);
        logging.calculation(6, 5, "4");
        logging.calculation(6);
    }
}

package com.otusproject.clinic;

import com.otusproject.testframework.Assertions;
import com.otusproject.testframework.annotations.After;
import com.otusproject.testframework.annotations.Test;

public class TestTest {

    @After
    public void tearDown() {
        System.out.println("TearDown is invoked!");
    }

    @Test
    public void testTest() {
        Assertions.assertEquals(0, 1);
    }
}

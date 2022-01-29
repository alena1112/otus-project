package com.otusproject;

import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {
        List<String> example = Lists.newArrayList("one", "two", "three");
        example.forEach(System.out::println);
    }
}

package com.otusproject.justforfun;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeCopyMethodTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        List<String> list = new ArrayList<>();
        list.add("test");

        Test test = new Test(list);
        Test clone = test.clone();

        clone.list.add("test 2");

        System.out.println("Если хотим использовать метод клон, то обязательно класс должен реализовывать " +
                "интерфейс-маркер Cloneable");
        System.out.println("Если не реализует, то будет ошибка CloneNotSupportedException");
        System.out.println("При изменении поля клонированного метода также изменится поле оригинального:");
        test.list.forEach(System.out::println);
    }

    private static class Test implements Cloneable {
        public List<String> list;

        public Test(List<String> list) {
            this.list = list;
        }

        @Override
        protected Test clone() throws CloneNotSupportedException {
            return (Test) super.clone();
        }
    }
}

package com.otusproject.justforfun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateElementsInHashMap {

        public static void main(String[] args) {
            Map<Iterable<String>, String> map = new HashMap<>();

            final List<String> key1 = new ArrayList<>();

            final List<String> key2 = new ArrayList<>();
            key2.add("World");

            map.put(key1, "Hello1");
            map.put(key2, "Hello2");

            key1.add("World");

            System.out.println("В мапе заранее создали элемент с ключом как коллекция из элемента World");
            System.out.println("Потом в мапу добавили еще один элемент с ключом пустой коллекцией");
            System.out.println("И после добавили в ключ элемент World");

            System.out.println(key1.hashCode());
            System.out.println(key2.hashCode());
            System.out.println(key1.equals(key2));

            System.out.println("И вместо удаления key1, удалился key2:");
            System.out.println(map.remove(key1));
        }
}

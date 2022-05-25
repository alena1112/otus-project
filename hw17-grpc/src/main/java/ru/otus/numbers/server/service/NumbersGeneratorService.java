package ru.otus.numbers.server.service;

import java.util.List;

public interface NumbersGeneratorService {
    List<Integer> generateNumbers(int firstValue, int lastValue);
}

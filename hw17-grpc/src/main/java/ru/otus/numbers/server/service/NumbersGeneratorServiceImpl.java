package ru.otus.numbers.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumbersGeneratorServiceImpl implements NumbersGeneratorService {
    private static final Logger log = LoggerFactory.getLogger(NumbersGeneratorServiceImpl.class);

    @Override
    public List<Integer> generateNumbers(int firstValue, int lastValue) {
        if (firstValue < 0 || lastValue < 0 || lastValue > 30 || firstValue >= lastValue) {
            log.error("Incorrect FirstValue/LastValue");
            throw new IllegalArgumentException("Incorrect FirstValue/LastValue");
        }

        return IntStream.range(firstValue, lastValue).boxed().collect(Collectors.toList());
    }
}

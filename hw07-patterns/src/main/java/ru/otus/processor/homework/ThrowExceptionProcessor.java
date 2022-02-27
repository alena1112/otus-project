package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ThrowExceptionProcessor implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ThrowExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.getDate().getSecond();
        if (second % 2 == 0) {
            throw new EvenSecondException("Even second is " + second);
        }
        return message.toBuilder().build();
    }
}

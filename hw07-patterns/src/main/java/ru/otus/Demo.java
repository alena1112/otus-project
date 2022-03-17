package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;
import ru.otus.processor.homework.SwapProcessor;
import ru.otus.processor.homework.ThrowExceptionProcessor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Demo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()),
                new SwapProcessor(),
                new ThrowExceptionProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});

        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(Arrays.asList("test1", "test2"));
        long messageId = 1L;
        var message = new Message.Builder(messageId)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        Optional<Message> messageById = historyListener.findMessageById(messageId);
        System.out.println("found message: " + messageById.orElse(null));

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);
    }
}

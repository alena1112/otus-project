package ru.otus.numbers.client;

import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.numbers.generated.NumberMessage;
import ru.otus.numbers.generated.NumbersRange;
import ru.otus.numbers.generated.RemoteNumbersServiceGrpc;

public class NumbersClient {
    private final Logger log = LoggerFactory.getLogger(NumbersClient.class);
    private final Channel channel;

    private volatile int lastValue = 0;
    private volatile boolean isError = false;
    private final Object monitor = new Object();

    public NumbersClient(Channel channel) {
        this.channel = channel;
    }

    public void runClient() throws InterruptedException {
        createStub();

        int currentValue = 0;
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1000);
            if (isError) {
                break;
            }
            synchronized (monitor) {
                currentValue += lastValue + 1;
                lastValue = 0;
            }
            log.info("CurrentValue: {}", currentValue);
        }
    }

    private void createStub() {
        var newStub = RemoteNumbersServiceGrpc.newStub(channel);
        newStub.generateNumbers(createNumbersRange(), new StreamObserver<>() {
            @Override
            public void onNext(NumberMessage value) {
                synchronized (monitor) {
                    lastValue = value.getNumber();
                }
                log.info("New Value: {}", lastValue);
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage());
                isError = true;
            }

            @Override
            public void onCompleted() {
                log.info("NumbersClient is over");
            }
        });
    }

    private NumbersRange createNumbersRange() {
        return NumbersRange.newBuilder().setFirstValue(0).setLastValue(30).build();
    }
}

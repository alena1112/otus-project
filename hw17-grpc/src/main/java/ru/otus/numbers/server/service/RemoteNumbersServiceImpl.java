package ru.otus.numbers.server.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.numbers.generated.NumberMessage;
import ru.otus.numbers.generated.NumbersRange;
import ru.otus.numbers.generated.RemoteNumbersServiceGrpc;

import java.util.List;

public class RemoteNumbersServiceImpl extends RemoteNumbersServiceGrpc.RemoteNumbersServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(RemoteNumbersServiceImpl.class);
    private final NumbersGeneratorService numbersGeneratorService;

    public RemoteNumbersServiceImpl(NumbersGeneratorService numbersGeneratorService) {
        this.numbersGeneratorService = numbersGeneratorService;
    }

    @Override
    public void generateNumbers(NumbersRange request, StreamObserver<NumberMessage> responseObserver) {
        log.info("Received new message from client with range({}; {})", request.getFirstValue(), request.getLastValue());
        try {
            List<Integer> numbers = numbersGeneratorService.generateNumbers(
                    request.getFirstValue(), request.getLastValue());
            numbers.forEach(number -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                responseObserver.onNext(numberToNumberMessage(number));
                log.info("Generated and sent new number: {}", number);
            });
            responseObserver.onCompleted();
        } catch (IllegalArgumentException exc) {
            responseObserver.onError(exc);
        }
    }

    private NumberMessage numberToNumberMessage(Integer number) {
        return NumberMessage.newBuilder()
                .setNumber(number)
                .build();
    }
}

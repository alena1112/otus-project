package ru.otus.numbers.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.numbers.server.service.NumbersGeneratorServiceImpl;
import ru.otus.numbers.server.service.RemoteNumbersServiceImpl;

import java.io.IOException;

import static ru.otus.numbers.core.Params.SERVER_PORT;

public class NumbersServer {
    private static final Logger log = LoggerFactory.getLogger(NumbersServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        var numbersGeneratorService = new NumbersGeneratorServiceImpl();
        var remoteNumbersService = new RemoteNumbersServiceImpl(numbersGeneratorService);

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteNumbersService)
                .build();
        server.start();

        log.info("NumbersServer waiting for client connections...");
        server.awaitTermination();
    }
}

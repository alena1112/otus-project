package ru.otus.numbers.client;

import io.grpc.ManagedChannelBuilder;

import static ru.otus.numbers.core.Params.SERVER_HOST;
import static ru.otus.numbers.core.Params.SERVER_PORT;

public class ClientMain {

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var numbersClient = new NumbersClient(channel);
        numbersClient.runClient();

        channel.shutdown();
    }
}

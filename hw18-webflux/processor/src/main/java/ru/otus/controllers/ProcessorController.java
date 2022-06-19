package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.controllers.dto.ClientDto;
import ru.otus.crm.dao.ClientDao;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ProcessorController {
    private static final Logger log = LoggerFactory.getLogger(ProcessorController.class);

    private final ClientDao clientDao;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public ProcessorController(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @GetMapping(value = "/api/client", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<List<ClientDto>> getAllClients() {
        log.info("request to get all clients");

        var future = CompletableFuture
                .supplyAsync(() -> clientDao.findAllClients(), executor);
        return Mono.fromFuture(future);
    }

    @PostMapping(value = "/api/client", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<String> saveClient(@RequestParam("name") String name,
                                   @RequestParam("address") String address,
                                   @RequestParam("phone") String phone) {
        log.info("request to save a client");

        var future = CompletableFuture
                .supplyAsync(() -> {
                            Client client = clientDao.saveClient(new ClientDto(name, address, phone));
                            return client != null ? String.valueOf(client.getId()) : "";
                        },
                        executor);
        return Mono.fromFuture(future);
    }
}

package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.dto.ClientDto;

import java.util.List;

@RestController
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final WebClient client;

    public ClientController(WebClient.Builder builder) {
        this.client = builder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @GetMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ClientDto>> getAllClients() {
        log.info("request to get all clients");

        return client.get().uri("/api/client")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ClientDto>>() {})
                .doOnNext(val -> log.info("val:{}", val));
    }

    @PostMapping("/api/client")
    public Mono<String> saveClient(@RequestBody ClientDto clientDto) {
        log.info("request to create a client");

        return client.post().uri(String.format("/api/client?name=%s&address=%s&phone=%s",
                        clientDto.getName(), clientDto.getAddress(), clientDto.getPhone()))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(val -> log.info("new client with id:{}", val));
    }
}


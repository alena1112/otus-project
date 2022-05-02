package ru.otus.crm.service;

import ru.otus.controllers.ClientDto;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {

    Client saveClient(ClientDto client);

    Optional<Client> getClient(long id);

    List<ClientDto> findAll();

    void removeClient(Client client);
}

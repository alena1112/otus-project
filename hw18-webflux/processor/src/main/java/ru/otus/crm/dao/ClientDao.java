package ru.otus.crm.dao;

import ru.otus.controllers.dto.ClientDto;
import ru.otus.crm.model.Client;

import java.util.List;

public interface ClientDao {
    Client saveClient(ClientDto client);
    List<ClientDto> findAllClients();
}

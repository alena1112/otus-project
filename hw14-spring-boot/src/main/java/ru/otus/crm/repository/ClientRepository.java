package ru.otus.crm.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.controllers.ClientDto;
import ru.otus.crm.model.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query("select c.id, c.name, a.street as address, p.number as phone " +
            "from client c " +
            "left join address a on c.address_id = a.id " +
            "left join phone p on p.client_id = c.id")
    List<ClientDto> findAllClients();
}

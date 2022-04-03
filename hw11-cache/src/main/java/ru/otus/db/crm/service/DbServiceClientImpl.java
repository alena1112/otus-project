package ru.otus.db.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cashe.HwCache;
import ru.otus.cashe.HwListener;
import ru.otus.cashe.MyCache;
import ru.otus.db.core.repository.DataTemplate;
import ru.otus.db.core.sessionmanager.TransactionRunner;
import ru.otus.db.crm.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<String, Client> cache = new MyCache<>();

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache.addListener(new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
    }

    @Override
    public Client saveClient(Client client) {
        Client clientFromCash = getClientFromCash(client.getId());
        if (clientFromCash == null) {
            return transactionRunner.doInTransaction(connection -> {
                if (client.getId() == null) {
                    var clientId = dataTemplate.insert(connection, client);
                    var createdClient = new Client(clientId, client.getName());
                    log.info("created client: {}", createdClient);
                    cache.put(String.valueOf(createdClient.getId()), createdClient);
                    return createdClient;
                }
                dataTemplate.update(connection, client);
                log.info("updated client: {}", client);
                cache.put(String.valueOf(client.getId()), client);
                return client;
            });
        } else {
            return transactionRunner.doInTransaction(connection -> {
                dataTemplate.update(connection, client);
                log.info("updated client: {}", client);
                cache.put(String.valueOf(client.getId()), client);
                return client;
            });
        }
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client clientFromCash = getClientFromCash(id);
        if (clientFromCash != null) {
            return Optional.of(clientFromCash);
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(c -> cache.put(String.valueOf(c.getId()), c));
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientIdList = dataTemplate.findAll(connection);
            List<Client> clients = new ArrayList<>();
            for (Long id : clientIdList) {
                Client clientFromCash = getClientFromCash(id);
                if (clientFromCash != null) {
                    clients.add(clientFromCash);
                } else {
                    var clientOptional = dataTemplate.findById(connection, id);
                    log.info("client: {}", clientOptional);
                    clientOptional.ifPresent(c -> {
                        cache.put(String.valueOf(c.getId()), c);
                        clients.add(c);
                    });
                }
            }
            log.info("clientList:{}", clients);
            return clients;
       });
    }

    private Client getClientFromCash(Long id) {
        return id != null ? cache.get(String.valueOf(id)) : null;
    }
}

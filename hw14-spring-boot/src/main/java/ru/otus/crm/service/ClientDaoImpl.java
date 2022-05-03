package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.controllers.ClientDto;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.repository.AddressRepository;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.crm.repository.PhoneRepository;
import ru.otus.crm.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientDaoImpl implements ClientDao {
    private static final Logger log = LoggerFactory.getLogger(ClientDaoImpl.class);

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final TransactionManager transactionManager;

    public ClientDaoImpl(TransactionManager transactionManager, ClientRepository clientRepository,
                         AddressRepository addressRepository, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Client saveClient(ClientDto client) {
        return transactionManager.doInTransaction(() -> {
            Client savedClient;

            if (client.getAddress() != null) {
                Address savedAddress = addressRepository.save(new Address(client.getAddress()));
                savedClient = clientRepository.save(new Client(client.getName(), savedAddress.getId()));
            } else {
                savedClient = clientRepository.save(new Client(client.getName()));
            }

            if (client.getPhone() != null && !client.getPhone().isEmpty()) {
                List<Phone> phones = client.getPhonesAsList().stream().map(number -> new Phone(number, savedClient.getId()))
                        .collect(Collectors.toList());
                phoneRepository.saveAll(phones);
            }

            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInTransaction(() -> {
            var clientOptional = clientRepository.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<ClientDto> findAll() {
        return transactionManager.doInTransaction(() -> {
            var clientList = clientRepository.findAllClients();
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }

    @Override
    public void removeClient(Client client) {
        transactionManager.doInTransaction(() -> {
            clientRepository.delete(client);
            return client;
        });
    }
}

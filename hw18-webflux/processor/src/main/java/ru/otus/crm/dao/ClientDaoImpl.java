package ru.otus.crm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.controllers.dto.ClientDto;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.repository.AddressRepository;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.crm.repository.PhoneRepository;
import ru.otus.crm.sessionmanager.TransactionManager;

import java.util.List;
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
    public List<ClientDto> findAllClients() {
        return transactionManager.doInTransaction(() -> {
            var clients = clientRepository.findAllClients();
            log.info("clients: {}", clients);
            return clients;
        });
    }
}

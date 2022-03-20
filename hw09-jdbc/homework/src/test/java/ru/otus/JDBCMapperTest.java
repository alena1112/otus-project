package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class JDBCMapperTest {
    private TransactionRunnerJdbc transactionRunner;
    private DbExecutorImpl dbExecutor;

    @Container
    private final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("initial_schema.sql", "/docker-entrypoint-initdb.d/initial_schema.sql", BindMode.READ_ONLY);

    @BeforeEach
    public void setUp() {
        var dataSource = new DriverManagerDataSource(postgresqlContainer.getJdbcUrl(), postgresqlContainer.getUsername(),
                postgresqlContainer.getPassword());
        transactionRunner = new TransactionRunnerJdbc(dataSource);
        dbExecutor = new DbExecutorImpl();
    }

    @Test
    public void testClient() {
        var entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        var entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        final String clientName = "client";
        Client client = dbServiceClient.saveClient(new Client(clientName));
        assertNotNull(client);
        assertNotNull(client.getId());
        assertEquals(clientName, client.getName());

        final Long id = client.getId();
        final String clientNewName = "client1";
        client.setName(clientNewName);
        client = dbServiceClient.saveClient(client);
        assertEquals(id, client.getId());
        assertEquals(clientNewName, client.getName());

        client = dbServiceClient.getClient(client.getId()).orElse(null);
        assertNotNull(client);

        dbServiceClient.saveClient(new Client("client2"));

        List<Client> clients = dbServiceClient.findAll();
        assertEquals(2, clients.size());
    }

    @Test
    public void testManager() {
        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);

        final String label = "label";
        Manager manager = dbServiceManager.saveManager(new Manager(label));
        assertNotNull(manager);
        assertNotNull(manager.getNo());
        assertEquals(label, manager.getLabel());

        final Long no = manager.getNo();
        final String param = "param";
        manager.setParam1(param);
        manager = dbServiceManager.saveManager(manager);
        assertEquals(no, manager.getNo());
        assertEquals(param, manager.getParam1());

        manager = dbServiceManager.getManager(manager.getNo()).orElse(null);
        assertNotNull(manager);

        dbServiceManager.saveManager(new Manager("label2"));

        List<Manager> managers = dbServiceManager.findAll();
        assertEquals(2, managers.size());
    }
}

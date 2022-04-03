package ru.otus.db.crm.repository;

import ru.otus.db.core.repository.DataTemplate;
import ru.otus.db.core.repository.DataTemplateException;
import ru.otus.db.core.repository.executor.DbExecutor;
import ru.otus.db.crm.model.Client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ClientDataTemplateJdbc implements DataTemplate<Client> {

    private final DbExecutor dbExecutor;

    public ClientDataTemplateJdbc(DbExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<Client> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, "select id, name from client where id  = ?", List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return new Client(rs.getLong("id"), rs.getString("name"));
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<Long> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, "select id from client", Collections.emptyList(), rs -> {
            var clientIdList = new ArrayList<Long>();
            try {
                while (rs.next()) {
                    clientIdList.add(rs.getLong("id"));
                }
                return clientIdList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, Client client) {
        try {
            return dbExecutor.executeStatement(connection, "insert into client(name) values (?)",
                    Collections.singletonList(client.getName()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, Client client) {
        try {
            dbExecutor.executeStatement(connection, "update client set name = ? where id = ?",
                    List.of(client.getName(), client.getId()));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}

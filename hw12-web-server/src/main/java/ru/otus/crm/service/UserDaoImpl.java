package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.User;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public UserDaoImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var userOptional = userDataTemplate.findByEntityField(session, "login", login);
            log.info("client: {}", userOptional);
            return Optional.ofNullable(userOptional.size() > 0 ? userOptional.get(0) : null);
        });
    }
}

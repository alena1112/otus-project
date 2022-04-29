package ru.otus.crm.service;

import ru.otus.crm.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login);
}

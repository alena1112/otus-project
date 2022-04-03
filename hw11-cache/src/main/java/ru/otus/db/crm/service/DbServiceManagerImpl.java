package ru.otus.db.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cashe.HwCache;
import ru.otus.cashe.HwListener;
import ru.otus.cashe.MyCache;
import ru.otus.db.core.repository.DataTemplate;
import ru.otus.db.core.sessionmanager.TransactionRunner;
import ru.otus.db.crm.model.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<String, Manager> cache = new MyCache<>();

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache.addListener(new HwListener<String, Manager>() {
            @Override
            public void notify(String key, Manager value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        });
    }

    @Override
    public Manager saveManager(Manager manager) {
        Manager managerFromCash = getManagerFromCash(manager.getNo());
        if (managerFromCash == null) {
            return transactionRunner.doInTransaction(connection -> {
                if (manager.getNo() == null) {
                    var managerNo = managerDataTemplate.insert(connection, manager);
                    var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                    log.info("created manager: {}", createdManager);
                    cache.put(String.valueOf(createdManager.getNo()), createdManager);
                    return createdManager;
                }
                managerDataTemplate.update(connection, manager);
                log.info("updated manager: {}", manager);
                cache.put(String.valueOf(manager.getNo()), manager);
                return manager;
            });
        } else {
            return transactionRunner.doInTransaction(connection -> {
                managerDataTemplate.update(connection, manager);
                log.info("updated manager: {}", manager);
                cache.put(String.valueOf(manager.getNo()), manager);
                return manager;
            });
        }
    }

    @Override
    public Optional<Manager> getManager(long no) {
        Manager managerFromCash = getManagerFromCash(no);
        if (managerFromCash != null) {
            return Optional.of(managerFromCash);
        }
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            managerOptional.ifPresent(m -> cache.put(String.valueOf(m.getNo()), m));
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerNoList = managerDataTemplate.findAll(connection);
            List<Manager> managers = new ArrayList<>();
            for(Long no: managerNoList) {
                Manager managerFromCash = getManagerFromCash(no);
                if (managerFromCash != null) {
                    managers.add(managerFromCash);
                } else {
                    var managerOptional = managerDataTemplate.findById(connection, no);
                    log.info("manager: {}", managerOptional);
                    managerOptional.ifPresent(m -> {
                        cache.put(String.valueOf(m.getNo()), m);
                        managers.add(m);
                    });
                }
            }
            log.info("managerList:{}", managers);
            return managers;
       });
    }

    private Manager getManagerFromCash(Long no) {
        return no != null ? cache.get(String.valueOf(no)) : null;
    }
}

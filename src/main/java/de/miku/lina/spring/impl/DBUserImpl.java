package de.miku.lina.spring.impl;

import de.miku.lina.spring.repo.DBUserRepository;
import org.springframework.stereotype.Service;

@Service
public class DBUserImpl {
    private final DBUserRepository repository;

    public DBUserImpl(DBUserRepository repository) {
        this.repository = repository;
    }

    public DBUserRepository getDbUserRepository() {
        return repository;
    }

}

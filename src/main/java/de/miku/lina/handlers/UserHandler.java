package de.miku.lina.handlers;

import de.miku.lina.spring.entity.DBUser;
import de.miku.lina.spring.repo.DBUserRepository;
import de.miku.lina.utils.Logging;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserHandler implements Runnable {

    private DBUserRepository repository;
    private Map<String, DBUser> dbUserMap = new HashMap<>();
    private boolean saving = true;

    public UserHandler(DBUserRepository repository) {
        this.repository = repository;
    }

    public void insertUser(User user) {
        getUser(user);
    }

    public DBUser getUser(User user) {
        if (dbUserMap.containsKey(user.getId())) return dbUserMap.get(user.getId());
        Logging.info(UserHandler.class, "Get user with id " + user.getId());
        DBUser dbUser;
        if (!repository.existsById(user.getId())) {
            Logging.info("User %s not exists, create new user".formatted(user.getId()));
            dbUser = new DBUser(user.getId(), user.getName(), LocalDateTime.now());
            repository.save(dbUser);

        } else {
            dbUser= repository.getById(user.getId());
            Logging.info(dbUser.getDescription());
        }
        dbUserMap.put(user.getId(), dbUser);
        return dbUser;
    }

    public void saveUser(DBUser user) {
        repository.save(user);
    }

    public void save() {
        Logging.info(UserHandler.class, "Start saving users");
        List<DBUser> users = dbUserMap.values().stream().collect(Collectors.toList());
        repository.saveAll(users);
        Logging.info(UserHandler.class, "Users successfully saving");
    }


    @Override
    public void run() {
        while (saving) {
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            save();
        }
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }
}

package de.miku.lina.spring;

import de.miku.lina.spring.repo.DBUserRepository;
import de.miku.lina.utils.DataShare;

public class SpringRepos {

    public static void saveAll() {
        DataShare.userHandler.save();
    }

    public static DBUserRepository dbUserRepository;

}

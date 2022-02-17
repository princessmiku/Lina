package de.miku.lina.spring.repo;

import de.miku.lina.spring.entity.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DBUserRepository extends JpaRepository<DBUser, String> {


}

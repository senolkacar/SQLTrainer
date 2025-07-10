package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DatabaseRepository extends JpaRepository<Database, Long> {
    Optional<Database> findByName(String name);
}

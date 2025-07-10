package com.senolkacar.sqltrainer.repository;
import com.senolkacar.sqltrainer.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Method to find a teacher by their pseudo
    Optional<Teacher> findByPseudo(String pseudo);

    // Method to check if a teacher exists by their pseudo
    boolean existsByPseudo(String pseudo);

    // Method to check if a teacher exists by their email
    boolean existsByEmail(String email);
}

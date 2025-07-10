package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

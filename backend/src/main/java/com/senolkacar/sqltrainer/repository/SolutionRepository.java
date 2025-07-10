package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    // Additional query methods can be defined here if needed
}

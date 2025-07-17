package com.senolkacar.sqltrainer.repository;

import com.senolkacar.sqltrainer.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Integer> {
    @Query("SELECT s FROM Solution s " +
            "WHERE s.id = :id")
    Optional<Solution> findSolutionById(@Param("id") int id);

    @Query("DELETE FROM Solution s " +
            "WHERE s.id = :id")
    void deleteSolutionById(@Param("id") int id);

    boolean existsByQuestionIdAndOrderAndIdNot(Long questionId, Integer order, Long id);
}
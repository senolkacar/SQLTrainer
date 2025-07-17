package com.senolkacar.sqltrainer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import com.senolkacar.sqltrainer.validation.UniqueSolutionOrder;

@UniqueSolutionOrder
@Entity
@Table(name = "solutions")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_num")
    private Integer order;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "The sql cannot be empty (excluding whitespace).")
    private String sql;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    // Constructors
    public Solution() {}

    public Solution(String sql, Long questionId, Integer order) {
        this.sql = sql;
        this.questionId = questionId;
        this.order = order;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
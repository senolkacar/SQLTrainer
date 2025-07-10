package com.senolkacar.sqltrainer.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private boolean isPublished = false;
    private boolean isClosed = false;
    private boolean isTest = false;

    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "database_id")
    private Database database;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Attempt> attempts = new HashSet<>();

    @Transient
    private String statut = "";

    @Transient
    private String evaluation = "";

    public Database getDatabase() {
        return database;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Attempt> getAttempts() {
        return attempts;
    }

    public void setAttempts(Set<Attempt> attempts) {
        this.attempts = attempts;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getStatus(User user) {
        if (this.isClosed || (this.endDate != null && this.endDate.isBefore(OffsetDateTime.now()))) {
            return "CLOTURE";
        } else {
            Optional<Attempt> lastAttempt = attempts.stream()
                    .filter(a -> a.getStudent() != null && a.getStudent().getId().equals(user.getId()))
                    .reduce((first, second) -> second);
            if (lastAttempt.isPresent()) {
                Attempt attempt = lastAttempt.get();
                if (attempt.getStart() != null) {
                    return attempt.getFinish() == null ? "EN_COURS" : "FINI";
                }
            }
            return "PAS_COMMENCE";
        }
    }

    public String getEvaluation(User user) {
        String status = getStatus(user);
        if ("FINI".equals(status) || "CLOTURE".equals(status)) {
            Optional<Attempt> firstAttempt = attempts.stream()
                    .filter(a -> a.getStudent() != null && a.getStudent().getId().equals(user.getId()))
                    .findFirst();
            if (firstAttempt.isPresent()) {
                double score = firstAttempt.get().getScore(user);
                int questionCount = questions.size();
                if (questionCount > 0) {
                    double percentage = (score / questionCount) * 100;
                    return (percentage / 10) + "/10";
                }
            }
        }
        return "N/A";
    }
}
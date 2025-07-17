package com.senolkacar.sqltrainer.dto;

import java.time.OffsetDateTime;

public class QuizDTO {
    private int id;
    private String name;
    private String description;
    private boolean isPublished;
    private boolean isClosed;
    private boolean isTest;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    private String Statut;
    private String Evaluation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getStatut() {
        return Statut;
    }

    public void setStatut(String statut) {
        Statut = statut;
    }

    public String getEvaluation() {
        return Evaluation;
    }

    public void setEvaluation(String evaluation) {
        Evaluation = evaluation;
    }
}

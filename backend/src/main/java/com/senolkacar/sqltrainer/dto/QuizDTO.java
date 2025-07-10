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
}

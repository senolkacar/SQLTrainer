package com.senolkacar.sqltrainer.dto;

import java.time.OffsetDateTime;

public class AnswerDTO {
    private int id;
    private String sql;
    private OffsetDateTime timestamp;
    private boolean isCorrect;
    private int AttemptId;
    private int QuestionId;
}

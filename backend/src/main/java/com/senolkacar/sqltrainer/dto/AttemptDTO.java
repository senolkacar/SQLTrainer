package com.senolkacar.sqltrainer.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttemptDTO {
    private int id;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private int quizId;
    private int studentId;
    private List<AnswerDTO> answers = new ArrayList<>();
}

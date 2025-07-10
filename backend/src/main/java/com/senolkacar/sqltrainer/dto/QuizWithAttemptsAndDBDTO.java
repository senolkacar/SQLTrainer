package com.senolkacar.sqltrainer.dto;

import java.util.ArrayList;
import java.util.List;

public class QuizWithAttemptsAndDBDTO extends QuizDTO {
    private DatabaseDTO database;
    private List<AttemptDTO> attempts = new ArrayList<>();
    private List<QuestionDTO> questions = new ArrayList<>();
}

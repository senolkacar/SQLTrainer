package com.senolkacar.sqltrainer.dto;

import java.util.ArrayList;
import java.util.List;

public class QuizWithAttemptsDTO extends QuizDTO{
    private List<AttemptDTO> attempts = new ArrayList<>();
}

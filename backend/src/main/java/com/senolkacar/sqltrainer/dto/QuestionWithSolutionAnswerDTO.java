package com.senolkacar.sqltrainer.dto;

import java.util.ArrayList;
import java.util.List;

public class QuestionWithSolutionAnswerDTO extends QuestionDTO{
    private QuizWithAttemptsAndDBDTO quiz;
    private List<AnswerDTO> answers = new ArrayList<>();

    public QuizWithAttemptsAndDBDTO getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizWithAttemptsAndDBDTO quiz) {
        this.quiz = quiz;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}

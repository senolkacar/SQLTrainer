package com.senolkacar.sqltrainer.dto;

import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {
    private int id;
    private int order;
    private String body;
    private Integer previousQuestionId;
    private Integer nextQuestionId;
    private boolean hasAnswer = false;
    private AnswerDTO answer;
    private List<SolutionDTO> solutions = new ArrayList<>();

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Integer getPreviousQuestionId() { return previousQuestionId; }
    public void setPreviousQuestionId(Integer previousQuestionId) { this.previousQuestionId = previousQuestionId; }

    public Integer getNextQuestionId() { return nextQuestionId; }
    public void setNextQuestionId(Integer nextQuestionId) { this.nextQuestionId = nextQuestionId; }

    public boolean isHasAnswer() { return hasAnswer; }
    public void setHasAnswer(boolean hasAnswer) { this.hasAnswer = hasAnswer; }

    public AnswerDTO getAnswer() { return answer; }
    public void setAnswer(AnswerDTO answer) { this.answer = answer; }

    public List<SolutionDTO> getSolutions() { return solutions; }
    public void setSolutions(List<SolutionDTO> solutions) { this.solutions = solutions; }
}

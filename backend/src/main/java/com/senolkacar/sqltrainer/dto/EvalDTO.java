package com.senolkacar.sqltrainer.dto;

public class EvalDTO {
    private int questionId;
    private String query;

    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public int getQuestionId() {
        return questionId;
    }
}

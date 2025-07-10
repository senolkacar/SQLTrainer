package com.senolkacar.sqltrainer.entity;

import jakarta.persistence.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_num")
    private Integer order;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(name = "quiz_id")
    private Integer quizId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", insertable = false, updatable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Solution> solutions = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Answer> answers = new HashSet<>();

    @Transient
    private Integer previousQuestionId;

    @Transient
    private Integer nextQuestionId;

    @Transient
    private boolean hasAnswer = false;

    @Transient
    private Answer answer;

    public Query eval(String sql, String databaseName) {
        Query query = getData(sql, databaseName);
        validate(query);
        return query;
    }

    private void validate(Query query) {
        String solutionSQL = this.solutions.stream()
                .map(Solution::getSql)
                .findFirst()
                .orElse(null);

        if (solutionSQL == null) {
            query.addError("No solution found for this question");
            return;
        }

        Query solutionResult = getData(solutionSQL, this.quiz.getDatabase().getName());

        if (query.getErrors().isEmpty()) {
            if (solutionResult.getRowCount() != query.getRowCount()) {
                query.addError("\nbad number of rows");
            }
            if (solutionResult.getColumns().length != query.getColumns().length) {
                query.addError("\nbad number of columns");
            }
        }

        if (query.getErrors().isEmpty()) {
            List<String> listOfSolutionElements = new ArrayList<>();
            List<String> listOfAttemptElements = new ArrayList<>();

            for (int i = 0; i < solutionResult.getRowCount(); i++) {
                for (int j = 0; j < solutionResult.getColumns().length; j++) {
                    listOfSolutionElements.add(solutionResult.getData()[i][j]);
                }
            }

            for (int i = 0; i < query.getRowCount(); i++) {
                for (int j = 0; j < query.getColumns().length; j++) {
                    listOfAttemptElements.add(query.getData()[i][j]);
                }
            }

            Collections.sort(listOfAttemptElements);
            Collections.sort(listOfSolutionElements);

            if (!listOfAttemptElements.equals(listOfSolutionElements)) {
                query.addError("\nwrong data");
            }
        }
    }

    private Query getData(String sql, String databaseName) {
        Query query = new Query();
        query.setSql(sql);
        query.setRowCount(0);
        query.setColumns(new String[0]);
        query.setData(new String[0][]);
        query.setErrors(new ArrayList<>());

        String connectionUrl = String.format("jdbc:mysql://localhost:3306/%s?user=root&password=root", databaseName);

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {

            statement.execute("SET sql_mode = 'STRICT_ALL_TABLES'");
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                String[] columns = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columns[i - 1] = metaData.getColumnName(i);
                }
                query.setColumns(columns);

                List<String[]> dataRows = new ArrayList<>();
                while (resultSet.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
                        row[i - 1] = formatValue(value);
                    }
                    dataRows.add(row);
                }

                query.setRowCount(dataRows.size());
                query.setData(dataRows.toArray(new String[0][]));
            }

        } catch (SQLException e) {
            query.getErrors().add(e.getMessage());
        }

        return query;
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Timestamp) {
            LocalDateTime localDateTime = ((Timestamp) value).toLocalDateTime();
            if (localDateTime.toLocalTime().equals(java.time.LocalTime.MIDNIGHT)) {
                return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        }
        if (value instanceof java.util.Date) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) value);
        }
        return value.toString();
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public Set<Solution> getSolutions() { return solutions; }
    public void setSolutions(Set<Solution> solutions) { this.solutions = solutions; }

    public Set<Answer> getAnswers() { return answers; }
    public void setAnswers(Set<Answer> answers) { this.answers = answers; }

    public Integer getPreviousQuestionId() { return previousQuestionId; }
    public void setPreviousQuestionId(Integer previousQuestionId) { this.previousQuestionId = previousQuestionId; }

    public Integer getNextQuestionId() { return nextQuestionId; }
    public void setNextQuestionId(Integer nextQuestionId) { this.nextQuestionId = nextQuestionId; }

    public boolean isHasAnswer() { return hasAnswer; }
    public void setHasAnswer(boolean hasAnswer) { this.hasAnswer = hasAnswer; }

    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }

}
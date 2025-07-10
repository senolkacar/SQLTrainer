package com.senolkacar.sqltrainer.entity;

import jakarta.persistence.*;
import java.sql.*;
import java.util.*;

@Entity
@Table(name = "databases")
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "database", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Quiz> quizzes = new HashSet<>();

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<String> getTables(String dbName) {
        List<String> tables = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        String user = "root";
        String password = "root";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<String> getColumns(String dbName) {
        List<String> columns = new ArrayList<>();
        List<String> tables = getTables(dbName);
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        String user = "root";
        String password = "root";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            for (String table : tables) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM " + table)) {
                    while (rs.next()) {
                        columns.add(rs.getString("Field"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }
}
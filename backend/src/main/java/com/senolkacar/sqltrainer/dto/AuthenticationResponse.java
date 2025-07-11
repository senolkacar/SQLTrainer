package com.senolkacar.sqltrainer.dto;

import com.senolkacar.sqltrainer.entity.Role;
import com.senolkacar.sqltrainer.entity.User;

public class AuthenticationResponse {
    private String token;
    private String pseudo;
    private String email;
    private Role role;
    private Long userId;

    // Constructors
    public AuthenticationResponse() {}

    public AuthenticationResponse(String token, String pseudo, String email, Role role, Long userId) {
        this.token = token;
        this.pseudo = pseudo;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

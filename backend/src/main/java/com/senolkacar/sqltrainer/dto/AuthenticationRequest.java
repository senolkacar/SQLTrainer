package com.senolkacar.sqltrainer.dto;

public class AuthenticationRequest {
    private String pseudo;
    private String password;

    // Constructors
    public AuthenticationRequest() {}

    public AuthenticationRequest(String pseudo, String password) {
        this.pseudo = pseudo;
        this.password = password;
    }

    // Getters and Setters
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

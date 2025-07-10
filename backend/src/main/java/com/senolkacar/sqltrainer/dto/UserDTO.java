package com.senolkacar.sqltrainer.dto;

import com.senolkacar.sqltrainer.entity.Role;

import java.time.OffsetDateTime;

public class UserDTO {
    private int id;
    private String pseudo;
    private String email;
    private String firstname;
    private String lastname;
    private OffsetDateTime birthdate;
    private Role role;
    private String token;
}

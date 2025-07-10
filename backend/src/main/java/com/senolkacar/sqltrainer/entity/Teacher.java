package com.senolkacar.sqltrainer.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends User {

    public Teacher() {
        this.setRole(Role.Teacher);
    }
}
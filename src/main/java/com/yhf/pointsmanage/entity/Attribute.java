package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class Attribute {
    int id;
    String username;
    String password;
    String email;
    String phone;
    int points;

    public Attribute(int id, String username, String password, String email, String phone, int points) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.points = points;
    }

    public Attribute() {

    }
}

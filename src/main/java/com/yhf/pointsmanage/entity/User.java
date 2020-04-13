package com.yhf.pointsmanage.entity;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private int id;
    private String userName;
    private String password;
    private List<Mall> bindMall;
    private String phone;
    private String userType;

    public User() {
    }

    public User(int id, String userName, String password, List<Mall> bindMall, String phone, String userType) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.bindMall = bindMall;
        this.phone = phone;
        this.userType = userType;
    }
}

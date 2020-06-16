package com.yhf.pointsmanage.entity;

import com.yhf.pointsmanage.tools.Base64Util;
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
    private String img;

    public User() {
    }

    public User(String userName, String password, String phone, String userType) {
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.userType = userType;
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

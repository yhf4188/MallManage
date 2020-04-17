package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class UserBindMall {
    int  userID;
    int  mallID;
    String userName;
    int points;

    public UserBindMall() {
    }

    public UserBindMall(int userID, int mallID, String userName) {
        this.userID = userID;
        this.mallID = mallID;
        this.userName = userName;
    }

    public UserBindMall(int userID, int mallID, String userName, int point) {
        this.userID = userID;
        this.mallID = mallID;
        this.userName = userName;
        this.points = point;
    }
}

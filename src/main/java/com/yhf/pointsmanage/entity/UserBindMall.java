package com.yhf.pointsmanage.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserBindMall {
    int id;
    int  userID;
    int  mallID;
    String userName;
    int points;
    String password;
    String email;
    String phone;
    String picture;
    List<Address> address;

    public UserBindMall() {
    }

    public UserBindMall(int userID, int mallID){
        this.userID = userID;
        this.mallID = mallID;
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

    public UserBindMall(int userID, int mallID, String userName, int points, String password, String email, String phone) {
        this.userID = userID;
        this.mallID = mallID;
        this.userName = userName;
        this.points = points;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public UserBindMall(int id,int userID, int mallID, String userName, int points, String password, String email, String phone,String picture) {
        this.id = id;
        this.userID = userID;
        this.mallID = mallID;
        this.userName = userName;
        this.points = points;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
    }
}

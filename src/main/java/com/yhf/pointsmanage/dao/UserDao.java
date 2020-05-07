package com.yhf.pointsmanage.dao;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.entity.UserBindMall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserDao {
    User getUserMall(@Param("userName") String userName, @Param("password") String password);

    String haveUserName(@Param("userName") String userName);

    String havePhone(@Param("phone") String phone);

    Boolean register(User user);

    User getUserMallByUserName(@Param("userName") String userName);

    Boolean setUserBind(UserBindMall userBindMall);

    ArrayList<UserBindMall> getUserBind(@Param("userID") int userID);
}

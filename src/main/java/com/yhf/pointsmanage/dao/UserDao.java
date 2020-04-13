package com.yhf.pointsmanage.dao;

import com.yhf.pointsmanage.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    User getUserMall(@Param("userName") String userName, @Param("password") String password);

    Boolean haveUserName(@Param("userName") String userName);

    Boolean havePhone(@Param("phone") String phone);

    Boolean register(User user);
}

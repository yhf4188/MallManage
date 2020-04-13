package com.yhf.pointsmanage.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yhf.pointsmanage.dao.UserDao;
import com.yhf.pointsmanage.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    //根据用户名密码进行账号查找
    public User getUserMall(String userName, String password) {
        try {
            User user = new User();
            user = userDao.getUserMall(userName, password);
            return user;
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    //注册
    public boolean register(User user)
    {
        try
        {
            boolean registerSuccess=userDao.register(user);
            return registerSuccess;
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    //查询手机是否注册
    public boolean havePhone(String phone)
    {
        try
        {
            return userDao.havePhone(phone);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    //查询用户名是否被注册
    public boolean haveUserName(String userName)
    {
        try
        {
            return userDao.haveUserName(userName);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }
}

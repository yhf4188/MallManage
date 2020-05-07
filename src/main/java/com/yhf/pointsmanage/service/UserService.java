package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yhf.pointsmanage.dao.UserDao;
import com.yhf.pointsmanage.entity.Attribute;
import com.yhf.pointsmanage.entity.Mall;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.entity.UserBindMall;
import com.yhf.pointsmanage.tools.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //根据用户名进行账号查找
    public User getUserMallByUserName(String userName) {
        try {
            User user = new User();
            user = userDao.getUserMallByUserName(userName);
            return user;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //注册
    public boolean register(User user) {
        try {
            User newUser= new User(user.getUserName(),user.getPassword(),user.getPhone(),user.getUserType());
            System.out.println(user.getUserName()+user.getPassword()+user.getPhone()+user.getUserType());
            boolean registerSuccess = userDao.register(user);
            return registerSuccess;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //查询手机是否注册
    public String havePhone(String phone) {
        try {
            return userDao.havePhone(phone);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //查询用户名是否被注册
    public String haveUserName(String userName) {
        try {
            return userDao.haveUserName(userName);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //添加用户商城绑定信息
    public boolean setUserBind(User user,Attribute attribute, Mall mall) throws IOException {
        try {
            String att_url = mall.getAttribution_impl();
            Map<String,Object> map=new HashMap<>();
            map.put("user",attribute);
            JSONObject attributionJson = JsonData.getJson(att_url,map);
            if(attributionJson.isEmpty())
            {return false;}
            JSONObject data = new JSONObject();
            Iterator it = attributionJson.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                data.put(entry.getKey(), entry.getValue());
            }
            JSONObject attributionJ = data.getJSONObject("user");
            UserBindMall userBindMall = new UserBindMall(user.getId(), mall.getId(), attributionJ.getString("username"), attributionJ.getInteger("points"),
                    attributionJ.getString("password"),attributionJ.getString("email"),attributionJ.getString("phone"));
            userDao.setUserBind(userBindMall);
            return userDao.setUserBind(userBindMall);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //获取绑定信息
    public List<UserBindMall> getUserBind(User user)
    {
        try
        {
            List<UserBindMall> userBindMalls=new ArrayList<>();
            userBindMalls=userDao.getUserBind(user.getId());
            return userBindMalls;
        }catch (RuntimeException e)
        {
            throw e;
        }
    }
}

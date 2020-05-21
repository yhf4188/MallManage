package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yhf.pointsmanage.dao.AddressDao;
import com.yhf.pointsmanage.dao.MallDao;
import com.yhf.pointsmanage.dao.UserDao;
import com.yhf.pointsmanage.entity.*;
import com.yhf.pointsmanage.tools.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MallDao mallDao;

    @Autowired
    private AddressDao addressDao;

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
            User newUser = new User(user.getUserName(), user.getPassword(), user.getPhone(), user.getUserType());
            System.out.println(user.getUserName() + user.getPassword() + user.getPhone() + user.getUserType());
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
    public boolean setUserBind(User user, Attribute attribute, Mall mall) throws IOException {
        try {
            mall = mallDao.getMall(mall.getId());
            String att_url = mall.getAttribution_impl();
            Map<String, Object> map = new HashMap<>();
            map.put("user", attribute);
            JSONObject attributionJson = JsonData.getJsonHaveReturn(att_url, map);
            if (attributionJson.isEmpty()) {
                return false;
            }
            JSONObject data = new JSONObject();
            Iterator it = attributionJson.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                data.put(entry.getKey(), entry.getValue());
            }
            JSONObject attributionJ = data.getJSONObject("user");
            if (attributionJ.isEmpty()) {
                return false;
            }
            UserBindMall userBindMall = new UserBindMall(user.getId(), mall.getId(), attributionJ.getString("username"), attributionJ.getInteger("points"),
                    attributionJ.getString("password"), attributionJ.getString("email"), attributionJ.getString("phone"));
            userDao.setUserBind(userBindMall);
            userBindMall=userDao.getUserBindByMall(user.getId(), mall.getId());
            Address address=new Address();
            JSONObject ad=attributionJ.getJSONObject("address");
            address.setAddress(ad.getString("address"));
            address.setOrigin_id(ad.getInteger("id"));
            address.setUser_mall_id(userBindMall.getId());
            addressDao.insertAddress(address);
            return true;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //获取绑定信息
    public List<UserBindMall> getUserBind(User user) {
        try {
            List<UserBindMall> userBindMalls = new ArrayList<>();
            userBindMalls = userDao.getUserBind(user.getId());
            return userBindMalls;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    //更新用户绑定数据
    public void updateUserBind(UserBindMall userBindMall) {
        Mall mall = mallDao.getMall(userBindMall.getMallID());
        String att_url = mall.getAttribution_impl();
        Map<String, Object> map = new HashMap<>();
        map.put("user", userBindMall);
        try {
            JSONObject attributionJson = JsonData.getJsonHaveReturn(att_url, map);
            if (attributionJson.isEmpty()) {
                return;
            }
            JSONObject data = new JSONObject();
            Iterator it = attributionJson.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                data.put(entry.getKey(), entry.getValue());
            }
            JSONObject attributionJ = data.getJSONObject("user");
            if (attributionJ.isEmpty()) {
                return;
            }
            userBindMall.setPoints(attributionJ.getInteger("points"));
            userDao.updateUserBindMall(userBindMall);
        } catch (RuntimeException e) {
            throw e;
        } catch (IOException e1) {
            e1.printStackTrace();
            log.error(e1.getMessage());
        }
    }
}

package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.dao.AddressDao;
import com.yhf.pointsmanage.dao.MallDao;
import com.yhf.pointsmanage.dao.UserDao;
import com.yhf.pointsmanage.entity.*;
import com.yhf.pointsmanage.exception.CustomizeException;
import com.yhf.pointsmanage.tools.Base64Util;
import com.yhf.pointsmanage.tools.HttpUtil;
import com.yhf.pointsmanage.tools.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MallDao mallDao;

    @Autowired
    private MallService mallService;

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
    public boolean register(User user) throws IOException {
        try {
            String base64 = user.getImg();
            base64 = base64.replaceAll(" ", "+");
            base64 = base64.replace("data:image/png;base64,", "");
            base64 = base64.replace("data:image/jpeg;base64,","");
            String filename = UUID.randomUUID() + ".jpg";
//            String savePath = "/home/imges/"+filename;
            String savePath = "D:/imgs/" + filename;
            //String urlPath = "http://139.155.79.45:8088/"+filename;
            String urlPath = "http://localhost:80/" + filename;
            String url = Base64Util.Base64ToImage(base64, savePath, urlPath);
            System.out.println(urlPath);
            User newUser = new User(user.getUserName(), user.getPassword(), user.getPhone(), user.getUserType());
            user.setImg(urlPath);
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
                throw new CustomizeException("绑定失败，请确认绑定信息的正确性");
            }
            JSONObject data = new JSONObject();
            Iterator it = attributionJson.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                data.put(entry.getKey(), entry.getValue());
            }
            JSONObject attributionJ = data.getJSONObject("user");
            if (attributionJ.isEmpty()) {
                throw new CustomizeException("未获取到用户信息");
            }
            UserBindMall userBindMall = new UserBindMall(user.getId(), mall.getId(), attributionJ.getString("username"), attributionJ.getInteger("points"),
                    attributionJ.getString("password"), attributionJ.getString("email"), attributionJ.getString("phone"));
            userDao.setUserBind(userBindMall);
            userBindMall = userDao.getUserBindByMall(user.getId(), mall.getId());
            Address address = new Address();
            JSONObject ad = attributionJ.getJSONObject("address");
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

    //设置应用绑定的积分商城
    public boolean setBindMall(User user, Mall mall) {
        try {
            User u = new User();
            boolean exist = true;
            exist = HttpUtil.exist(mall.getOrder_impl());
            if (exist == false)
                throw new CustomizeException("订单接口无法访问请确认接口是否填写错误");
            exist = HttpUtil.exist(mall.getConsume_impl());
            if (exist == false)
                throw new CustomizeException("消费接口无法访问请确认接口是否填写错误");
            exist = HttpUtil.exist(mall.getAttribution_impl());
            if (exist == false)
                throw new CustomizeException("用户信息接口无法访问请确认接口是否填写错误");
            exist = HttpUtil.exist(mall.getShop_impl());
            if (exist == true) {
                if (mallDao.getSameMall(mall).isEmpty()) {
                    mallDao.setMall(mall);
                    mall = mallDao.getLastMall();
                    UserBindMall userBindMall = new UserBindMall(user.getId(), mall.getId());
                    userDao.setUserBind(userBindMall);
                    mallService.getAllMall();
                } else {
                    throw new CustomizeException("商品接口无法访问请确认接口是否填写错误");
                }
            }
            return exist;
        } catch (Exception e) {
            throw e;
        }
    }

    //设置应用绑定的积分商城
    public boolean updateBindMall(Mall mallOld, Mall mallNew) throws IOException{
        try {
            boolean exist = true;
            if (!mallOld.getAttribution_impl().equals(mallNew.getAttribution_impl())) {
                exist = HttpUtil.exist(mallNew.getOrder_impl());
                if (exist == false)
                    throw new CustomizeException("用户信息接口无法访问请确认接口是否填写错误");
            }
            if (!mallOld.getConsume_impl().equals(mallNew.getConsume_impl())) {
                exist = HttpUtil.exist(mallNew.getConsume_impl());
                if (exist == false)
                    throw new CustomizeException("消费接口无法访问请确认接口是否填写错误");
            }
            if (!mallOld.getOrder_impl().equals(mallNew.getOrder_impl())) {
                exist = HttpUtil.exist(mallNew.getAttribution_impl());
                if (exist == false)
                    throw new CustomizeException("订单接口无法访问请确认接口是否填写错误");
            }
            if (!mallOld.getShop_impl().equals(mallNew.getShop_impl())) {
                exist = HttpUtil.exist(mallNew.getShop_impl());
                if(exist == false)
                    throw new CustomizeException("商品接口无法访问请确认接口是否填写错误");
            }
            if (exist == true) {
                if(!mallNew.getPic().equals(mallOld.getPic())) {
                    String base64 = mallNew.getPic();
                    base64 = base64.replaceAll(" ", "+");
                    base64 = base64.replace("data:image/png;base64,", "");
                    base64 = base64.replace("data:image/jpeg;base64,", "");
                    String oldPic = mallOld.getPic().replace("http://localhost:80/", "D:/imgs/");
                    File file = new File(oldPic);
                    boolean delete = file.delete();
                    if (delete == false) {
                        throw new CustomizeException("80001", "文件删除异常");
                    }
                    String filename = UUID.randomUUID() + ".jpg";
//              String savePath = "/home/imges/"+filename;
                    String savePath = "D:/imgs/" + filename;
                    //String urlPath = "http://139.155.79.45:8088/"+filename;
                    String urlPath = "http://localhost:80/" + filename;
                    String url = Base64Util.Base64ToImage(base64, savePath, urlPath);
                    mallNew.setPic(url);
                }
                mallDao.updateMall(mallNew);
                mallNew = mallDao.getLastMall();
                mallService.getAllMall();
            }
            return exist;
        } catch (Exception e) {
            throw e;
        }
    }

    public User setAddress(String address, int mallID, User user) throws IOException {
        UserBindMall userBindMall = userDao.getUserBindByMall(user.getId(), mallID);
        Mall mall = mallDao.getMall(mallID);
        Attribute attribute = new Attribute();
        attribute.setUsername(userBindMall.getUserName());
        attribute.setPassword(userBindMall.getPassword());
        attribute.setEmail(userBindMall.getEmail());
        attribute.setPhone(userBindMall.getPhone());
        Map map = new ConcurrentHashMap();
        map.put("user", attribute);
        map.put("address", address);
        JSONObject attributionJson = JsonData.getJsonHaveReturn(mall.getAttribution_impl(), map);
        JSONObject data = new JSONObject();
        Iterator it = attributionJson.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            data.put(entry.getKey(), entry.getValue());
        }
        JSONObject attributionJ = data.getJSONObject("address");
        Address addresses = new Address();
        addresses.setAddress(attributionJson.getJSONObject("address").getString("address"));
        addresses.setOrigin_id(attributionJson.getJSONObject("address").getInteger("id"));
        addresses.setUser_mall_id(userBindMall.getId());
        addressDao.insertAddress(addresses);
        user = userDao.getUserMall(user.getUserName(), user.getPassword());
        return user;
    }

    public User setAddress(Address address, int mallID, User user) throws IOException {
        UserBindMall userBindMall = userDao.getUserBindByMall(user.getId(), mallID);
        Mall mall = mallDao.getMall(mallID);
        Attribute attribute = new Attribute();
        attribute.setUsername(userBindMall.getUserName());
        attribute.setPassword(userBindMall.getPassword());
        attribute.setEmail(userBindMall.getEmail());
        attribute.setPhone(userBindMall.getPhone());
        Map map = new ConcurrentHashMap();
        map.put("user", attribute);
        map.put("address_id", address.getOrigin_id());
        JSONObject attributionJson = JsonData.getJsonHaveReturn(mall.getAttribution_impl(), map);
        JSONObject data = new JSONObject();
        Iterator it = attributionJson.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            data.put(entry.getKey(), entry.getValue());
        }
        JSONObject attributionJ = data.getJSONObject("user");
        addressDao.updateDefaultAddress(address);
        addressDao.cancelDefaultAddress(address);
        user = userDao.getUserMall(user.getUserName(), user.getPassword());
        return user;
    }
}

package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.dao.GoodsDao;
import com.yhf.pointsmanage.dao.MallDao;
import com.yhf.pointsmanage.dao.UserDao;
import com.yhf.pointsmanage.entity.*;
import com.yhf.pointsmanage.tools.InterfaceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class OrderService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private MallDao mallDao;

    public List<Order> getOrder(User user)
    {
        List<Order> orders=new ArrayList<>();
        try
        {
            List<UserBindMall> userBindMalls=userDao.getUserBind(user.getId());
            ExecutorService exc = Executors.newFixedThreadPool(userBindMalls.size());
            List<Future<JSONObject>> futures = new ArrayList<Future<JSONObject>>();
            for(int i=0; i<userBindMalls.size();i++)
            {
                Mall mall=mallDao.getMall(userBindMalls.get(i).getMallID());
                String url=mall.getOrder_impl();
                Attribute attribute=new Attribute(0,userBindMalls.get(i).getUserName(),userBindMalls.get(i).getPassword(),
                        userBindMalls.get(i).getEmail(),userBindMalls.get(i).getPhone(),userBindMalls.get(i).getPoints());
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("user",attribute);
                jsonObject.put("url",url);
                jsonObject.put("mallID",mall.getId());
                InterfaceRequest call=new InterfaceRequest(jsonObject);
                Future< JSONObject> future = exc.submit(call);
                //将每个线程放入线程集合， 这里如果任何一个线程的执行结果没有回调，线程都会自动堵塞
                futures.add(future);
            }
            for (Future< JSONObject> future : futures) {
                JSONObject json = future.get();
                Integer mallID=json.getInteger("mallID");
                JSONArray jsonArray= json.getJSONObject("returnJson").getJSONArray("order");
                for(int i=0;i<jsonArray.size();i++)
                {
                    Order order=new Order();
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Address address=new Address();
                    address.setOrigin_id(jsonObject.getJSONObject("address").getInteger("id"));
                    address.setAddress(jsonObject.getJSONObject("address").getString("address"));
                    order.setAddress(address);
                    Goods goods = goodsDao.getGoodsByMallIDAndInMall(mallID,jsonObject.getInteger("goods_id"));
                    order.setGoods(goods);
                    order.setUser(user);
                    order.setOrigin_id(jsonObject.getInteger("id"));
                    order.setState(jsonObject.getString("goods_state"));
                    orders.add(order);
                }
            }
            exc.shutdown();
        }catch (Exception e)
        {
            throw e;
        }finally {
            return orders;
        }
    }

}

package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.dao.GoodsDao;
import com.yhf.pointsmanage.dao.MallDao;
import com.yhf.pointsmanage.dao.UserDao;
import com.yhf.pointsmanage.entity.Goods;
import com.yhf.pointsmanage.entity.Mall;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.tools.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import com.yhf.pointsmanage.constant.Constant;

@Slf4j
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MallDao mallDao;

    //导入商品数据
    public int setGoods() {
        List<Mall> malls = new ArrayList<>();
        malls = mallDao.getAllMall();
        Map<String, String> inMallID = new HashMap<>();
        inMallID = mallDao.getInMallID();
        List<Goods> goods = new ArrayList<>();
        try {
            //分商城导入商品数据
            for (Mall mall : malls) {
                String url = mall.getShop_impl();
                JSONObject goodsJson = JsonData.getJson(url);
                JSONObject data = new JSONObject();
                Iterator it = goodsJson.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                    data.put(entry.getKey(), entry.getValue());
                }
                //将data中数据转存到List中
                for (String key : data.keySet()) {
                    System.out.println(data.get(key));
                    JSONObject goodJ = data.getJSONObject(key);
                    String inMallId = goodJ.getString("name") + goodJ.getInteger("id");
                    if (!inMallID.containsKey(inMallId)) {
                        Goods good = new Goods(goodJ.getString("name"), goodJ.getInteger("points"), goodJ.getString("picture")
                                , goodJ.getInteger("goods_num"), 0, goodJ.getInteger("goods_classf"), inMallId);
                        goods.add(good);
                    }
                }
            }
            boolean in_su = goodsDao.insertList(goods);
            if (in_su) {
                return Constant.SUCCESS;
            } else
                return Constant.FAILURE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constant.EXCEPTION;
        }
    }

    //获取所有商品数据
    public List<Goods> getAllGoods(String userName) {
        List<Mall> malls = new ArrayList<>();
        malls = mallDao.getMallImpl(userName);
        User user = userDao.getUserMallByUserName(userName);
        List<Goods> goods = new ArrayList<>();
        try {
            for (Mall mall : malls) {
                goods.addAll(goodsDao.getGoodsByMallIdAndUser(mall.getId(),user.getId()));
            }
            return goods;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    //根据商品分类输出数据
    public List<Goods> getGoodsByMallId(int mallId) {
        List<Goods> goods = new ArrayList<>();
        try {
            goods = goodsDao.getGoodsByMallId(mallId);
            return goods;
        }catch (Exception e)
        {
            log.error(e.getMessage());
            return null;
        }
    }
}

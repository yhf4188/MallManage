package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.dao.GoodsDao;
import com.yhf.pointsmanage.entity.Goods;
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

    public int setGoods(){
        HttpClient client = HttpClients.createDefault();
        // 要调用的接口方法
        String url = "http://localhost:8081/pointsGoods/getAll";
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = null;
        JSONObject date = new JSONObject();
        try {
            StringEntity s = new StringEntity(date.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            post.addHeader("content-type", "text/xml");
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                jsonObject = JSONObject.parseObject(result);
            }
            JSONObject data = new JSONObject();
            //获取Json中data部分数据
            JSONObject goodsJson=jsonObject.getJSONObject("data");
            Iterator it = goodsJson.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                data.put(entry.getKey(), entry.getValue());
            }
            List<Goods> goods = new ArrayList<>();
            //将data中数据转存到List中
            for (String key : data.keySet()) {
                System.out.println(data.get(key));
                JSONObject goodJ=data.getJSONObject(key);
                Goods good=new Goods(goodJ.getInteger("id"),goodJ.getString("name"),goodJ.getInteger("points"),goodJ.getString("picture")
                ,goodJ.getInteger("goods_num"),0);
                goods.add(good);
            }
            boolean in_su = goodsDao.insertList(goods);
            if(in_su)
            {
                return Constant.SUCCESS;
            }
            else
                return Constant.FAILURE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Constant.EXCEPTION;
        }
    }

    public List<Goods> getAllGoods()
    {
        HttpClient client = HttpClients.createDefault();
        String url = "http://localhost:8081/pointsGoods/getAll";
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = null;
        JSONObject date = new JSONObject();
        try {
            StringEntity s = new StringEntity(date.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            post.addHeader("content-type", "text/xml");
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                jsonObject = JSONObject.parseObject(result);
            }
            JSONObject data = new JSONObject();
            JSONObject goodsJson=jsonObject.getJSONObject("data");
            Iterator it = goodsJson.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                data.put(entry.getKey(), entry.getValue());
            }
            List<Goods> goods = new ArrayList<>();
            for (String key : data.keySet()) {
                System.out.println(data.get(key));
                JSONObject goodJ=data.getJSONObject(key);
                Goods good=new Goods(goodJ.getInteger("id"),goodJ.getString("name"),goodJ.getInteger("points"),goodJ.getString("picture")
                        ,goodJ.getInteger("goods_num"),goodJ.getInteger("goods_browse"));
                goods.add(good);
            }
           return goods;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}

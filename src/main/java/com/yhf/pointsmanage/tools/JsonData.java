package com.yhf.pointsmanage.tools;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.constant.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonData {
    public static JSONObject getJson(String url) throws IOException
    {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = new JSONObject();
        JSONObject date = new JSONObject();

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
        //获取Json中data部分数据
        JSONObject json = jsonObject.getJSONObject("data");
        return json;
    }

    public static JSONObject getJsonHaveReturn(String url, Map<String, Object> map) throws IOException
    {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = new JSONObject();
        JSONObject date = new JSONObject();
        JSONObject json = new JSONObject();
        date.putAll(map);
        StringEntity s = new StringEntity(date.toString(),"UTF-8");
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        post.setEntity(s);
        post.addHeader("content-type", "application/json");
        HttpResponse res = client.execute(post);
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(res.getEntity());// 返回json格式：
            jsonObject = JSONObject.parseObject(result);
            //获取Json中data部分数据
            json = jsonObject.getJSONObject("data");
        }
        return json;
    }

    public static Integer getJsonHaveNoReturn(String url, Map<String, Object> map) throws IOException
    {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = new JSONObject();
        JSONObject date = new JSONObject();
        Integer state= Constant.ERROR;
        date.putAll(map);
        StringEntity s = new StringEntity(date.toString());
        s.setContentEncoding("UTF-8");
        s.setContentType("application/json");
        post.setEntity(s);
        post.addHeader("content-type", "application/json");
        HttpResponse res = client.execute(post);
        if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(res.getEntity());// 返回json格式：
            jsonObject = JSONObject.parseObject(result);
            //获取Json中data部分数据
            state=jsonObject.getInteger("code");
        }
        return state;
    }
}

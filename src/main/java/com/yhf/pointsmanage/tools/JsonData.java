package com.yhf.pointsmanage.tools;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class JsonData {
    public static JSONObject getJson(String url) throws IOException
    {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject jsonObject = null;
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
}

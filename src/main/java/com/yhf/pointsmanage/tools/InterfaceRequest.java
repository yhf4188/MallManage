package com.yhf.pointsmanage.tools;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.entity.Attribute;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class InterfaceRequest implements Callable<JSONObject> {

    private JSONObject param;

    public InterfaceRequest(JSONObject param) throws IOException, JSONException{
        this.param=param;
        try{
            String url=this.param.getString("url");
            Attribute attribute = this.param.getObject("user",Attribute.class);
            Map<String,Object> map=new ConcurrentHashMap<>();
            map.put("user",attribute);
            JSONObject jsonObject=JsonData.getJsonHaveReturn(url,map);
            this.param.put("returnJson",jsonObject);
        }catch (Exception e)
        {
            throw e;
        }
    }

    //数据回调
    public JSONObject call() throws Exception {
        return this.param;
    }
}

package com.yhf.pointsmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.*;
import com.yhf.pointsmanage.exception.CustomizeException;
import com.yhf.pointsmanage.service.UserService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValueOperations valueOperations;

    /**
     * 时刻获取用户数据以监听数据变化
     * @param userName
     * @return
     */
    @PostMapping("monitorUserInfo")
    public Message getUser(@RequestParam("username") String userName) {
        Message message = new Message();
        try
        {
            User user=new User();
            if(user!=null) {
                user = userService.getUserMallByUserName(userName);
                String token = valueOperations.get(userName).toString();
                message.setMessage(Constant.SUCCESS,"获取成功");
                message.getData().put("user",user);
                message.getData().put("token",token);
            }
            else message.setMessage(Constant.FAILURE,"获取失败");
        }catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (RuntimeException e)
        {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,e.getMessage());
        }
        finally {
            return message;
        }
    }

    /**
     * 设置用户与商城的绑定关系
     * @param map
     * @return
     */
    @RequestMapping(value = "/bindUser",method = RequestMethod.POST)
    public Message setUserBind(@RequestBody JSONObject map)
    {
        User user =map.getObject("user",User.class);
        Attribute attribute = map.getObject("attribute",Attribute.class);
        Mall mall=map.getObject("mall",Mall.class);
        Message message=new Message();
        try
        {
            boolean success=userService.setUserBind(user,attribute,mall);
            if(success)
            {
                message.setMessage(Constant.SUCCESS,"绑定成功");
            }
            else message.setMessage(Constant.FAILURE,"绑定失败");
        }catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,e.getMessage());
        }
        finally {
            return message;
        }
    }

    /**
     * 获取用户绑定商城
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/getBind",method = RequestMethod.POST)
    public Message getUserBind(@RequestBody JSONObject jsonObject)
    {
        User user = jsonObject.getObject("user",User.class);
        Message message=new Message();
        try
        {
            List<UserBindMall> userBindMalls=new ArrayList<>();
            userBindMalls=userService.getUserBind(user);
            if(userBindMalls.isEmpty())
            {
                message.setMessage(Constant.FAILURE,"还没有绑定商城，请您绑定");
            }
            else
            {
                message.setMessage(Constant.SUCCESS,"查找成功");
                message.getData().put("mall",userBindMalls);
            }
        }catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (RuntimeException e)
        {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,e.getMessage());
        }
        finally {
            return message;
        }
    }


    @RequestMapping(value = "/updateBindMall",method = RequestMethod.POST)
    public Message updateBindMall(@RequestBody JSONObject jsonObject)
    {
        Mall mallOld = jsonObject.getObject("oldMall",Mall.class);
        Mall mallNew = jsonObject.getObject("newMall",Mall.class);
        Message message=new Message();
        try{
            boolean success = true;
            success = userService.updateBindMall(mallOld,mallNew);
            if(success = true) {
                message.setMessage(Constant.SUCCESS, "修改成功");
            }else message.setMessage(Constant.ERROR, "存在无效连接");
        }catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,"修改异常");
        }finally {
            return message;
        }
    }

    @RequestMapping(value = "/setBindMall",method = RequestMethod.POST)
    public Message setBindMall(@RequestBody JSONObject jsonObject)
    {
        Mall mall = jsonObject.getObject("mall",Mall.class);
        User user = jsonObject.getObject("user",User.class);
        Message message = new Message();
        try
        {
            boolean success = true;
            success = userService.setBindMall(user,mall);
            if(success = true) {
                message.getData().put("user", user);
                message.setMessage(Constant.SUCCESS, "绑定成功");
            }else message.setMessage(Constant.ERROR, "存在无效连接或已存在商城");
        }catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,"绑定异常");
        }
        finally {
            return message;
        }
    }

    @RequestMapping(value = "/setAddress",method = RequestMethod.POST)
    public Message setAddress(@RequestBody JSONObject jsonObject)
    {
        int mallID=jsonObject.getInteger("mall");
        User user = jsonObject.getObject("user",User.class);
        Message message = new Message();
        try {
            if(jsonObject.containsKey("add"))
            {
                String address = jsonObject.getString("add");
                if(userService.setAddress(address,mallID,user)!=null){
                    message.setMessage(Constant.SUCCESS,"地址设置成功");
                } else message.setMessage(Constant.FAILURE,"地址设置失败");
            }else {
                Address address = jsonObject.getObject("address",Address.class);
                if(userService.setAddress(address,mallID,user)!=null){
                    message.setMessage(Constant.SUCCESS,"默认地址更改成功");
                } else message.setMessage(Constant.FAILURE,"默认地址设置失败");
            }
        }catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,"设置异常");
        }finally {
            return message;
        }
    }
}

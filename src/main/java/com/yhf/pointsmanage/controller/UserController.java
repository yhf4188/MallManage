package com.yhf.pointsmanage.controller;

import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.Mall;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.entity.UserBindMall;
import com.yhf.pointsmanage.service.UserService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param user
     * @param mall
     * @return
     */
    public Message setUserBind(User user, Mall mall)
    {
        Message message=new Message();
        try
        {
            boolean success=userService.setUserBind(user,mall);
            if(success)
            {
                message.setMessage(Constant.SUCCESS,"绑定成功");
            }
            else message.setMessage(Constant.FAILURE,"绑定失败");
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
     * 获取用户绑定商城
     * @param user
     * @return
     */
    public Message getUserBind(User user)
    {
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
        }catch (RuntimeException e)
        {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,e.getMessage());
        }
        finally {
            return message;
        }
    }
}

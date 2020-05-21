package com.yhf.pointsmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.service.UserService;
import com.yhf.pointsmanage.tools.Message;
import com.yhf.pointsmanage.tools.RedisService;
import com.yhf.pointsmanage.tools.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/loginAndRegister")
public class LoginAndRegisterController {

    @Autowired
    private UserService userService;

    @Resource
    private ValueOperations<String, Object> valueOperations;

    @Resource
    private RedisService redisService;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Message login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = new User();
        Message message = new Message();
        try {
            user = userService.getUserMall(username, password);
            if (user == null) {
                message.setMessage(Constant.FAILURE, "登陆失败，账号或密码错误");
            } else {
                message.setMessage(Constant.SUCCESS, "登陆成功");
                //将登陆信息存入redis缓存，以实现在其他地方登陆后的排挤操作
                //生成token
                User u = new User();
                u.setUserName(username);
                String token = TokenUtil.sign(u);
                message.getData().put("user", user);
                message.getData().put("token", token);
                valueOperations.set(user.getUserName(), token);
                redisService.persistKey(user.getUserName());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "登陆异常：" + e.getMessage());
        } finally {
            return message;
        }
    }

    /**
     * 登出操作（当前端监听到程序关闭，在前端SessionStorage删除登录信息的同时，删除服务器的登录信息）
     *
     * @param jsonObject
     */
    @RequestMapping("/logout")
    public Message logout(@RequestBody JSONObject jsonObject) {
        Message message = new Message();
        String userName = jsonObject.getString("username");
        try {
            redisService.deleteKey(userName);
            message.setMessage(Constant.SUCCESS,"注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            message.setMessage(Constant.ERROR,"注销异常");
        } finally {
            return message;
        }
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST,consumes ="application/json")
    public Message register(@RequestBody User user) {
        Message message = new Message();
        try {
            User newUser = new User();
            boolean registerSuccess = userService.register(user);
            if (registerSuccess) {
                message.setMessage(Constant.SUCCESS, "注册成功");
            } else message.setMessage(Constant.FAILURE, "注册失败");
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "注册异常：" + e.getMessage());
        } finally {
            return message;
        }
    }

    /**
     * 查询手机是否被注册
     *
     * @param phone
     * @return
     */
    @GetMapping("/havePhone")
    public Message havePhone(@RequestParam("phone") String phone) {
        Message message = new Message();
        try {
            String have = userService.havePhone(phone);
            if (have != null) {
                message.setMessage(Constant.FAILURE, "该手机已注册");
            } else message.setMessage(Constant.SUCCESS, "可以使用");
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "查询异常：" + e.getMessage());
        } finally {
            return message;
        }
    }

    /**
     * 查询用户名是否被注册
     *
     * @param userName
     * @return
     */
    @GetMapping("/haveUserName")
    public Message haveUserName(@RequestParam("username") String userName) {
        Message message = new Message();
        try {
            String have = userService.haveUserName(userName);
            if (have != null) {
                message.setMessage(Constant.FAILURE, "该用户已注册");
            } else message.setMessage(Constant.SUCCESS, "可以使用");
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "查询异常：" + e.getMessage());
        } finally {
            return message;
        }
    }
}

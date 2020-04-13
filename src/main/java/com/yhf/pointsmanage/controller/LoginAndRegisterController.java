package com.yhf.pointsmanage.controller;

import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.service.UserService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/loginAndRegister")
public class LoginAndRegisterController {

    @Autowired
    private UserService userService;

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
            } else
            {
                message.setMessage(Constant.SUCCESS, "登陆成功");
                message.getData().put("user",user);
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "登陆异常：" + e.getMessage());
        }finally {
            return message;
        }
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @GetMapping("/register")
    public Message register(User user) {
        Message message = new Message();
        try {
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
     * @param phone
     * @return
     */
    @GetMapping("/havePhone")
    public Message havePhone(@RequestParam("phone") String phone)
    {
        Message message = new Message();
        try {
            boolean have = userService.havePhone(phone);
            if (have) {
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
     * @param userName
     * @return
     */
    @GetMapping("/haveUserName")
    public Message haveUserName(@RequestParam("userName") String userName)
    {
        Message message = new Message();
        try {
            boolean have = userService.haveUserName(userName);
            if (have) {
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

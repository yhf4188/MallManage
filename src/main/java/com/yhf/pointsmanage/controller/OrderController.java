package com.yhf.pointsmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.Order;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.exception.CustomizeException;
import com.yhf.pointsmanage.service.OrderService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取兑换记录
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/getOrder",method = RequestMethod.POST)
    public Message getOrder(@RequestBody JSONObject jsonObject)
    {
        Message message=new Message();
        try
        {
            User user=jsonObject.getObject("user",User.class);
            List<Order> orders = orderService.getOrder(user);
            message.setMessage(Constant.SUCCESS,"获取成功");
            message.getData().put("orders",orders);
        } catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,"系统异常");
        } finally {
            return message;
        }
    }
}

package com.yhf.pointsmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.Goods;
import com.yhf.pointsmanage.entity.Mall;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.service.GoodsService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Author:yhf
 * 商品Controller
 */
@Slf4j
@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 将所有的商品数据存入数据库
     * @return
     */

    //定时在每天零点导入一次商品
//    @Scheduled(cron = "0 0 0 1/1 * ? *")
    public Message insertList() {
        Message message = new Message();
        int result = goodsService.setGoods();
        if (result == Constant.SUCCESS) {
            message.setMessage(result, "批量插入成功");
        } else if (result == Constant.FAILURE) {
            message.setMessage(result, "批量插入失败");
        } else {
            message.setMessage(result, "批量插入异常");
        }
        return message;
    }

    /**
     * 获取所有商品数据
     * @return
     */
    @PostMapping("/getAllGoods")
    public Message getAllGoods(@RequestBody JSONObject jsonObject) {
        try {
            User user=jsonObject.getObject("user",User.class);
            String userName=user.getUserName();
            Message message = new Message();
            List<Goods> goods = new ArrayList<>();
            goods = goodsService.getAllGoods(userName);
            message.getData().put("goods",goods);;
            message.setMessage(Constant.SUCCESS, "查找成功");
            return message;
        } catch (Exception e) {
            Message message = new Message();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "查找异常").getData().put("Exception", e.getMessage());
            return message;
        }
    }

    /**
     * 获取对应商城的商品数据
     * @param mallId
     * @return
     */
    @RequestMapping(value = "/getGoodsByMallId")
    public Message getGoodsByMallId(@RequestParam("mallId") int mallId)
    {
        try {
            Message message = new Message();
            List<Goods> goods =new ArrayList<>();
            goods = goodsService.getGoodsByMallId(mallId);
            message.getData().put("goods",goods);
            message.getData().put("goods",goods);;
            message.setMessage(Constant.SUCCESS, "查找成功");
            return message;
        } catch (Exception e) {
            Message message = new Message();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "查找异常").getData().put("Exception", e.getMessage());
            return message;
        }
    }
}

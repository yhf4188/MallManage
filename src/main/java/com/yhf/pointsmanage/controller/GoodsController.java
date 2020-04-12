package com.yhf.pointsmanage.controller;

import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.Goods;
import com.yhf.pointsmanage.service.GoodsService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/insertList")
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
    public Message getAllGoods() {
        try {
            Message message = new Message();
            List<Goods> goods = new ArrayList<>();
            goods = goodsService.getAllGoods();
            Map<String, Object> map = new HashMap<>();
            map.put("goods",goods);
            message.getData().putAll(map);
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

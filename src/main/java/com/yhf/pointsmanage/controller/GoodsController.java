package com.yhf.pointsmanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.Address;
import com.yhf.pointsmanage.entity.Goods;
import com.yhf.pointsmanage.entity.User;
import com.yhf.pointsmanage.service.GoodsService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = "/getGoodsByMallId",method=RequestMethod.POST)
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

    /**
     * 积分消费
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/pointsCost",method = RequestMethod.POST)
    public Message cost(@RequestBody JSONObject jsonObject)
    {
        Message message=new Message();
        try{
            String userName=jsonObject.getString("userName");
            int goodID=jsonObject.getInteger("goodID");
            int mallID=jsonObject.getInteger("mallID");
            Address address=jsonObject.getObject("address",Address.class);
            Integer code=goodsService.cost(userName,goodID,mallID,address);
            if(code.equals(Constant.SUCCESS))
            {
                message.setMessage(Constant.SUCCESS,"兑换成功");
            } else if(code.equals(Constant.FAILURE))
                message.setMessage(Constant.FAILURE,"兑换失败，库存不足");
            else if(code.equals(1000))
                message.setMessage(code,"请先设置收货地址");
            else message.setMessage(Constant.ERROR,"系统异常");
        }catch (Exception e){
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,"系统异常");
            e.printStackTrace();
        }finally {
            return message;
        }
    }


    /**
     * 分页数据获取
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/getGoodsByPage", method = RequestMethod.POST)
    public Message getGoodsByPage(@RequestBody JSONObject jsonObject)
    {
        Message message = new Message();
        try{
            Integer pageNo = jsonObject.getInteger("pageNo");//页数
            Integer pageNum = jsonObject.getInteger("pageNum");//一页数量
            Integer mallID= jsonObject.getInteger("mallID");//获取商城
            Integer classif = jsonObject.getInteger("classf");//获取分类
            Integer userID = jsonObject.getInteger("userID");//获取当前用户ID
            PageHelper.startPage(pageNo,pageNum);
            List<Goods> list = goodsService.getGoodsByPage(mallID,classif,userID);
            PageInfo<Goods> pageInfo = new PageInfo<>(list);
            message.setMessage(Constant.SUCCESS,"获取成功");
            message.getData().put("goods",pageInfo);
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR,"获取异常");
        }finally {
          return message;
        }
    }
}

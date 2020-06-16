package com.yhf.pointsmanage.controller;

import com.yhf.pointsmanage.constant.Constant;
import com.yhf.pointsmanage.entity.Mall;
import com.yhf.pointsmanage.exception.CustomizeException;
import com.yhf.pointsmanage.service.MallService;
import com.yhf.pointsmanage.tools.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mall")
public class MallController {

    @Autowired
    private MallService mallService;

    /**
     * 获取商城信息
     * @return
     */
    @RequestMapping(value = "/getAllMall",method = RequestMethod.POST)
    public Message getAllMall()
    {
        Message message =new Message();
        try{
            List<Mall> malls = mallService.getAllMall();
            message.setMessage(Constant.SUCCESS,"获取成功");
            message.getData().put("mall",malls);
        } catch (CustomizeException e) {
            e.printStackTrace();
            log.error(e.getMsgDes());
            message.setMessage(Constant.ERROR,e.getMessage());
        }catch (Exception e)
        {
            log.error(e.getMessage());
            message.setMessage(Constant.ERROR, "获取异常:"+e.getMessage());
        } finally {
            return message;
        }
    }
}

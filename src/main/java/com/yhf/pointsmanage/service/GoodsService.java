package com.yhf.pointsmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.yhf.pointsmanage.dao.*;
import com.yhf.pointsmanage.entity.*;
import com.yhf.pointsmanage.tools.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.yhf.pointsmanage.constant.Constant;

import javax.annotation.Resource;

@Slf4j
@Service
public class GoodsService {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MallDao mallDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private ConsumeRecordDao consumeRecordDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String, Object> valueOperations;

    //导入商品数据
    public int setGoods() {
        List<Mall> malls = new ArrayList<>();
        malls = mallDao.getAllMall();
        List<String> inMallID = new ArrayList<>();
        List<Goods> goods = new ArrayList<>();
        try {
            //分商城导入商品数据
            for (Mall mall : malls) {
                inMallID = goodsDao.getInMallID(mall.getId());
                String url = mall.getShop_impl();
                JSONObject goodsJson = JsonData.getJson(url);
                if(goodsJson.isEmpty())
                {
                    return Constant.FAILURE;
                }
                JSONObject data = new JSONObject();
                Iterator it = goodsJson.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                    data.put(entry.getKey(), entry.getValue());
                }
                //将data中数据转存到List中
                for (String key : data.keySet()) {
                    JSONObject goodJ = data.getJSONObject(key);
                    String inMallId = goodJ.getString("id");
                    if (!inMallID.contains(inMallId)) {
                        Goods good = new Goods(goodJ.getString("name"), goodJ.getInteger("points"), goodJ.getString("picture")
                                , goodJ.getInteger("goods_num"), 0, goodJ.getInteger("goods_classf"),mall.getId(), inMallId);
                        goods.add(good);
                    } else {
                        goodsDao.update(new Goods(goodJ.getString("name"), goodJ.getInteger("points"), goodJ.getString("picture")
                                , goodJ.getInteger("goods_num"), 0, goodJ.getInteger("goods_classf"),mall.getId(), inMallId));
                    }
                }
            }
            boolean in_su = true;
            if(!goods.isEmpty())
                in_su = goodsDao.insertList(goods);
            if (in_su) {
                return Constant.SUCCESS;
            } else
                return Constant.FAILURE;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Constant.EXCEPTION;
        }
    }

    //获取所有商品数据
    public List<Goods> getAllGoods(String userName) {
        List<Mall> malls = new ArrayList<>();
        malls = mallDao.getMallImpl(userName);
        User user = userDao.getUserMallByUserName(userName);
        List<Goods> goods = new ArrayList<>();
        try {
            for (Mall mall : malls) {
                goods.addAll(goodsDao.getGoodsByMallIdAndUser(mall.getId(),user.getId()));
            }
            return goods;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    //根据商品分类输出数据
    public List<Goods> getGoodsByMallId(int mallId) {
        List<Goods> goods = new ArrayList<>();
        try {
            goods = goodsDao.getGoodsByMallId(mallId);
            return goods;
        }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    //兑换
    public Integer cost(String userName,int goodID, int mallID,Address address) throws IOException{
        try{
            Mall mall=mallDao.getMall(mallID);
            String url=mall.getConsume_impl();
            UserBindMall userBindMall=userDao.getUserBindByMall(userDao.getUserMallByUserName(userName).getId(),mallID);
            if(address==null)
            {
                return 1000;
            }
            Map<String,Object> map =new ConcurrentHashMap<>();
            map.put("userName",userName);
            map.put("mallID",goodID);
            map.put("addressID",address.getOrigin_id());
            Integer code = JsonData.getJsonHaveNoReturn(url,map);
            if(code == Constant.SUCCESS)
            {
                ConsumeRecord order=new ConsumeRecord();
                order.setGoods_id(goodsDao.getGoodsByMallIDAndInMall(mallID,goodID).getId());
                System.out.println(order.getGoods_id());
                order.setUser_id(userBindMall.getUserID());
                consumeRecordDao.insertOrder(order);
                userService.updateUserBind(userBindMall);
                setGoods();
            }
            return code;
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    //获取一页商品
    public List<Goods> getGoodsByPage(Integer mallID,Integer classif, Integer userID)
    {
        List<Goods> list=new ArrayList<>();
        try
        {
            list = goodsDao.getGoodsByPage(userID,mallID,classif);
        }catch (Exception e)
        {
            throw e;
        }finally {
            return list;
        }
    }

    //添加收藏
    public boolean star(int goodID, int userID) {
        try {
            String key = "goods_key:" + goodID;
            if (redisTemplate.opsForSet().isMember(key, userID)) {
                redisTemplate.opsForSet().remove(key, userID);
                goodsDao.unstar(goodID, userID);
            } else {
                redisTemplate.opsForSet().add(key, userID);
                goodsDao.star(goodID, userID);
            }
            return true;
        }catch (Exception e)
        {
            throw e;
        }
    }

    //获取收藏
    public List<Goods> getCollectionByPage(int userID) {
        try{
            List<Goods> collections = new ArrayList<>();
            collections=goodsDao.getCollectionByPage(userID);
            return collections;
        }catch (Exception e)
        {
            throw  e;
        }
    }
}

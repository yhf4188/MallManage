package com.yhf.pointsmanage.dao;

import com.yhf.pointsmanage.entity.Goods;
import com.yhf.pointsmanage.entity.Mall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsDao {
    boolean insertList(List<Goods> goods);
    List<Goods> getGoodsByMallId(@Param("mallID") int mallID);
    List<Goods> getGoodsByMallIdAndUser(@Param("mallID") int mallID,@Param("userID") int userID);
    List<String> getInMallID(@Param("mallID") int mallID);
    void deleteAll();
    void update(Goods goods);
    List<Goods> getGoodsByPage(@Param("userID") int userID,@Param("mallID") int mallID, @Param("classif") int classif);
    Goods getGoodsByMallIDAndInMall(@Param("mallID") int mallID,@Param("inMallID") int inMallID);
    int star(@Param("goodID") int goodsID,@Param("userID") int userID);
    int unstar(@Param("goodID") int goodsID,@Param("userID") int userID);
    List<Goods> getCollectionByPage(@Param("userID") int userID);
}

package com.yhf.pointsmanage.dao;

import com.yhf.pointsmanage.entity.Goods;
import com.yhf.pointsmanage.entity.Mall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {
    boolean insertList(List<Goods> goods);
    List<Goods> getGoodsByMallId(@Param("mallID") int mallID);
    List<Goods> getGoodsByMallIdAndUser(@Param("mallID") int mallID,@Param("userID") int userID);
}

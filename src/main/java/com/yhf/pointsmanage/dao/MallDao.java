package com.yhf.pointsmanage.dao;

import com.yhf.pointsmanage.entity.Mall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MallDao {
    List<Mall> getMallIpml(@Param("userName") String userName);
    List<Mall> getAllMall();
    Map<String,String> getInMallID();
}

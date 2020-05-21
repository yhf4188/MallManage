package com.yhf.pointsmanage.dao;

import com.yhf.pointsmanage.entity.Mall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MallDao {
    List<Mall> getMallImpl(@Param("userName") String userName);
    Mall getMall(@Param("id") int id);
    List<Mall> getAllMall();
}

package com.yhf.pointsmanage.dao;

import com.yhf.pointsmanage.entity.ConsumeRecord;
import com.yhf.pointsmanage.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ConsumeRecordDao {
    List<ConsumeRecord> getOrder(User user);
    int insertOrder(ConsumeRecord order);
}

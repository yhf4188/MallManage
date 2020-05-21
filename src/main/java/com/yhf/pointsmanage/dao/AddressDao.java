package com.yhf.pointsmanage.dao;


import com.yhf.pointsmanage.entity.Address;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressDao {

    //新增地址
    int insertAddress(Address address);

    //删除地址
    int deleteAddress(int id);

    //更新地址
    int updateAddress(Address address);

    //获取地址
    List<Address> getAddress(int user_mall_id);

    //根据id获取地址
    Address getOneAddress(int id);

    //获取用户默认地址
    Address getDefaultAddress(int user_mall_id,boolean ad_default);
}

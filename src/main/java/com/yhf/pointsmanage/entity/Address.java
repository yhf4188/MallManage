package com.yhf.pointsmanage.entity;


import lombok.Data;

@Data
public class Address {
    Integer id;
    Integer user_mall_id;
    Integer origin_id;
    String address;
    boolean ad_default;
    boolean have_delete;

    public Address() {
    }

    public Address(Integer id, Integer origin_id,Integer user_mall_id, String address,boolean ad_default,boolean have_delete) {
        this.id = id;
        this.origin_id = origin_id;
        this.user_mall_id = user_mall_id;
        this.address = address;
        this.ad_default = ad_default;
        this.have_delete = have_delete;
    }
}

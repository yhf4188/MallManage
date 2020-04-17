package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class Mall {
    private int id;
    private String name;
    private String shop_impl;//商品接口地址
    private String consume_impl;//消费接口地址
    private String logistics_impl;//物流接口地址
    private String order_impl;//订单接口地址
    private String points_impl;//积分操作接口地址
    private String attribution_impl;//个人信息接口地址

    public Mall() {
    }

    public Mall(int id, String name, String shop_impl, String consume_impl, String logistics_impl, String order_impl, String points_impl, String attribution_impl) {
        this.id = id;
        this.name = name;
        this.shop_impl = shop_impl;
        this.consume_impl = consume_impl;
        this.logistics_impl = logistics_impl;
        this.order_impl = order_impl;
        this.points_impl = points_impl;
        this.attribution_impl = attribution_impl;
    }
}

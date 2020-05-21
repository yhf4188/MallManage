package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class Order {
    Goods goods;
    User user;
    Address address;
    String state;
    int origin_id;

    public Order() {
    }

    public Order(Goods goods, User user, Address address, String state, int origin_id) {
        this.goods = goods;
        this.user = user;
        this.address = address;
        this.state = state;
        this.origin_id = origin_id;
    }
}

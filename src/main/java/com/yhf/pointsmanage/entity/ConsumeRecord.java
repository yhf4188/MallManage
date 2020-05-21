package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class ConsumeRecord {
    int id;
    int goods_id;
    int mall_goods_id;//商品in-mall-id
    int mall_user_id;
    int user_id;
    String goods_state;

    public ConsumeRecord() {
    }

    public ConsumeRecord(int id, int goods_id, int user_id) {
        this.id = id;
        this.goods_id = goods_id;
        this.user_id = user_id;
    }

    public ConsumeRecord(int id, int mall_goods_id, int mall_user_id, String goods_state) {
        this.id = id;
        this.mall_goods_id = mall_goods_id;
        this.mall_user_id = mall_user_id;
        this.goods_state = goods_state;
    }
}

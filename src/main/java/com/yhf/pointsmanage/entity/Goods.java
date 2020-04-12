package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class Goods {
    int id;
    String name;
    int points;
    String picture;
    int goods_num;
    int goods_browse;

    public Goods() {
    }

    public Goods(int id, String name, int points, String picture, int goods_num, int goods_browse) {
        this.id=id;
        this.name = name;
        this.points = points;
        this.picture = picture;
        this.goods_num = goods_num;
        this.goods_browse = goods_browse;
    }
}

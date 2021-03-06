package com.yhf.pointsmanage.entity;

import lombok.Data;

@Data
public class Goods {
    int id;
    String name;//商品名
    int points;//花费积分
    String picture;//商品图片
    int goods_num;//商品数量
    int goods_browse;//商品已兑换次数
    int goods_classf;//商品分类
    int mallID;//商品所在积分商城ID
    String inMallID;//商品在积分商城所占用的id
    String star;

    public Goods() {
    }

    public Goods(String name, int points, String picture, int goods_num, int goods_browse,int goods_classf, String inMallID) {
        this.name = name;
        this.points = points;
        this.picture = picture;
        this.goods_num = goods_num;
        this.goods_browse = goods_browse;
        this.goods_classf=goods_classf;
        this.inMallID = inMallID;
    }

    public Goods( String name, int points, String picture, int goods_num, int goods_browse,int goods_classf, int mallID, String inMallID) {
        this.name = name;
        this.points = points;
        this.picture = picture;
        this.goods_num = goods_num;
        this.goods_browse = goods_browse;
        this.goods_classf=goods_classf;
        this.mallID = mallID;
        this.inMallID = inMallID;
    }
    public Goods(int id, String name, int points, String picture, int goods_num, int goods_browse, int mallID, String inMallID) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.picture = picture;
        this.goods_num = goods_num;
        this.goods_browse = goods_browse;
        this.mallID = mallID;
        this.inMallID = inMallID;
    }
    public Goods(int id, String name, int points, String picture, int goods_num, int goods_browse, int goods_classf, int mallID, String inMallID, String star) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.picture = picture;
        this.goods_num = goods_num;
        this.goods_browse = goods_browse;
        this.goods_classf = goods_classf;
        this.mallID = mallID;
        this.inMallID = inMallID;
        this.star = star;
    }
}

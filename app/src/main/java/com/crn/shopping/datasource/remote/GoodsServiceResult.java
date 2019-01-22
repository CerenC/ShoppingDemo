package com.crn.shopping.datasource.remote;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * Created by ceren on 6/17/17.
 */
public class GoodsServiceResult {
    private String date;
    private List<GoodsServiceResultItem> goods;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<GoodsServiceResultItem> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsServiceResultItem> goods) {
        this.goods = goods;
    }
}

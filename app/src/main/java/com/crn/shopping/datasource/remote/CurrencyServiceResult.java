package com.crn.shopping.datasource.remote;

import java.math.BigDecimal;
import java.util.Map;
/**
 * Created by ceren on 6/17/17.
 */
public class CurrencyServiceResult {
    // {"base":"EUR","date":"2017-06-16","rates":{"USD":1.1167}}
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}

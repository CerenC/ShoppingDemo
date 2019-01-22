package com.crn.shopping.datasource.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by ceren on 6/17/17.
 */
public class GoodsServiceFactory {
    private final static String BASE_URL = "https://data-strg.appspot.com/";

    public static GoodsService create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GoodsService.class);
    }

}
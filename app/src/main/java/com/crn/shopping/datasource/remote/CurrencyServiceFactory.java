package com.crn.shopping.datasource.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by ceren on 6/17/17.
 */
public class CurrencyServiceFactory {
    // https://api.fixer.io/latest?symbols=USD
    private final static String BASE_URL = "https://api.fixer.io/";

    public static CurrencyService create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(CurrencyService.class);
    }

}
package com.crn.shopping.datasource.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ceren on 6/17/17.
 */

public interface CurrencyService {
    // https://api.fixer.io/latest?symbols=USD
    @GET("latest")
    Call<CurrencyServiceResult> getLatest();

    @GET("latest")
    Call<CurrencyServiceResult> getLatest(@Query("symbols") String symbol);
}

package com.crn.shopping.datasource.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ceren on 6/17/17.
 */

public interface GoodsService {
    // http://data-strg.appspot.com/shopping/goods.json
    @GET("shopping/goods.json")
    Call<GoodsServiceResult> getLatest();
}

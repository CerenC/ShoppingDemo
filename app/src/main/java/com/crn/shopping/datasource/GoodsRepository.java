package com.crn.shopping.datasource;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.crn.shopping.application.BaseApplication;
import com.crn.shopping.datasource.local.AppDatabase;
import com.crn.shopping.datasource.remote.GoodsServiceFactory;
import com.crn.shopping.datasource.remote.GoodsServiceResult;
import com.crn.shopping.datasource.remote.GoodsServiceResultItem;
import com.crn.shopping.model.GoodsItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GoodsRepository {

    private static GoodsRepository sInstance;

    private final MutableLiveData<Boolean> mIsReady = new MutableLiveData<>();

    private final MutableLiveData<List<GoodsItem>> mGoodsItemList = new MutableLiveData<>();
    // lock for Singleton instantiation
    private static final Object LOCK = new Object();

    public synchronized static GoodsRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new GoodsRepository();
                }
            }
        }
        return sInstance;
    }

    // private constructor for Singleton instantiation
    private GoodsRepository() {
        reset();
    }

    public LiveData<List<GoodsItem>> loadAll() {
        return mGoodsItemList;
    }

    public MutableLiveData<Boolean> isReady() {
        return mIsReady;
    }

    public void reset() {
        ResetAsyncTask resetAsyncTask = new ResetAsyncTask();
        resetAsyncTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class ResetAsyncTask extends AsyncTask<Void, Void, List<GoodsItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsReady.setValue(false);// To show a loading screen.
        }

        @Override
        protected List<GoodsItem> doInBackground(Void... params) {
            try {

                Call<GoodsServiceResult> call = GoodsServiceFactory.create().getLatest();

                // Sync call in async task.
                Response<GoodsServiceResult> response = call.execute();
                if (response.body() != null && response.body().getGoods() != null) {
                    List<GoodsServiceResultItem> goodsRemoteList = response.body().getGoods();

                    List<GoodsItem> goodsList = new ArrayList<>(goodsRemoteList.size());
                    for (GoodsServiceResultItem goodsRemote : goodsRemoteList) {
                        GoodsItem goodsItem = new GoodsItem();
                        goodsItem.setName(goodsRemote.getName());
                        goodsItem.setUnit(goodsRemote.getUnit());
                        goodsItem.setPricePerUnit(goodsRemote.getPrice());
                        goodsItem.setPriceCurrency(goodsRemote.getCurrency());
                        goodsItem.setIconUrl(goodsRemote.getIconUrl());
                        goodsList.add(goodsItem);
                    }
                    return goodsList;

                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO retry one more time
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<GoodsItem> goodsList) {
            super.onPostExecute(goodsList);
            mIsReady.setValue(true);
            mGoodsItemList.setValue(goodsList);
        }
    }
}

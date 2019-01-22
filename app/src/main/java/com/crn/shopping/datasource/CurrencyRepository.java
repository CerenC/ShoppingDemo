package com.crn.shopping.datasource;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.crn.shopping.application.BaseApplication;
import com.crn.shopping.datasource.local.AppDatabase;
import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.datasource.remote.CurrencyServiceFactory;
import com.crn.shopping.datasource.remote.CurrencyServiceResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class CurrencyRepository {

    private AppDatabase mDatabase;

    private static CurrencyRepository sInstance;

    private final MutableLiveData<Boolean> mIsReady = new MutableLiveData<>();

    // lock for Singleton instantiation
    private static final Object LOCK = new Object();

    public synchronized static CurrencyRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new CurrencyRepository();
                }
            }
        }
        return sInstance;
    }

    // private constructor for Singleton instantiation
    private CurrencyRepository() {
        reset();
    }

    public LiveData<List<CurrencyEntity>> loadAll() {
        return mDatabase.currencyDao().loadAll();
    }

    public MutableLiveData<Boolean> isReady() {
        return mIsReady;
    }

    public void reset() {
        ResetAsyncTask resetAsyncTask = new ResetAsyncTask();
        resetAsyncTask.execute();
    }


    public LiveData<CurrencyEntity> load(String currencyCode) {
        return mDatabase.currencyDao().load( currencyCode);
    }
    @SuppressLint("StaticFieldLeak")
    private class ResetAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsReady.setValue(false);// To show a loading screen.
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Context appContext = BaseApplication.getInstance().getApplicationContext();

                mDatabase = Room.databaseBuilder(appContext,
                        AppDatabase.class, AppDatabase.DATABASE_NAME).build();

                Call<CurrencyServiceResult> call = CurrencyServiceFactory.create().getLatest();

                // Sync call in async task.
                Response<CurrencyServiceResult> response = call.execute();
                if (response.body() != null && response.body().getRates() != null) {
                    Map<String, BigDecimal> currenciesRemoteList = response.body().getRates();
                    save(currenciesRemoteList);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO retry one more time
            }

            return null;
        }

        private void save(@NonNull Map<String, BigDecimal> currenciesRemoteList) {

            // Map remote result to entity
            List<CurrencyEntity> currencyEntityList = new ArrayList<>(currenciesRemoteList.size());
            Iterator<Map.Entry<String, BigDecimal>> it = currenciesRemoteList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, BigDecimal> pair = it.next();

                String currencyCode = pair.getKey();
                BigDecimal currencyRate = pair.getValue();

                CurrencyEntity entity = new CurrencyEntity();
                entity.code = currencyCode;
                entity.rate = currencyRate.doubleValue();

                currencyEntityList.add(entity);

                it.remove(); // avoids a ConcurrentModificationException
            }

            // Save to database
            mDatabase.beginTransaction();
            try {
                if(currencyEntityList.size() > 0) {
                    mDatabase.currencyDao().insertAll(currencyEntityList);
                }
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mIsReady.setValue(true);
        }
    }
}

package com.crn.shopping.datasource.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.crn.shopping.datasource.local.entity.CurrencyEntity;

import java.util.List;

@Dao
public interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CurrencyEntity> currencies);

    @Query("SELECT * FROM " + CurrencyEntity.TABLE_NAME)
    LiveData<List<CurrencyEntity>> loadAll();

    @Query("SELECT * FROM " + CurrencyEntity.TABLE_NAME + " WHERE code = :currencyCode")
    LiveData<CurrencyEntity> load(String currencyCode);
}



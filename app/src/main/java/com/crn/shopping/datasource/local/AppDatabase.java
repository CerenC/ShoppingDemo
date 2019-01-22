package com.crn.shopping.datasource.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.crn.shopping.datasource.local.converter.PriceConverter;
import com.crn.shopping.datasource.local.dao.CurrencyDao;
import com.crn.shopping.datasource.local.entity.CurrencyEntity;

@Database(entities = {CurrencyEntity.class}, version = 4)
@TypeConverters(PriceConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "app-db";

    public abstract CurrencyDao currencyDao();
}
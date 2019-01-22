package com.crn.shopping.datasource.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

@Entity(tableName = CurrencyEntity.TABLE_NAME)
public class CurrencyEntity implements Parcelable {
    public static final String TABLE_NAME = "currencies";

    @PrimaryKey
    public String code;
    public String date;
    public double rate;

    public CurrencyEntity() {
    }

    // Parcelable <start>
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.date);
        dest.writeSerializable(this.rate);
    }

    protected CurrencyEntity(Parcel in) {
        this.code = in.readString();
        this.date = in.readString();
        this.rate = in.readDouble();
    }

    public static final Creator<CurrencyEntity> CREATOR = new Creator<CurrencyEntity>() {
        @Override
        public CurrencyEntity createFromParcel(Parcel source) {
            return new CurrencyEntity(source);
        }

        @Override
        public CurrencyEntity[] newArray(int size) {
            return new CurrencyEntity[size];
        }
    };
    // Parcelable <end>
}
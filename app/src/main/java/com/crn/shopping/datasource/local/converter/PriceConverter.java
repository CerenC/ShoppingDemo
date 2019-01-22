package com.crn.shopping.datasource.local.converter;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PriceConverter {

    @TypeConverter
    public static BigDecimal toBigDecimal(String decimalStr) {
        return new BigDecimal(decimalStr);
    }

    @TypeConverter
    public static String toString(BigDecimal decimal) {
        return new DecimalFormat("#0.##").format(decimal);
    }
}
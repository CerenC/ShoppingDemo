package com.crn.shopping.util;

import java.text.DecimalFormat;

/**
 * Created by ceren on 19.06.2017.
 */

public abstract class AmountFormatter {
    public static String getFormattedAmount(double amount){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return  decimalFormat.format(amount);
    }

}

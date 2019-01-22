package com.crn.shopping.ui.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.model.GoodsItem;
import com.crn.shopping.ui.transformation.CircleTransform;
import com.crn.shopping.util.AmountFormatter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import shopping.crn.com.shoppingdemo.R;

public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    @BindingAdapter("visibleGone")
    public static void showHide(View view, int count) {
        if(count> 0){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"iconUrl"})
    public static void loadIcon(ImageView imageView, String iconUrl) {
        loadIcon(imageView, iconUrl, false);
    }

    @BindingAdapter({"circularIconUrl"})
    public static void loadCircularIcon(ImageView imageView, String circularIconUrl) {
        loadIcon(imageView, circularIconUrl, true);
    }

    private static void loadIcon(ImageView imageView, String iconUrl, boolean circular) {
        if (!TextUtils.isEmpty(iconUrl)) {
            RequestCreator requestCreator = Picasso.with(imageView.getContext())
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);
            if(circular) {
                requestCreator.transform(new CircleTransform());
            }
            requestCreator.into(imageView);
        } else {
            RequestCreator requestCreator = Picasso.with(imageView.getContext())
                    .load(R.drawable.ic_placeholder);
            if(circular) {
                requestCreator.transform(new CircleTransform());
            }
            requestCreator.into(imageView);
        }
    }
    @BindingAdapter({"goodsItem","currencyItem"})
    public static void showInfo(TextView textView, GoodsItem goodsItem, CurrencyEntity currencyItem) {
        Context context= textView.getContext();
        double price = goodsItem.getPricePerUnit();
        String currency= context.getResources().getString(R.string.eur);

        if(currencyItem != null && !currencyItem.code.equals(goodsItem.getPriceCurrency())){
            price= price * currencyItem.rate;
            currency=currencyItem.code;
        }

        String priceFormatted= AmountFormatter.getFormattedAmount(price);
        textView.setText(context.getResources().getString(R.string.goods_detail,priceFormatted,currency,goodsItem.getUnit()));

    }

    @BindingAdapter({"goodsItem","currencyItem","count"})
    public static void showInfo(TextView textView, GoodsItem goodsItem, CurrencyEntity currencyItem, int count) {
        if(count > 0){
            textView.setVisibility(View.VISIBLE);

            Context context= textView.getContext();
            double price = goodsItem.getPricePerUnit();
            String currency= context.getResources().getString(R.string.eur);

            if(currencyItem != null && !currencyItem.code.equals(goodsItem.getPriceCurrency())){
                price= price * currencyItem.rate;
                currency=currencyItem.code;
            }


            if(count > 0){
                price = price * count;
            }
            String priceFormatted= AmountFormatter.getFormattedAmount(price);
            textView.setText(context.getResources().getString(R.string.goods_amount, priceFormatted,currency, String.valueOf(count)));
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }
}
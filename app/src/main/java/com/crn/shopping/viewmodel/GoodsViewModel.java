package com.crn.shopping.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.crn.shopping.datasource.CurrencyRepository;
import com.crn.shopping.datasource.GoodsRepository;
import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.model.GoodsItem;

import java.util.List;

public class GoodsViewModel extends AndroidViewModel {

    final GoodsRepository goodsRepository = GoodsRepository.getInstance();
    final CurrencyRepository currencyRepository = CurrencyRepository.getInstance();

    public static final MutableLiveData ABSENT_GOODS = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT_GOODS.setValue(null);
    }

    private static final MutableLiveData ABSENT_CURRENCIES = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT_CURRENCIES.setValue(null);
    }

    private LiveData<List<GoodsItem>> mObservableGoods;
    private LiveData<List<CurrencyEntity>> mObservableCurrencies;

    public LiveData<List<GoodsItem>> getGoods() {
        return mObservableGoods;
    }

    public GoodsViewModel(Application application) {
        super(application);

        mObservableGoods = Transformations.switchMap(goodsRepository.isReady(), new Function<Boolean, LiveData<List<GoodsItem>>>() {
            @Override
            public LiveData<List<GoodsItem>> apply(Boolean isReady) {
                if (!isReady) {
                    //noinspection unchecked
                    return ABSENT_GOODS;
                } else {
                    //noinspection ConstantConditions
                    return goodsRepository.loadAll();
                }
            }
        });

        mObservableCurrencies = Transformations.switchMap(currencyRepository.isReady(), new Function<Boolean, LiveData<List<CurrencyEntity>>>() {
            @Override
            public LiveData<List<CurrencyEntity>> apply(Boolean isReady) {
                if (!isReady) {
                    //noinspection unchecked
                    return ABSENT_CURRENCIES;
                } else {
                    //noinspection ConstantConditions
                    return currencyRepository.loadAll();
                }
            }
        });
    }

    public void refresh() {
        goodsRepository.reset();
    }
}
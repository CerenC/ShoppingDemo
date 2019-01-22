package com.crn.shopping.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.crn.shopping.datasource.CurrencyRepository;
import com.crn.shopping.datasource.local.entity.CurrencyEntity;

import java.util.List;

public class CurrenciesViewModel extends AndroidViewModel {

    final CurrencyRepository currencyRepository = CurrencyRepository.getInstance();

    private static final MutableLiveData ABSENT_CURRENCIES = new MutableLiveData();




    {
        //noinspection unchecked
        ABSENT_CURRENCIES.setValue(null);
    }
    private final MutableLiveData<String> mSelectedCurrencyCode = new MutableLiveData<>();
    private final LiveData<CurrencyEntity> mSelectedCurrency ;
    private final LiveData<List<CurrencyEntity>> mObservableCurrencies;

    public LiveData<CurrencyEntity> getSelectedCurrency() {
        return mSelectedCurrency;
    }

    public void setSelectedCurrencyCode(String currencyCode){
        mSelectedCurrencyCode.setValue(currencyCode);
    }

    public String getSelectedCurrencyCode(){

        return mSelectedCurrencyCode.getValue();
    }
    public LiveData<List<CurrencyEntity>> getCurrencies() {
        return mObservableCurrencies;
    }

    public CurrenciesViewModel(Application application) {
        super(application);

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
        mSelectedCurrency = Transformations.switchMap(mSelectedCurrencyCode, new Function<String, LiveData<CurrencyEntity>>() {
            @Override
            public LiveData<CurrencyEntity> apply(String currencyCode) {
                return currencyRepository.load(currencyCode);
            }
        });

    }

    public void refresh() {
        currencyRepository.reset();
    }
}


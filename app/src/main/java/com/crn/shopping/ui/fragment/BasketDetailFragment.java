package com.crn.shopping.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.model.GoodsItem;
import com.crn.shopping.ui.activity.MainActivity;
import com.crn.shopping.ui.adapter.GoodsAdapter;
import com.crn.shopping.util.AmountFormatter;
import com.crn.shopping.viewmodel.CurrenciesViewModel;
import com.crn.shopping.viewmodel.GoodsViewModel;

import java.util.List;

import shopping.crn.com.shoppingdemo.R;
import shopping.crn.com.shoppingdemo.databinding.BasketDetailFragmentBinding;

/**
 * Created by ceren on 19.06.2017.
 */

public class BasketDetailFragment extends BaseFragment {
    public static final String TAG = GoodsListFragment.class.getSimpleName();
    private GoodsAdapter mAdapter;

    private BasketDetailFragmentBinding mBinding;

    private GoodsViewModel mGoodsViewModel;
    private CurrenciesViewModel mCurrenciesViewModel;

    public BasketDetailFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.basket_detail_fragment, container, false);
        mAdapter = new GoodsAdapter(GoodsAdapter.Type.LIST, null);
        mBinding.itemList.setAdapter(mAdapter);
        return mBinding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setFabVisible(false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGoodsViewModel =
                ViewModelProviders.of(this).get(GoodsViewModel.class);

        mCurrenciesViewModel = ViewModelProviders.of(getActivity()).get(CurrenciesViewModel.class);

        subscribeUi();
    }

    private void subscribeUi() {
        // Update the list when the data changes
        LiveData<List<GoodsItem>> goodsLiveData = mGoodsViewModel.getGoods();
        goodsLiveData.observe(this, new Observer<List<GoodsItem>>() {
            @Override
            public void onChanged(@Nullable List<GoodsItem> goods) {
                if (goods != null) {
                    mBinding.setIsLoading(false);
                    mAdapter.setGoods(goods);
                    mAdapter.notifyDataSetChanged();
                    setTotal();
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });

        LiveData<CurrencyEntity> selectedCurrencyLiveData = mCurrenciesViewModel.getSelectedCurrency();
        selectedCurrencyLiveData.observe(this, new Observer<CurrencyEntity>() {
            @Override
            public void onChanged(@Nullable CurrencyEntity currency) {
                mBinding.setIsLoading(false);
                mAdapter.setCurrency(currency);
                mAdapter.notifyDataSetChanged();
                setTotal();
            }
        });

    }

    private void setTotal() {
        CurrencyEntity selectedCurrency = mCurrenciesViewModel.getSelectedCurrency().getValue();
        String currencyCode = getResources().getString(R.string.eur);
        double rate = 1;
        if(selectedCurrency != null
                && !selectedCurrency.code.equals(currencyCode)) {
            rate = selectedCurrency.rate;
            currencyCode = selectedCurrency.code;
        }

        double total = 0d;
        for (GoodsItem item : mAdapter.getGoods()) {
            if(item.getCount() > 0) {
                total += (item.getPricePerUnit() * item.getCount()) * rate;
            }
        }
        mBinding.totalAmountTextView.setText(getResources().getString(R.string.total_amount, AmountFormatter.getFormattedAmount(total), currencyCode));
    }
}







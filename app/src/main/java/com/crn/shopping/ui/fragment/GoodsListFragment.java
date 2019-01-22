package com.crn.shopping.ui.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.model.GoodsItem;
import com.crn.shopping.ui.activity.MainActivity;
import com.crn.shopping.ui.adapter.GoodsAdapter;
import com.crn.shopping.ui.callback.RefreshCallback;
import com.crn.shopping.viewmodel.CurrenciesViewModel;
import com.crn.shopping.viewmodel.GoodsViewModel;

import java.util.List;

import shopping.crn.com.shoppingdemo.R;
import shopping.crn.com.shoppingdemo.databinding.ListFragmentBinding;

public class GoodsListFragment extends BaseFragment {
    public static final String TAG = GoodsListFragment.class.getSimpleName();
    private GoodsAdapter mAdapter;

    private ListFragmentBinding mBinding;

    private GoodsViewModel mGoodsViewModel;
    private CurrenciesViewModel mCurrenciesViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
        mAdapter = new GoodsAdapter(mClickCallback);
        int spanCount = getResources().getInteger(R.integer.span_count);
        GridLayoutManager glm = new GridLayoutManager(getContext(), spanCount);

        mBinding.itemList.setLayoutManager(glm);
        mBinding.itemList.addItemDecoration(new GridSpacingItemDecoration(spanCount, getResources().getDimension(R.dimen.grid_spacing), true));

        mBinding.itemList.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGoodsViewModel =
                ViewModelProviders.of(this).get(GoodsViewModel.class);

        mCurrenciesViewModel = ViewModelProviders.of(getActivity()).get(CurrenciesViewModel.class);

        subscribeUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setFabVisible(true);
    }

    private void subscribeUi() {
        // Update the list when the data changes
        LiveData<List<GoodsItem>> goodsLiveData = mGoodsViewModel.getGoods();
        goodsLiveData.observe(this, new Observer<List<GoodsItem>>() {
            @Override
            public void onChanged(@Nullable List<GoodsItem> goods) {
                if(goods == null) {
                    mBinding.setIsLoading(true);
                } else {
                    mBinding.setIsLoading(false);
                    mAdapter.setGoods(goods);
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
                
            }
        });

        mBinding.setRefreshCallback(new RefreshCallback() {
            @Override
            public void onRefresh() {
                mBinding.setIsLoading(true);
                mGoodsViewModel.refresh();
                mCurrenciesViewModel.refresh();
            }
        });

        mBinding.swipeContainer.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, float spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing =(int) spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private final GoodsAdapter.ClickCallback mClickCallback= new GoodsAdapter.ClickCallback() {

        @Override
        public void onIncrease(GoodsItem goodsItem, View view) {
            int increaseCount= goodsItem.getCount()+ 1;
            goodsItem.setCount(increaseCount);
            mBinding.itemList.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onDecrease(GoodsItem goodsItem, View view) {
            if(goodsItem.getCount()>0) {
                int decreaseCount= goodsItem.getCount()-1;
                goodsItem.setCount(decreaseCount);
            }
            mBinding.itemList.getAdapter().notifyDataSetChanged();
        }
    };
    public boolean isBasketEmpty(){
        int basketCount=0;
        List<GoodsItem> itemList= mAdapter.getGoods();
        for (GoodsItem item: itemList) {
            basketCount+=item.getCount();
        }
        return basketCount > 0 ? false: true;
    }
}


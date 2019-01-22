package com.crn.shopping.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.model.GoodsItem;

import java.util.ArrayList;
import java.util.List;

import shopping.crn.com.shoppingdemo.R;
import shopping.crn.com.shoppingdemo.databinding.BasketDetailItemBinding;
import shopping.crn.com.shoppingdemo.databinding.GoodsListItemBinding;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder> {
    private List<? extends GoodsItem> mGoodsList;
    private CurrencyEntity mCurrency;
    private Type mType = Type.GRID;
    private final ClickCallback mClickCallback;

    public enum Type {
        GRID, LIST
    }

    public GoodsAdapter(ClickCallback mClickCallback) {
        this.mClickCallback = mClickCallback;
    }

    public GoodsAdapter(Type type, ClickCallback mClickCallback) {
        this.mType = type;
        this.mClickCallback = mClickCallback;
    }
    public void setGoods(final List<? extends GoodsItem> goodsList) {
        List<GoodsItem> goodItemList = new ArrayList<>();
        if (mGoodsList == null) {

            for (GoodsItem item:goodsList) {
                if((mType.equals(Type.LIST) && item.getCount()> 0) || mType.equals(Type.GRID)){
                    goodItemList.add(item);
                }

            }
            mGoodsList = goodItemList;
            notifyItemRangeInserted(0, goodsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mGoodsList.size();
                }

                @Override
                public int getNewListSize() {
                    return goodsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mGoodsList.get(oldItemPosition).getName().equals(
                            goodsList.get(newItemPosition).getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    GoodsItem newItem = goodsList.get(newItemPosition);
                    GoodsItem oldItem = goodsList.get(oldItemPosition);
                    return newItem.getName().equals(oldItem.getName())
                            && newItem.getPriceCurrency().equals(oldItem.getPriceCurrency())
                            && newItem.getPricePerUnit() == oldItem.getPricePerUnit();
                }
            });
            for (GoodsItem item:goodsList) {
                goodItemList.add(item);
            }
            mGoodsList = goodItemList;
            result.dispatchUpdatesTo(this);
        }
    }
    public void setCurrency(CurrencyEntity currency){
            mCurrency=currency;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(Type.GRID.equals(mType)) {
            GoodsListItemBinding binding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.goods_list_item,
                            parent, false);
            binding.setCallback(mClickCallback);
            return new GoodsViewHolder(binding);
        }else{
            BasketDetailItemBinding binding = DataBindingUtil
                    .inflate(LayoutInflater.from(parent.getContext()), R.layout.basket_detail_item,
                            parent, false);
            return new GoodsViewHolder(binding);
        }
    }
    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {
        if(holder.binding instanceof  GoodsListItemBinding){
            ((GoodsListItemBinding) holder.binding).setItem(mGoodsList.get(position));
            ((GoodsListItemBinding) holder.binding).setCurrencyItem(mCurrency);
        }else if(holder.binding instanceof  BasketDetailItemBinding){
            ((BasketDetailItemBinding) holder.binding).setItem(mGoodsList.get(position));
            ((BasketDetailItemBinding) holder.binding).setCurrencyItem(mCurrency);
        }

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mGoodsList == null ? 0 : mGoodsList.size();
    }

    public List<GoodsItem> getGoods() {
        return (List<GoodsItem>) mGoodsList;
    }
    static class GoodsViewHolder extends RecyclerView.ViewHolder {

        final ViewDataBinding binding;


        public GoodsViewHolder(GoodsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public GoodsViewHolder(BasketDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickCallback{
        void onIncrease(GoodsItem goodsItem, View view);
        void onDecrease(GoodsItem goodsItem, View view);
    }
}
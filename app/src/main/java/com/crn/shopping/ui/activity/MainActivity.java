package com.crn.shopping.ui.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.crn.shopping.datasource.local.entity.CurrencyEntity;
import com.crn.shopping.ui.fragment.BasketDetailFragment;
import com.crn.shopping.ui.fragment.GoodsListFragment;
import com.crn.shopping.viewmodel.CurrenciesViewModel;

import java.util.List;

import shopping.crn.com.shoppingdemo.R;

public class MainActivity extends BaseActivity {
    private Menu mMenu;
    private CurrenciesViewModel mCurrenciesViewModel;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            GoodsListFragment fragment = new GoodsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, GoodsListFragment.TAG).commit();
        }

        mCurrenciesViewModel = ViewModelProviders.of(this).get(CurrenciesViewModel.class);
        subscribeUi(mCurrenciesViewModel);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment goodsFragment = MainActivity.this.getSupportFragmentManager().findFragmentByTag(GoodsListFragment.TAG);
                if(goodsFragment instanceof GoodsListFragment){
                    if(((GoodsListFragment) goodsFragment).isBasketEmpty()){
                        Snackbar.make(view, getResources().getString(R.string.no_good_selected_warning), Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }else{
                        BasketDetailFragment newFragment = new BasketDetailFragment();
                        FragmentTransaction transaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.fragment_container, newFragment,newFragment.TAG).addToBackStack(null)
                                .commit();
                        view.setVisibility(View.GONE);
                        MainActivity.this.setTitle(R.string.basket_detail);
                    }
                }
            }
        });
    }
    private void subscribeUi(CurrenciesViewModel viewModel) {
        LiveData<List<CurrencyEntity>> currenciesLiveData = viewModel.getCurrencies();
        currenciesLiveData.observe(this, new Observer<List<CurrencyEntity>>() {
            @Override
            public void onChanged(@Nullable List<CurrencyEntity> currencies) {
                setUpMenu(currencies);
            }
        });
    }
    private void setUpMenu(List<CurrencyEntity> currencies) {
        invalidateOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        mMenu = menu;

        if(mCurrenciesViewModel.getCurrencies() != null && mCurrenciesViewModel.getCurrencies().getValue() != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);

            MenuItem currencyMenuItem = menu.findItem(R.id.change_currency);
            SubMenu currencySubMenu = currencyMenuItem.getSubMenu();

            int index = 0;
            currencySubMenu.add(0, index, index, getResources().getString(R.string.eur));
            index++;
            for (CurrencyEntity currency : mCurrenciesViewModel.getCurrencies().getValue()) {
                currencySubMenu.add(0, index, index, currency.code);
                index++;
            }
            if(mCurrenciesViewModel.getSelectedCurrency().getValue() != null){
                currencyMenuItem.setTitle(mCurrenciesViewModel.getSelectedCurrency().getValue() .code);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selectedMenuItemTitle = item.getTitle().toString();
        if(!selectedMenuItemTitle.equals(mCurrenciesViewModel.getSelectedCurrencyCode())){
            MenuItem currencyMenuItem = getMenu().findItem(R.id.change_currency);
            currencyMenuItem.setTitle(selectedMenuItemTitle);
            mCurrenciesViewModel.setSelectedCurrencyCode(selectedMenuItemTitle);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount()== 0){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
            fab.setVisibility(View.VISIBLE);
            MainActivity.this.setTitle(R.string.app_name);
        }
    }

    public void setFabVisible(boolean visible) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    public Menu getMenu() {
        return mMenu;
    }

}

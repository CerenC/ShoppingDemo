package com.crn.shopping.ui.fragment;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v4.app.Fragment;

/**
 * Created by ceren on 6/17/17.
 */

public class BaseFragment extends Fragment implements LifecycleRegistryOwner {
    protected String getLogTag() {
        return this.getClass().getSimpleName();
    }
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}

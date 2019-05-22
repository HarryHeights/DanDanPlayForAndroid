package com.xyoye.dandanplay2.base;

import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by xyy on 2017/6/23.
 */

public abstract class BaseFragment<T extends BaseMvpPresenter> extends BaseMvpFragment<T> {

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public BaseAppCompatActivity get() {
        return (BaseAppCompatActivity) this.getContext();
    }
}

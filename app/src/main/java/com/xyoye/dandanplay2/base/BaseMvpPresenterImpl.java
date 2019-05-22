package com.xyoye.dandanplay2.base;

import android.content.Context;

import com.xyoye.dandanplay2.app.BaseApplication;
import com.xyoye.dandanplay2.utils.Lifeful;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;

/**
 * mvp中presenter的抽象类
 * Created by xyy on 2017/6/23.
 */
public abstract class BaseMvpPresenterImpl<T extends BaseMvpView> implements BaseMvpPresenter {

    private Context mContext;
    private T view;
    private Lifeful lifeful;

    public BaseMvpPresenterImpl(T view) {
        this.view = view;
    }

    @Deprecated
    public BaseMvpPresenterImpl(Context mContext, T view) {
        this.mContext = mContext;
        this.view = view;
    }

    public BaseMvpPresenterImpl(T view, Lifeful lifeful) {
        this.view = view;
        this.lifeful = lifeful;
    }

    @Deprecated
    public BaseMvpPresenterImpl(Context mContext, T view, Lifeful lifeful) {
        this.mContext = mContext;
        this.view = view;
        this.lifeful = lifeful;
    }

    @Override
    public void initPage() {
        getView().initView();
        getView().initListener();
    }

    @Deprecated
    public Context getContext() {
        return mContext;
    }

    public Context getApplicationContext() {
        return BaseApplication.get_context();
    }

    public T getView() {
        return view;
    }

    public Lifeful getLifeful() {
        return lifeful;
    }
}

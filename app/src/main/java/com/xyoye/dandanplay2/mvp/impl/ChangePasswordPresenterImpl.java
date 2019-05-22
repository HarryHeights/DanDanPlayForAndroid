package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.bean.PersonalBean;
import com.xyoye.dandanplay2.bean.params.ChangePasswordParam;
import com.xyoye.dandanplay2.mvp.presenter.ChangePasswordPresenter;
import com.xyoye.dandanplay2.mvp.view.ChangePasswordView;
import com.xyoye.dandanplay2.utils.Lifeful;
import com.xyoye.dandanplay2.utils.net.CommJsonEntity;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;

/**
 * Created by YE on 2018/8/11.
 */


public class ChangePasswordPresenterImpl extends BaseMvpPresenterImpl<ChangePasswordView> implements ChangePasswordPresenter {

    public ChangePasswordPresenterImpl(ChangePasswordView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {

    }

    @Override
    public void process(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void change(ChangePasswordParam param) {
        getView().showLoading();
        PersonalBean.changePassword(param, new CommJsonObserver<CommJsonEntity>(getLifeful()) {
            @Override
            public void onSuccess(CommJsonEntity commJsonEntity) {
                getView().hideLoading();
                getView().changeSuccess();
            }

            @Override
            public void onError(int errorCode, String message) {
                getView().hideLoading();
                LogUtils.e(message);
                ToastUtils.showShort(message);
            }
        }, new NetworkConsumer());
    }
}

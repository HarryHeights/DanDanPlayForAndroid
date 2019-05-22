package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.bean.RegisterBean;
import com.xyoye.dandanplay2.bean.params.RegisterParam;
import com.xyoye.dandanplay2.mvp.presenter.RegisterPresenter;
import com.xyoye.dandanplay2.mvp.view.RegisterView;
import com.xyoye.dandanplay2.utils.KeyUtil;
import com.xyoye.dandanplay2.utils.Lifeful;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;

/**
 * Created by YE on 2018/8/5.
 */


public class RegisterPresenterImpl extends BaseMvpPresenterImpl<RegisterView> implements RegisterPresenter {

    public RegisterPresenterImpl(RegisterView view, Lifeful lifeful) {
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
    public void register(RegisterParam param) {
        getView().showLoading();
        param.setScreenName(param.getUserName());
        param.setAppId(KeyUtil.getDanDanAppId(getView().getRegisterContext()));
        param.setUnixTimestamp(System.currentTimeMillis()/1000);
        param.buildHash(getView().getRegisterContext());
        RegisterBean.register(param, new CommJsonObserver<RegisterBean>(getLifeful()) {
            @Override
            public void onSuccess(RegisterBean registerBean) {
                getView().hideLoading();
                getView().registerSuccess();
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

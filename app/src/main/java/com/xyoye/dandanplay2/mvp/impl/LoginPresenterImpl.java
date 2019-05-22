package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.bean.PersonalBean;
import com.xyoye.dandanplay2.bean.params.LoginParam;
import com.xyoye.dandanplay2.mvp.presenter.LoginPresenter;
import com.xyoye.dandanplay2.mvp.view.LoginView;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.Lifeful;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;

/**
 * Created by YE on 2018/7/22.
 */

public class LoginPresenterImpl extends BaseMvpPresenterImpl<LoginView> implements LoginPresenter {

    public LoginPresenterImpl(LoginView view, Lifeful lifeful) {
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
    public void login(LoginParam param){
        getView().showLoading();
        PersonalBean.login(param, new CommJsonObserver<PersonalBean>(getLifeful()) {
            @Override
            public void onSuccess(PersonalBean personalBean) {
                getView().hideLoading();
                IApplication.isUpdateUserInfo = true;
                AppConfig.getInstance().setLogin(true);
                AppConfig.getInstance().saveUserScreenName(personalBean.getScreenName());
                AppConfig.getInstance().saveUserName(param.getUserName());
                AppConfig.getInstance().saveUserImage(personalBean.getProfileImage());
                AppConfig.getInstance().saveToken(personalBean.getToken());
                ToastUtils.showShort("登录成功");
                getView().launchMain();
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

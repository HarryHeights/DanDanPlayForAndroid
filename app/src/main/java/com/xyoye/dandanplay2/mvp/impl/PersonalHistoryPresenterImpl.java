package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.bean.PlayHistoryBean;
import com.xyoye.dandanplay2.mvp.presenter.PersonalHistoryPresenter;
import com.xyoye.dandanplay2.mvp.view.PersonalHistoryView;
import com.xyoye.dandanplay2.utils.Lifeful;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;

/**
 * Created by YE on 2018/7/24.
 */


public class PersonalHistoryPresenterImpl extends BaseMvpPresenterImpl<PersonalHistoryView> implements PersonalHistoryPresenter{

    public PersonalHistoryPresenterImpl(PersonalHistoryView view, Lifeful lifeful) {
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
    public void getPlayHistory(){
        getView().showLoading();
        PlayHistoryBean.getPlayHistory(new CommJsonObserver<PlayHistoryBean>(getLifeful()) {
            @Override
            public void onSuccess(PlayHistoryBean playHistoryBean) {
                getView().refreshHistory(playHistoryBean);
                getView().hideLoading();
            }

            @Override
            public void onError(int errorCode, String message) {
                getView().refreshHistory(null);
                getView().hideLoading();
                LogUtils.e(message);
            }
        }, new NetworkConsumer());
    }
}

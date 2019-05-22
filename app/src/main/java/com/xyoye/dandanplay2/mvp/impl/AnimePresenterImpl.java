package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.AnimePresenter;
import com.xyoye.dandanplay2.mvp.view.AnimaView;
import com.xyoye.dandanplay2.utils.Lifeful;

/**
 * Created by YE on 2018/7/15.
 */


public class AnimePresenterImpl extends BaseMvpPresenterImpl<AnimaView> implements AnimePresenter {

    public AnimePresenterImpl(AnimaView view, Lifeful lifeful) {
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
}

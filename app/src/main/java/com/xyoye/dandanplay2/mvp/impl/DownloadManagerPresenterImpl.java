package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.DownloadManagerPresenter;
import com.xyoye.dandanplay2.mvp.view.DownloadManagerView;
import com.xyoye.dandanplay2.utils.Lifeful;

/**
 * Created by YE on 2018/10/27.
 */


public class DownloadManagerPresenterImpl extends BaseMvpPresenterImpl<DownloadManagerView> implements DownloadManagerPresenter {

    public DownloadManagerPresenterImpl(DownloadManagerView view, Lifeful lifeful) {
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

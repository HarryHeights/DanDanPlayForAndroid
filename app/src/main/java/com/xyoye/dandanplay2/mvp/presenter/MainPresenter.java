package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/6/28 0028.
 */


public interface MainPresenter extends BaseMvpPresenter {
    void initTracker();

    void backupBlockData();
}

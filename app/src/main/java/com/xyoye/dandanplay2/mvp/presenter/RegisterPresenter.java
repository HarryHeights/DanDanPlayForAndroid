package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.params.RegisterParam;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/8/5.
 */


public interface RegisterPresenter extends BaseMvpPresenter {
    void register(RegisterParam param);
}

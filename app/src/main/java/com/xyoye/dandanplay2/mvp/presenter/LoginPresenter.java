package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.params.LoginParam;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/7/22.
 */


public interface LoginPresenter extends BaseMvpPresenter {
    void login(LoginParam param);
}

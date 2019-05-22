package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.params.ChangePasswordParam;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/8/11.
 */


public interface ChangePasswordPresenter extends BaseMvpPresenter {
    void change(ChangePasswordParam param);
}

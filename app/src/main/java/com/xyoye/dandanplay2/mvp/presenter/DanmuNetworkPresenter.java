package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.params.DanmuMatchParam;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/7/4 0004.
 */


public interface DanmuNetworkPresenter extends BaseMvpPresenter {
    void matchDanmu(DanmuMatchParam param);

    void searchDanmu(String anime, String episode);
}

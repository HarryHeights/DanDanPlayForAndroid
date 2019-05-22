package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.VideoBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by xyoye on 2019/5/14.
 */

public interface VideoScanPresenter extends BaseMvpPresenter {
    void queryFormSystem(VideoBean videoBean, String path);

    boolean saveNewVideo(VideoBean videoBean);

    void listFolder(String path);
}

package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.LanDeviceBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by xyy on 2018/11/19.
 */

public interface LanDevicePresenter extends BaseMvpPresenter {
    void getLanDevices();

    void searchVideo(String smbUrl);

    void authLan(LanDeviceBean deviceBean, int position, boolean isAdd);
}

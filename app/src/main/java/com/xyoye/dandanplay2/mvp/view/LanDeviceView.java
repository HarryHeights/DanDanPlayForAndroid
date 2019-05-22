package com.xyoye.dandanplay2.mvp.view;

import android.content.Context;

import com.xyoye.dandanplay2.bean.LanDeviceBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by xyy on 2018/11/19.
 */

public interface LanDeviceView extends BaseMvpView, LoadDataView {
    Context getContext();

    void authSuccess(LanDeviceBean deviceBean, int position);

    void searchOver();

    void addDevice(LanDeviceBean deviceBean);

    void refreshDevices(List<LanDeviceBean> devices);
}

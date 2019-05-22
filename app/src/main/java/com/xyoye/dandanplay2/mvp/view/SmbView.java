package com.xyoye.dandanplay2.mvp.view;

import android.content.Context;

import com.xyoye.dandanplay2.bean.SmbBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by YE on 2019/3/30.
 */

public interface SmbView extends BaseMvpView, LoadDataView {
    void refreshSqlDevice(List<SmbBean> deviceList);

    void refreshLanDevice(List<SmbBean> deviceList);

    void refreshSmbFile(List<SmbBean> deviceList, String parentPath);

    Context getContext();
}

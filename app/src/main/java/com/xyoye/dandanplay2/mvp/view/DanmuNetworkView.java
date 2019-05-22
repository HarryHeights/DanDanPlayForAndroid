package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.DanmuMatchBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by YE on 2018/7/4 0004.
 */


public interface DanmuNetworkView extends BaseMvpView , LoadDataView{
    String getVideoPath();

    void refreshAdapter(List<DanmuMatchBean.MatchesBean> matches);
}

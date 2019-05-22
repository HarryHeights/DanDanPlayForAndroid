package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.PlayHistoryBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

/**
 * Created by YE on 2018/7/24.
 */


public interface PersonalHistoryView extends BaseMvpView, LoadDataView {
    void refreshHistory(PlayHistoryBean playHistoryBean);
}

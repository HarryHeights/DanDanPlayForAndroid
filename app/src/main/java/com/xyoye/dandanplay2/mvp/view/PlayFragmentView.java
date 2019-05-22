package com.xyoye.dandanplay2.mvp.view;

import android.content.Context;

import com.xyoye.dandanplay2.bean.FolderBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by YE on 2018/6/29 0029.
 */


public interface PlayFragmentView extends BaseMvpView, LoadDataView {
    void refreshAdapter(List<FolderBean> beans);

    void refreshOver();

    Context getContext();
}

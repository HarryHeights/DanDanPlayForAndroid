package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.FolderBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;

import java.util.List;

/**
 * Created by xyy on 2018/11/21.
 */

public interface LanFolderView extends BaseMvpView {
    void refreshFolder(List<FolderBean> folderBeans);
}

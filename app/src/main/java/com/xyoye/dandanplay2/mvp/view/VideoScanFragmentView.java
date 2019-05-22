package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.ScanFolderBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;

import java.util.List;

/**
 * Created by xyoye on 2019/5/14.
 */

public interface VideoScanFragmentView extends BaseMvpView{
    void updateFolderList(List<ScanFolderBean> folderList);
}

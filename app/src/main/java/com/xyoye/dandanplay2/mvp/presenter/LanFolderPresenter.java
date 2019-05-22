package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by xyy on 2018/11/21.
 */

public interface LanFolderPresenter extends BaseMvpPresenter {
    void getFolders();

    void searchFolder();

    void deleteFolder(String folder);
}

package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.SmbBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2019/3/30.
 */

public interface SmbPresenter extends BaseMvpPresenter {
    void querySqlDevice();

    void queryLanDevice();

    void addSqlDevice(SmbBean smbBean);

    void updateSqlDevice(SmbBean smbBean);

    void loginSmb(SmbBean smbBean);

    void listSmbFolder(SmbBean smbBean);

    void openSmbFile(SmbBean smbBean);

    void returnParentFolder();
}

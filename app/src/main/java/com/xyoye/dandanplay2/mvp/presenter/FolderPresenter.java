package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.VideoBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/6/30 0030.
 */


public interface FolderPresenter extends BaseMvpPresenter {
    void getVideoList(String folderPath);

    void updateDanmu(String danmuPath, int episodeId, String[] whereArgs);

    void deleteFile(String filePath);

    void getDanmu(String videoPath);

    void observeService(VideoBean videoBean);
}

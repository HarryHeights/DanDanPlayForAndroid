package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.DanmuMatchBean;
import com.xyoye.dandanplay2.bean.VideoBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by YE on 2018/6/30 0030.
 */


public interface FolderView extends BaseMvpView, LoadDataView {

    void refreshAdapter(List<VideoBean> beans);

    void downloadDanmu(DanmuMatchBean.MatchesBean matchesBean);

    void noMatchDanmu(String videoPath);

    void openIntentVideo(VideoBean videoBean);
}

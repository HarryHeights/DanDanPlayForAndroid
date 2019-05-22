package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.MagnetBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by YE on 2018/10/13.
 */


public interface SearchMagnetView extends BaseMvpView, LoadDataView {

    void refreshAdapter(List<MagnetBean.ResourcesBean> beanList);

    int getEpisodeId();

    void downloadTorrentOver(String torrentPath, String magnet);

    void showLoading(String text);
}

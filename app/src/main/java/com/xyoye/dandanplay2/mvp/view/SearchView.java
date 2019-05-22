package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.MagnetBean;
import com.xyoye.dandanplay2.bean.SearchHistoryBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by xyy on 2019/1/8.
 */

public interface SearchView extends BaseMvpView, LoadDataView {
    void refreshHistory(List<SearchHistoryBean> historyList, boolean doSearch);

    void refreshSearch(List<MagnetBean.ResourcesBean> searchResult);

    void downloadTorrentOver(String torrentPath, String magnet);

    void downloadExisted(String torrentPath, String magnet);

    String getDownloadFolder();
}

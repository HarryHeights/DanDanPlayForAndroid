package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.AnimeTypeBean;
import com.xyoye.dandanplay2.bean.SubGroupBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

import java.util.List;

/**
 * Created by xyy on 2019/1/8.
 */

public interface SearchPresenter extends BaseMvpPresenter {
    List<AnimeTypeBean.TypesBean> getTypeList();

    List<SubGroupBean.SubgroupsBean> getSubGroupList();

    void getSearchHistory(boolean doSearch);

    void addHistory(String text);

    void updateHistory(int _id);

    void deleteHistory(int _id);

    void deleteAllHistory();

    void search(String text, int type, int subgroup);

    void searchLocalTorrent(String magnet);

    void downloadTorrent(String magnet);
}

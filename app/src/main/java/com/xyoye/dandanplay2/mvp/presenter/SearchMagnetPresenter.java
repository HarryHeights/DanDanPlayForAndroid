package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.AnimeTypeBean;
import com.xyoye.dandanplay2.bean.SubGroupBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

import java.util.List;

/**
 * Created by YE on 2018/10/13.
 */


public interface SearchMagnetPresenter extends BaseMvpPresenter {

    void searchMagnet(String anime, int typeId, int subGroundId);

    void downloadTorrent(String animeTitle, String magnet);

    List<AnimeTypeBean.TypesBean> getTypeList();

    List<SubGroupBean.SubgroupsBean> getSubGroupList();
}

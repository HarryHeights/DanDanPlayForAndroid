package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.AnimeDetailBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

/**
 * Created by YE on 2018/7/20.
 */


public interface AnimeDetailView extends BaseMvpView, LoadDataView{
    void showAnimeDetail(AnimeDetailBean detailBean);

    void afterFollow(boolean isFollow);
}

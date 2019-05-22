package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

/**
 * Created by YE on 2018/7/20.
 */


public interface AnimeDetailPresenter extends BaseMvpPresenter {

    void getAnimeDetail(String animeId);

    void followConfirm(String animeId);

    void followCancel(String animeId);
}

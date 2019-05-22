package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.bean.AnimeBean;
import com.xyoye.dandanplay2.bean.BangumiBean;
import com.xyoye.dandanplay2.bean.SeasonAnimeBean;
import com.xyoye.dandanplay2.mvp.presenter.AnimaSeasonPresenter;
import com.xyoye.dandanplay2.mvp.view.AnimaSeasonView;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.Lifeful;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.xyoye.dandanplay2.ui.activities.AnimeSeasonActivity.SORT_FOLLOW;
import static com.xyoye.dandanplay2.ui.activities.AnimeSeasonActivity.SORT_NAME;

/**
 * Created by xyy on 2019/1/9.
 */

public class AnimaSeasonPresenterImpl extends BaseMvpPresenterImpl<AnimaSeasonView> implements AnimaSeasonPresenter {

    public AnimaSeasonPresenterImpl(AnimaSeasonView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {

    }

    @Override
    public void process(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void getSeasonAnima(int year, int month) {
        getView().showLoading();
        SeasonAnimeBean.getSeasonAnimas(year+"", month+"", new CommJsonObserver<BangumiBean>() {
            @Override
            public void onSuccess(BangumiBean bangumiBean) {
                getView().hideLoading();
                if (bangumiBean != null){
                    int sortType = AppConfig.getInstance().getSeasonSortType();
                    if (!AppConfig.getInstance().isLogin() && sortType == 0){
                        sortType = SORT_NAME;
                    }
                    sortAnima(bangumiBean.getBangumiList(), sortType);
                    getView().refreshAnimas(bangumiBean.getBangumiList());
                }
                else
                    getView().refreshAnimas(new ArrayList<>());
            }

            @Override
            public void onError(int errorCode, String message) {
                getView().hideLoading();
                getView().showError(message);
                LogUtils.e(message);
            }
        }, new NetworkConsumer());
    }

    @Override
    public void sortAnima(List<AnimeBean> animaList, int sortType){
        if (sortType == SORT_FOLLOW){
            Collections.sort(animaList, (o1, o2) ->
                    Boolean.compare(o2.isIsFavorited(), o1.isIsFavorited()));
        }else if (sortType == SORT_NAME){
            Collections.sort(animaList, (o1, o2) ->
                    Collator.getInstance(Locale.CHINESE).compare( o1.getAnimeTitle(), o2.getAnimeTitle()));
        }else {
            Collections.sort(animaList, (o1, o2) ->
                    Double.compare(o2.getRating(), o1.getRating()));
        }
    }
}

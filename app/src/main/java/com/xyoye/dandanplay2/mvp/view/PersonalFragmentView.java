package com.xyoye.dandanplay2.mvp.view;

import android.app.Activity;

import com.xyoye.dandanplay2.bean.AnimeFavoriteBean;
import com.xyoye.dandanplay2.bean.PlayHistoryBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;

/**
 * Created by YE on 2018/6/29 0029.
 */


public interface PersonalFragmentView extends BaseMvpView {

    void refreshFavorite(AnimeFavoriteBean favoriteBean);

    void refreshHistory(PlayHistoryBean historyBean);

    void refreshUI(AnimeFavoriteBean favoriteBean, PlayHistoryBean historyBean);

    Activity getActivity();
}

package com.xyoye.dandanplay2.mvp.presenter;

import com.xyoye.dandanplay2.bean.AnimeBean;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;

import java.util.List;

/**
 * Created by xyy on 2019/1/9.
 */

public interface AnimaSeasonPresenter extends BaseMvpPresenter {

    void getSeasonAnima(int year, int month);

    void sortAnima(List<AnimeBean> animaList, int sortType);
}

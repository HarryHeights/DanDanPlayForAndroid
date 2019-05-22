package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.AnimeBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;
import com.xyoye.dandanplay2.utils.interf.view.LoadDataView;

import java.util.List;

/**
 * Created by xyy on 2019/1/9.
 */

public interface AnimaSeasonView extends BaseMvpView, LoadDataView{

    void refreshAnimas(List<AnimeBean> animas);
}

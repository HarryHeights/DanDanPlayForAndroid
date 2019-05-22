package com.xyoye.dandanplay2.mvp.view;

import com.xyoye.dandanplay2.bean.BangumiBean;
import com.xyoye.dandanplay2.utils.interf.view.BaseMvpView;

import java.util.List;

/**
 * Created by YE on 2018/6/29 0029.
 */


public interface HomeFragmentView extends BaseMvpView{
    void setBanners(List<String> images, List<String> titles, List<String> urls);

    void initViewPager(List<BangumiBean> beans);

    void refreshUI(List<String> images, List<String> titles, List<String> urls, List<BangumiBean> beans);
}

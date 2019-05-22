package com.xyoye.dandanplay2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseFragment;
import com.xyoye.dandanplay2.base.BaseRvAdapter;
import com.xyoye.dandanplay2.bean.AnimeBean;
import com.xyoye.dandanplay2.bean.BangumiBean;
import com.xyoye.dandanplay2.mvp.impl.AnimePresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.AnimePresenter;
import com.xyoye.dandanplay2.mvp.view.AnimaView;
import com.xyoye.dandanplay2.ui.weight.ScrollableHelper;
import com.xyoye.dandanplay2.ui.weight.item.AnimeItem;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by YE on 2018/7/15.
 */


public class AnimeFragment extends BaseFragment<AnimePresenter> implements ScrollableHelper.ScrollableContainer, AnimaView {
    @BindView(R.id.bangumi_list_recycler_view)
    RecyclerView recyclerView;

    public static AnimeFragment newInstance(BangumiBean bangumiBean){
        AnimeFragment animeFragment = new AnimeFragment();
        Bundle args = new Bundle();
        args.putSerializable("anima", bangumiBean);
        animeFragment.setArguments(args);
        return animeFragment;
    }

    @NonNull
    @Override
    protected AnimePresenter initPresenter() {
        return new AnimePresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutId() {
        return R.layout.fragment_anime;
    }

    @Override
    public void initView() {
        BangumiBean bangumiBean;
        Bundle args = getArguments();
        if (args == null) return;
        bangumiBean = (BangumiBean)getArguments().getSerializable("anima");
        if (bangumiBean ==null) return;

        List<AnimeBean> bangumiList = bangumiBean.getBangumiList();

        if (AppConfig.getInstance().isLogin()){
            Collections.sort(bangumiList, (o1, o2) -> {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                if (o1.isIsFavorited()) return -1;
                if (o2.isIsFavorited()) return 1;
                return 0;
            });
        }
        BaseRvAdapter<AnimeBean> adapter = new BaseRvAdapter<AnimeBean>(bangumiList) {
            @NonNull
            @Override
            public AdapterItem<AnimeBean> onCreateItem(int viewType) {
                return new AnimeItem();
            }
        };

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3){});
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

}

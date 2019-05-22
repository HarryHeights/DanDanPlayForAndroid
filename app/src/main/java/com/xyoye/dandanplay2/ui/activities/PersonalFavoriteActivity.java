package com.xyoye.dandanplay2.ui.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseMvpActivity;
import com.xyoye.dandanplay2.base.BaseRvAdapter;
import com.xyoye.dandanplay2.bean.AnimeFavoriteBean;
import com.xyoye.dandanplay2.mvp.impl.PersonalPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.PersonalFavoritePresenter;
import com.xyoye.dandanplay2.mvp.view.PeronalFavoriteView;
import com.xyoye.dandanplay2.ui.weight.item.PersonalFavoriteAnimaItem;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import butterknife.BindView;

/**
 * Created by YE on 2018/7/24.
 */


public class PersonalFavoriteActivity extends BaseMvpActivity<PersonalFavoritePresenter> implements PeronalFavoriteView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void initView() {
        setTitle("我的关注");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        presenter.getFavorite();
    }

    @Override
    public void initListener() {

    }

    @NonNull
    @Override
    protected PersonalFavoritePresenter initPresenter() {
        return new PersonalPresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_personal_favorite;
    }

    @Override
    public void refreshFavorite(AnimeFavoriteBean animeFavoriteBean) {
        if (animeFavoriteBean != null){
            BaseRvAdapter<AnimeFavoriteBean.FavoritesBean> adapter = new BaseRvAdapter<AnimeFavoriteBean.FavoritesBean>(animeFavoriteBean.getFavorites()) {
                @NonNull
                @Override
                public AdapterItem<AnimeFavoriteBean.FavoritesBean> onCreateItem(int viewType) {
                    return new PersonalFavoriteAnimaItem();
                }
            };
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showError(String message) {

    }
}

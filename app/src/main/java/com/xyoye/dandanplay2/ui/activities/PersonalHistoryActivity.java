package com.xyoye.dandanplay2.ui.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseMvpActivity;
import com.xyoye.dandanplay2.base.BaseRvAdapter;
import com.xyoye.dandanplay2.bean.PlayHistoryBean;
import com.xyoye.dandanplay2.mvp.impl.PersonalHistoryPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.PersonalHistoryPresenter;
import com.xyoye.dandanplay2.mvp.view.PersonalHistoryView;
import com.xyoye.dandanplay2.ui.weight.item.PersonalPlayHistoryItem;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import butterknife.BindView;

/**
 * Created by YE on 2018/7/24.
 */

public class PersonalHistoryActivity extends BaseMvpActivity<PersonalHistoryPresenter> implements PersonalHistoryView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private BaseRvAdapter<PlayHistoryBean.PlayHistoryAnimesBean> adapter;

    @Override
    public void initView() {
        setTitle("播放历史");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        presenter.getPlayHistory();
    }

    @Override
    public void initListener() {

    }

    @NonNull
    @Override
    protected PersonalHistoryPresenter initPresenter() {
        return new PersonalHistoryPresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_personal_history;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void refreshHistory(PlayHistoryBean playHistoryBean) {
        if (playHistoryBean != null){
            adapter = new BaseRvAdapter<PlayHistoryBean.PlayHistoryAnimesBean>(playHistoryBean.getPlayHistoryAnimes()) {
                @NonNull
                @Override
                public AdapterItem<PlayHistoryBean.PlayHistoryAnimesBean> onCreateItem(int viewType) {
                    return new PersonalPlayHistoryItem();
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

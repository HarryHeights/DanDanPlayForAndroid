package com.xyoye.dandanplay2.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.ui.weight.dialog.BaseLoadingDialog;
import com.xyoye.dandanplay2.utils.interf.presenter.BaseMvpPresenter;


/**
 * Created by xyy on 2017/6/23.
 */

public abstract class BaseMvpActivity<T extends BaseMvpPresenter> extends BaseAppCompatActivity {

    protected T presenter;
    protected Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = initPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        presenter.init();
    }

    @Override
    public void initPageView() {
        presenter.initPage();
    }

    @Override
    public void initPageViewListener() {

    }

    @Override
    protected void process(Bundle savedInstanceState) {
        super.process(savedInstanceState);
        presenter.process(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        dialog = null;
        super.onDestroy();
    }

    @Override
    protected int getToolbarColor() {
        return ContextCompat.getColor(this, R.color.theme_color);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getToolbarColor(), 0);
    }

    public void showLoadingDialog() {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new BaseLoadingDialog(this);
            dialog.show();
        }
    }

    public void showLoadingDialog(String msg) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new BaseLoadingDialog(this, msg);
            dialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 返回主持人
     */
    protected abstract @NonNull
    T initPresenter();
}

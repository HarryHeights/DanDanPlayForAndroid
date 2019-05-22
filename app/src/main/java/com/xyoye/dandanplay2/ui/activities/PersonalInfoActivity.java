package com.xyoye.dandanplay2.ui.activities;

import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.base.BaseMvpActivity;
import com.xyoye.dandanplay2.bean.event.ChangeScreenNameEvent;
import com.xyoye.dandanplay2.mvp.impl.PersonalInfoPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.PersonalInfoPresenter;
import com.xyoye.dandanplay2.mvp.view.PersonalInfoView;
import com.xyoye.dandanplay2.ui.weight.dialog.CommonEditTextDialog;
import com.xyoye.dandanplay2.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by YE on 2018/7/23.
 */


public class PersonalInfoActivity extends BaseMvpActivity<PersonalInfoPresenter> implements PersonalInfoView,View.OnClickListener {
    @BindView(R.id.login_out_bt)
    Button loginOutBt;
    @BindView(R.id.screen_name_rl)
    RelativeLayout screenNameRl;
    @BindView(R.id.password_rl)
    RelativeLayout passwordRl;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.screen_name_tv)
    TextView screenNameTv;


    @Override
    public void initView() {
        setTitle("个人信息");
        if (AppConfig.getInstance().isLogin()){
            String screenName = AppConfig.getInstance().getUserScreenName();
            String userName = AppConfig.getInstance().getUserName();

            screenNameTv.setText(screenName);
            userNameTv.setText(userName);
        }else {
            ToastUtils.showShort("请先登录再进行此操作");
        }
    }

    @Override
    public void initListener() {
        screenNameRl.setOnClickListener(this);
        passwordRl.setOnClickListener(this);

        loginOutBt.setOnClickListener(v -> {
            if (AppConfig.getInstance().isLogin()){
                AppConfig.getInstance().setLogin(false);
                AppConfig.getInstance().saveUserName("");
                AppConfig.getInstance().saveUserScreenName("");
                AppConfig.getInstance().saveUserImage("");
                AppConfig.getInstance().saveToken("");
                IApplication.isUpdateUserInfo = true;

                launchActivity(LoginActivity.class);
                PersonalInfoActivity.this.finish();
            }else {
                ToastUtils.showShort("请先登录再进行此操作");
            }

        });
    }

    @NonNull
    @Override
    protected PersonalInfoPresenter initPresenter() {
        return new PersonalInfoPresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_personal_info;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.screen_name_rl:
                CommonEditTextDialog dialog = new CommonEditTextDialog(PersonalInfoActivity.this, R.style.Dialog, CommonEditTextDialog.SCREEN_NAME);
                dialog.show();
                break;
            case R.id.password_rl:
                launchActivity(ChangePasswordActivity.class);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeScreenNameEvent event){
        screenNameTv.setText(event.getScreenName());
        IApplication.isUpdateUserInfo = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}

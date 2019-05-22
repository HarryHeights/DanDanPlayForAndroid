package com.xyoye.dandanplay2.ui.weight.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.xyoye.dandanplay2.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YE on 2018/8/5.
 */


public class ToLoginDialog extends Dialog {
    @BindView(R.id.tips_tv)
    TextView tipsTv;

    private ActionSuccessListener listener;

    private int dex;

    public ToLoginDialog(@NonNull Context context, int themeResId, int dex, ActionSuccessListener listener) {
        super(context, themeResId);
        this.dex = dex;
        this.listener = listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_to_login);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        switch (dex) {
            case 0:
                tipsTv.setText("弹弹play，注册成功！");
                break;
            case 1:
                tipsTv.setText("重置密码成功，请前往邮箱查看临时密码");
                break;
            case 2:
                tipsTv.setText("修改密码成功，请重新登录");
                break;
        }
    }

    @OnClick({R.id.confirm_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_tv:
                ToLoginDialog.this.cancel();
                listener.onSuccess();
                break;
        }
    }

    public interface ActionSuccessListener{
        void onSuccess();
    }
}

package com.xyoye.dandanplay2.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.bean.event.SearchDanmuEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xyy on 2018/10/24.
 */

public class SearchDanmuDialog extends Dialog {

    @BindView(R.id.episode_type_rg)
    RadioGroup episodeTypeRg;
    @BindView(R.id.episode_rb)
    RadioButton episodeRb;
    @BindView(R.id.ova_rb)
    RadioButton ovaRb;
    @BindView(R.id.episode_et)
    EditText episodeEt;
    @BindView(R.id.anime_et)
    EditText animeEt;

    public SearchDanmuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_danmu);
        ButterKnife.bind(this);

        episodeTypeRg.check(episodeRb.getId());

        episodeRb.setOnClickListener(v -> {
            episodeEt.setEnabled(true);
            ovaRb.setChecked(false);
        });

        ovaRb.setOnClickListener(v -> {
            episodeEt.setEnabled(false);
            episodeRb.setChecked(false);
        });
    }

    @OnClick({R.id.cancel_tv, R.id.confirm_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_tv:
                String anime = animeEt.getText().toString().trim();
                String episode;
                if (episodeRb.isChecked()){
                    episode = episodeEt.getText().toString().trim();
                }else {
                    episode = "movie";
                }
                EventBus.getDefault().post(new SearchDanmuEvent(anime, episode));
                break;
        }
        SearchDanmuDialog.this.dismiss();
    }
}

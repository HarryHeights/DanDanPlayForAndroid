package com.xyoye.dandanplay2.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseMvcActivity;
import com.xyoye.dandanplay2.ui.weight.dialog.PlayerSettingDialog;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xyy on 2018/9/29.
 */

public class PlayerSettingActivity extends BaseMvcActivity {

    @BindView(R.id.player_type_tv)
    TextView playerTypeTv;
    @BindView(R.id.ijk_setting_ll)
    LinearLayout ijkSettingLL;
    @BindView(R.id.media_code_c_cb)
    CheckBox mediaCodeCCb;
    @BindView(R.id.open_sl_es_cb)
    CheckBox openSLESCb;
    @BindView(R.id.media_code_c_h265_cb)
    CheckBox mediaCodeCH265Cb;
    @BindView(R.id.surface_renders_cb)
    CheckBox surfaceRendersCb;
    @BindView(R.id.pixel_format_tv)
    TextView pixelFormatTv;
    @BindView(R.id.outer_china_danmu_cb)
    CheckBox outerChinaDanmuCb;
    @BindView(R.id.auto_load_danmu_cb)
    CheckBox autoLoadDanmuCb;
    @BindView(R.id.network_subtitle_cb)
    CheckBox networkSubtitleCb;
    @BindView(R.id.auto_load_local_subtitle_cb)
    CheckBox autoLoadLocalSubtitleCb;
    @BindView(R.id.auto_load_network_subtitle_cb)
    CheckBox autoLoadNetworkSubtitleCb;
    @BindView(R.id.auto_load_network_subtitle_rl)
    RelativeLayout autoLoadNetworkSubtitleRl;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_player_setting;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initPageView() {
        setTitle("播放器设置");

        int playerType = AppConfig.getInstance().getPlayerType();
        switch (playerType) {
            case com.player.commom.utils.Constants.EXO_PLAYER:
                playerTypeTv.setText("EXO Player");
                ijkSettingLL.setVisibility(View.GONE);
                break;
            case com.player.commom.utils.Constants.IJK_ANDROID_PLAYER:
                playerTypeTv.setText("AndroidMedia Player");
                ijkSettingLL.setVisibility(View.VISIBLE);
                break;
            case com.player.commom.utils.Constants.IJK_PLAYER:
            default:
                playerTypeTv.setText("IJK Player");
                ijkSettingLL.setVisibility(View.VISIBLE);
                break;
        }
        String pixelType = AppConfig.getInstance().getPixelFormat();
        switch (pixelType) {
            case Constants.PIXEL_RGB565:
                pixelFormatTv.setText("RGB 565");
                break;
            case Constants.PIXEL_RGB888:
                pixelFormatTv.setText("RGB 888");
                break;
            case Constants.PIXEL_RGBX8888:
                pixelFormatTv.setText("RGBX 8888");
                break;
            case Constants.PIXEL_YV12:
                pixelFormatTv.setText("YV12");
                break;
            case Constants.PIXEL_OPENGL_ES2:
                pixelFormatTv.setText("OpenGL ES2");
                break;
            case Constants.PIXEL_AUTO:
            default:
                pixelFormatTv.setText("默认");
                break;
        }
        boolean mediaCodeC = AppConfig.getInstance().isOpenMediaCodeC();
        boolean mediaCodeCH265 = AppConfig.getInstance().isOpenMediaCodeCH265();
        boolean openSLES = AppConfig.getInstance().isOpenSLES();
        boolean surfaceRenders = AppConfig.getInstance().isSurfaceRenders();
        boolean outerChinaDialog = AppConfig.getInstance().isShowOuterChainDanmuDialog();
        boolean autoLoadDanmu = AppConfig.getInstance().isAutoLoadDanmu();
        boolean useNetworkSubtitle = AppConfig.getInstance().isUseNetWorkSubtitle();
        boolean autoLoadLocalSubtitle = AppConfig.getInstance().isAutoLoadLocalSubtitle();
        boolean autoLoadNetworkSubtitle = AppConfig.getInstance().isAutoLoadNetworkSubtitle();
        mediaCodeCCb.setChecked(mediaCodeC);
        mediaCodeCH265Cb.setChecked(mediaCodeCH265);
        openSLESCb.setChecked(openSLES);
        surfaceRendersCb.setChecked(surfaceRenders);
        outerChinaDanmuCb.setChecked(outerChinaDialog);
        autoLoadDanmuCb.setChecked(autoLoadDanmu);
        networkSubtitleCb.setChecked(useNetworkSubtitle);
        autoLoadLocalSubtitleCb.setChecked(autoLoadLocalSubtitle);
        autoLoadNetworkSubtitleCb.setChecked(autoLoadNetworkSubtitle);
        if (useNetworkSubtitle){
            autoLoadNetworkSubtitleRl.setVisibility(View.VISIBLE);
        }else {
            autoLoadNetworkSubtitleRl.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPageViewListener() {
        mediaCodeCCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setOpenMediaCodeC(isChecked));
        mediaCodeCH265Cb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setOpenMediaCodeCH265(isChecked));
        openSLESCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setOpenSLES(isChecked));
        surfaceRendersCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setSurfaceRenders(isChecked));
        outerChinaDanmuCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setShowOuterChainDanmuDialog(isChecked));
        autoLoadDanmuCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setAutoLoadDanmu(isChecked));
        autoLoadLocalSubtitleCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setAutoLoadLocalSubtitle(isChecked));
        autoLoadNetworkSubtitleCb.setOnCheckedChangeListener((buttonView, isChecked) ->
                AppConfig.getInstance().setAutoLoadNetworkSubtitle(isChecked));
        networkSubtitleCb.setOnCheckedChangeListener((buttonView, isChecked) ->{
            AppConfig.getInstance().setUseNetWorkSubtitle(isChecked);
            autoLoadNetworkSubtitleRl.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (!isChecked){
                autoLoadNetworkSubtitleCb.setChecked(false);
                AppConfig.getInstance().setAutoLoadNetworkSubtitle(false);
            }
        });
    }

    @OnClick({R.id.select_player_type_ll, R.id.select_pixel_format_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_player_type_ll:
                new PlayerSettingDialog(
                        PlayerSettingActivity.this,
                        true,
                        result -> {
                            if (result.equals("EXO Player")){
                                ijkSettingLL.setVisibility(View.GONE);
                            }else {
                                ijkSettingLL.setVisibility(View.VISIBLE);
                            }
                            playerTypeTv.setText(result);
                        }
                ).show();
                break;
            case R.id.select_pixel_format_ll:
                new PlayerSettingDialog(
                        PlayerSettingActivity.this,
                        false,
                        result ->
                            pixelFormatTv.setText(result)
                ).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.setting_tips:
                startActivity(new Intent(PlayerSettingActivity.this, PlayerSettingTipsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player_setting_tips, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

package com.xyoye.dandanplay2.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.ui.weight.dialog.DanmuSelectDialog;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xyoye on 2019/4/26.
 */

public class PlayerManagerActivity extends AppCompatActivity {
    private static final int SELECT_DANMU = 102;

    private String videoPath;
    private String videoTitle;
    private String danmuPath;
    private String searchWord;
    private long currentPosition;
    private int episodeId;
    private boolean isOnline;

    @BindView(R.id.error_tv)
    TextView errorTv;
    @BindView(R.id.back_iv)
    ImageView backIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_manager);
        setFullscreen();
        ButterKnife.bind(this);

        errorTv.setVisibility(View.GONE);
        backIv.setOnClickListener(v -> PlayerManagerActivity.this.finish());

        initIntent();
    }

    private void initIntent() {
        Intent openIntent = getIntent();
        videoTitle = openIntent.getStringExtra("video_title");
        videoPath = openIntent.getStringExtra("video_path");
        danmuPath = openIntent.getStringExtra("danmu_path");
        searchWord = openIntent.getStringExtra("search_word");
        currentPosition = openIntent.getLongExtra("current_position", 0);
        episodeId = openIntent.getIntExtra("episode_id", 0);
        isOnline = openIntent.getBooleanExtra("is_online", false);

        boolean isSmbPlay = openIntent.getBooleanExtra("smb_play", false);

        //外部打开 或 smb播放
        if (Intent.ACTION_VIEW.equals(openIntent.getAction()) || isSmbPlay || isOnline) {
            Uri data;
            if (!isOnline) {
                data = getIntent().getData();
                if (data == null) {
                    ToastUtils.showShort("解析视频地址失败");
                    errorTv.setVisibility(View.VISIBLE);
                    return;
                }
                videoPath = CommonUtils.getRealFilePath(PlayerManagerActivity.this, data);
                videoTitle = FileUtils.getFileName(videoPath);
            }

            if (videoPath != null) {
                //是否展示前往选择弹幕弹窗
                if (AppConfig.getInstance().isShowOuterChainDanmuDialog()) {
                    new DanmuSelectDialog(this, isSelectDanmu -> {
                        if (isSelectDanmu) {
                            launchDanmuSelect(videoPath);
                        }else {
                            launchPlayerActivity();
                        }
                    }).show();
                } else {
                    if (AppConfig.getInstance().isOuterChainDanmuSelect()) {
                        launchDanmuSelect(videoPath);
                    } else {
                        launchPlayerActivity();
                    }
                }
            } else {
                ToastUtils.showShort("解析视频地址失败");
                errorTv.setVisibility(View.VISIBLE);
            }
        } else {
            launchPlayerActivity();
        }
    }

    private void launchDanmuSelect(String videoPath) {
        Intent intent = new Intent(PlayerManagerActivity.this, DanmuNetworkActivity.class);
        intent.putExtra("video_path", videoPath);
        intent.putExtra("is_lan", false);
        intent.putExtra("is_online", isOnline);
        intent.putExtra("search_word", searchWord);
        startActivityForResult(intent, SELECT_DANMU);
    }

    private void launchPlayerActivity() {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("video_title", videoTitle);
        intent.putExtra("video_path", videoPath);
        intent.putExtra("danmu_path", danmuPath);
        intent.putExtra("current_position", currentPosition);
        intent.putExtra("episode_id", episodeId);
        intent.putExtra("is_online", isOnline);
        this.startActivity(intent);
        PlayerManagerActivity.this.finish();
    }

    private void setFullscreen() {
        View decorView = this.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_DANMU) {
                danmuPath = data.getStringExtra("path");
                episodeId = data.getIntExtra("episode_id", 0);
                launchPlayerActivity();
            }
        }
    }

    public static void launchPlayerOnline(Context context, String title, String path, String searchWord, long position, int episodeId) {
        Intent intent = new Intent(context, PlayerManagerActivity.class);
        intent.putExtra("video_title", title);
        intent.putExtra("video_path", path);
        intent.putExtra("danmu_path", "");
        intent.putExtra("search_word", searchWord);
        intent.putExtra("current_position", position);
        intent.putExtra("episode_id", episodeId);
        intent.putExtra("is_online", true);
        context.startActivity(intent);
    }

    public static void launchPlayer(Context context, String title, String path, String danmu, long position, int episodeId) {
        Intent intent = new Intent(context, PlayerManagerActivity.class);
        intent.putExtra("video_title", title);
        intent.putExtra("video_path", path);
        intent.putExtra("danmu_path", danmu);
        intent.putExtra("current_position", position);
        intent.putExtra("episode_id", episodeId);
        context.startActivity(intent);
    }

    public static void launchPlayerSmb(Context context, String title, String path) {
        Intent intent = new Intent(context, PlayerManagerActivity.class);
        intent.putExtra("video_title", title);
        intent.putExtra("video_path", path);
        intent.putExtra("danmu_path", "");
        intent.putExtra("current_position", 0);
        intent.putExtra("episode_id", 0);
        intent.putExtra("smb_play", true);
        context.startActivity(intent);
    }
}

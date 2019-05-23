package com.xyoye.dandanplay2.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.player.commom.bean.SubtitleBean;
import com.player.commom.listener.OnDanmakuListener;
import com.player.commom.listener.PlayerViewListener;
import com.player.commom.receiver.BatteryBroadcastReceiver;
import com.player.commom.receiver.PlayerReceiverListener;
import com.player.commom.receiver.ScreenBroadcastReceiver;
import com.player.commom.utils.Constants;
import com.player.danmaku.danmaku.model.BaseDanmaku;
import com.player.exoplayer.ExoPlayerView;
import com.player.ijkplayer.IjkPlayerView_V2;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.bean.PlayHistoryBean;
import com.xyoye.dandanplay2.bean.UploadDanmuBean;
import com.xyoye.dandanplay2.bean.event.SaveCurrentEvent;
import com.xyoye.dandanplay2.bean.params.DanmuUploadParam;
import com.xyoye.dandanplay2.database.DataBaseInfo;
import com.xyoye.dandanplay2.database.DataBaseManager;
import com.xyoye.dandanplay2.ui.weight.dialog.FileManagerDialog;
import com.xyoye.dandanplay2.ui.weight.dialog.SelectSubtitleDialog;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.HashUtils;
import com.xyoye.dandanplay2.utils.SubtitleConverter;
import com.xyoye.dandanplay2.utils.net.CommJsonEntity;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.CommOtherDataObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;
import com.xyoye.dandanplay2.utils.net.RetroFactory;
import com.xyoye.dandanplay2.utils.net.RetrofitService;
import com.xyoye.dandanplay2.utils.net.okhttp.OkHttpEngine;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by YE on 2018/7/4 0004.
 */

public class PlayerActivity extends AppCompatActivity implements PlayerReceiverListener {
    PlayerViewListener mPlayer;

    private boolean isInit = false;
    private String videoPath;
    private String videoTitle;
    private String danmuPath;
    private long currentPosition;
    private int episodeId;
    private List<SubtitleBean> subtitleList;
    private boolean isOnline = false;

    //电源广播
    private BatteryBroadcastReceiver batteryReceiver;
    //锁屏广播
    private ScreenBroadcastReceiver screenReceiver;
    //弹幕回调
    private OnDanmakuListener onDanmakuListener;
    //内部事件回调
    private IMediaPlayer.OnOutsideListener onOutsideListener;
    //字幕查询观察者
    CommOtherDataObserver<List<SubtitleBean>> subtitleObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //播放器类型
        if (AppConfig.getInstance().getPlayerType() == com.player.commom.utils.Constants.EXO_PLAYER){
            mPlayer = new ExoPlayerView(this);
            setContentView((ExoPlayerView)mPlayer);
        }else {
            mPlayer = new IjkPlayerView_V2(this);
            setContentView((IjkPlayerView_V2)mPlayer);
        }

        //隐藏toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        //沉浸式状态栏
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //开启屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        subtitleList = new ArrayList<>();

        //注册监听广播
        batteryReceiver = new BatteryBroadcastReceiver(this);
        screenReceiver = new ScreenBroadcastReceiver(this);
        PlayerActivity.this.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        PlayerActivity.this.registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        //获取播放参数
        videoPath = getIntent().getStringExtra("video_path");
        videoTitle = getIntent().getStringExtra("video_title");
        danmuPath = getIntent().getStringExtra("danmu_path");
        currentPosition = getIntent().getLongExtra("current_position", 0);
        episodeId = getIntent().getIntExtra("episode_id", 0);
        isOnline = getIntent().getBooleanExtra("is_online", false);

        //初始化接口
        initListener();

        //初始化播放器
        initPlayer();
    }

    //初始化播放器
    private void initPlayer() {
        //获取弹幕数据流
        InputStream inputStream = null;
        if (!TextUtils.isEmpty(danmuPath) && FileUtils.isFileExists(danmuPath)) {
            try {
                inputStream = new FileInputStream(new File(danmuPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        //初始化不同的播放器
        if (AppConfig.getInstance().getPlayerType() == com.player.commom.utils.Constants.EXO_PLAYER) {
            initExoPlayer(inputStream);
        }else {
            initIjkPlayer(inputStream);
        }

        isInit = true;
        //添加播放历史
        if (episodeId > 0 && AppConfig.getInstance().isLogin())
            addPlayHistory(episodeId);
    }

    private void initListener(){
        onDanmakuListener = new OnDanmakuListener() {
            @Override
            public boolean isValid() {
                //是否可发送弹幕
                if (!AppConfig.getInstance().isLogin()){
                    ToastUtils.showShort("当前未登陆，不能发送弹幕");
                    return false;
                }
                if (episodeId == 0){
                    ToastUtils.showShort("当前弹幕不支持发送弹幕");
                }
                return true;
            }

            @Override
            public void onDataObtain(BaseDanmaku data) {
                //上传弹幕
                uploadDanmu(data);
            }

            @Override
            public void setCloudFilter(boolean isOpen) {
                //启用或关闭云屏蔽
                AppConfig.getInstance().setCloudDanmuFilter(isOpen);
            }

            @Override
            public void deleteBlock(String text) {
                DataBaseManager.getInstance()
                        .selectTable(13)
                        .delete()
                        .where(1, text)
                        .postExecute();
            }

            @Override
            public void addBlock(String text) {
                DataBaseManager.getInstance()
                        .selectTable(13)
                        .insert()
                        .param(1, text)
                        .postExecute();
            }
        };

        onOutsideListener = (what, extra) -> {
            switch (what){
                //选择本地字幕
                case Constants.INTENT_OPEN_SUBTITLE:
                    new FileManagerDialog(
                            PlayerActivity.this,
                            videoPath,
                            FileManagerDialog.SELECT_SUBTITLE,
                            path -> mPlayer.setSubtitlePath(path)
                    ).show();
                    break;
                //查询网络字幕
                case Constants.INTENT_QUERY_SUBTITLE:
                    querySubtitle(videoPath);
                    break;
                //选择网络字幕
                case Constants.INTENT_SELECT_SUBTITLE:
                    new SelectSubtitleDialog(
                            PlayerActivity.this,
                            subtitleList,
                            PlayerActivity.this::downloadSubtitle
                    ).show();
                    break;
                //自动选择网络字幕
                case Constants.INTENT_AUTO_SUBTITLE:
                    ToastUtils.showShort("自动加载字幕中");
                    SubtitleBean subtitleBean = subtitleList.get(0);
                    downloadSubtitle(subtitleBean.getName(), subtitleBean.getUrl());
                    break;
                //保存进度
                case Constants.INTENT_SAVE_CURRENT:
                    //更新内存中进度
                    SaveCurrentEvent event = new SaveCurrentEvent();
                    event.setCurrentPosition(extra);
                    event.setFolderPath(FileUtils.getDirName(videoPath));
                    event.setVideoPath(videoPath);
                    EventBus.getDefault().post(event);
                    //更新数据库中进度
                    SQLiteDatabase sqLiteDatabase = DataBaseManager.getInstance().getSQLiteDatabase();
                    ContentValues values = new ContentValues();
                    values.put("current_position", event.getCurrentPosition());
                    String whereCase = DataBaseInfo.getFieldNames()[2][1]+" =? AND "+ DataBaseInfo.getFieldNames()[2][2]+" =? ";
                    sqLiteDatabase.update(DataBaseInfo.getTableNames()[2], values, whereCase, new String[]{event.getFolderPath(), event.getVideoPath()});
                    break;
            }
        };

        //网络字幕请求回调
        subtitleObserver = new CommOtherDataObserver<List<SubtitleBean>>() {
            @Override
            public void onSuccess(List<SubtitleBean> subtitleList) {
                if (subtitleList.size() > 0){
                    //按评分排序
                    Collections.sort(subtitleList, (o1, o2) -> o2.getRank()-o1.getRank());
                    PlayerActivity.this.subtitleList.clear();
                    PlayerActivity.this.subtitleList.addAll(subtitleList);
                    mPlayer.onSubtitleQuery(subtitleList.size());
                    LogUtils.e("获取字幕成功，共"+subtitleList.size()+"条");
                }
            }

            @Override
            public void onError(int errorCode, String message) {

            }
        };
    }

    private void initIjkPlayer(InputStream inputStream){
        IjkPlayerView_V2 ijkPlayerView = (IjkPlayerView_V2) mPlayer;

        ijkPlayerView
                //设置弹幕事件回调，要在初始化弹幕之前完成
                .setDanmakuListener(onDanmakuListener)
                //内部事件回调
                .setOnInfoListener(onOutsideListener)
                //设置普通屏蔽弹幕
                .setNormalFilterData(IApplication.normalFilterList)
                //设置云屏蔽数据
                .setCloudFilterData(IApplication.cloudFilterList,
                        AppConfig.getInstance().isCloudDanmuFilter())
                //设置弹幕数据源
                .setDanmakuSource(inputStream)
                //默认展示弹幕
                .showOrHideDanmaku(true)
                //跳转至上一次播放进度
                .setSkipTip(currentPosition)
                //是否开启网络字幕
                .setNetworkSubtitle(!isOnline && AppConfig.getInstance().isUseNetWorkSubtitle())
                //是否自动加载同名字幕
                .setAutoLoadLocalSubtitle(!isOnline && AppConfig.getInstance().isAutoLoadLocalSubtitle())
                //是否自动加载网络字幕
                .setAutoLoadNetworkSubtitle(!isOnline && AppConfig.getInstance().isAutoLoadNetworkSubtitle())
                //设置标题
                .setTitle(videoTitle)
                //设置视频路径
                .setVideoPath(videoPath)
                .start();
    }

    private void initExoPlayer(InputStream inputStream){
        ExoPlayerView exoPlayerView = (ExoPlayerView)mPlayer;

        exoPlayerView
                //设置弹幕事件回调，要在初始化弹幕之前完成
                .setDanmakuListener(onDanmakuListener)
                //内部事件回调
                .setOnInfoListener(onOutsideListener)
                //设置普通屏蔽弹幕
                .setNormalFilterData(IApplication.normalFilterList)
                //设置云屏蔽数据
                .setCloudFilterData(IApplication.cloudFilterList,
                        AppConfig.getInstance().isCloudDanmuFilter())
                //设置弹幕数据源
                .setDanmakuSource(inputStream)
                //默认展示弹幕
                .showOrHideDanmaku(true)
                //跳转至上一次播放进度
                .setSkipTip(currentPosition)
                //是否开启网络字幕
                .setNetworkSubtitle(!isOnline && AppConfig.getInstance().isUseNetWorkSubtitle())
                //是否自动加载同名字幕
                .setAutoLoadLocalSubtitle(!isOnline && AppConfig.getInstance().isAutoLoadLocalSubtitle())
                //是否自动加载网络字幕
                .setAutoLoadNetworkSubtitle(!isOnline && AppConfig.getInstance().isAutoLoadNetworkSubtitle())
                //设置标题
                .setTitle(videoTitle)
                //设置视频路径
                .setVideoPath(videoPath)
                .start();
    }

    //上传一条弹幕
    private void uploadDanmu(BaseDanmaku data) {
        double dTime = new BigDecimal(data.getTime() / 1000)
                .setScale(3, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        String time = dTime + "";
        int type = data.getType();
        if (type != 1 && type != 4 && type != 5) {
            type = 1;
        }
        String mode = type + "";
        String color = (data.textColor & 0x00FFFFFF) + "";
        String comment = String.valueOf(data.text);
        DanmuUploadParam uploadParam = new DanmuUploadParam(time, mode, color, comment);
        UploadDanmuBean.uploadDanmu(uploadParam, episodeId + "", new CommJsonObserver<UploadDanmuBean>() {
            @Override
            public void onSuccess(UploadDanmuBean bean) {
                LogUtils.d("upload danmu success: text：" + data.text + "  cid：" + bean.getCid());
            }

            @Override
            public void onError(int errorCode, String message) {
                ToastUtils.showShort(message);
            }
        }, new NetworkConsumer());
    }

    //增加播放历史
    private void addPlayHistory(int episodeId){
        if (episodeId > 0){
            PlayHistoryBean.addPlayHistory(episodeId, new CommJsonObserver<CommJsonEntity>() {
                @Override
                public void onSuccess(CommJsonEntity commJsonEntity) {
                    LogUtils.d("add history success: episodeId：" + episodeId);
                }

                @Override
                public void onError(int errorCode, String message) {
                    LogUtils.e("add history fail: episodeId：" + episodeId+"  message："+message);
                }
            }, new NetworkConsumer());
        }
    }

    @Override
    protected void onDestroy() {
        mPlayer.onDestroy();
        this.unregisterReceiver(batteryReceiver);
        this.unregisterReceiver(screenReceiver);
        if (subtitleObserver != null)
            subtitleObserver.dispose();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (isInit)
            mPlayer.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isInit)
            mPlayer.onPause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mPlayer.handleVolumeKey(keyCode) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayer.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayer.configurationChanged(newConfig);
    }

    @Override
    public void onBatteryChanged(int status, int progress) {
        mPlayer.setBatteryChanged(status, progress);
    }

    @Override
    public void onScreenLocked() {
        mPlayer.onScreenLocked();
    }

    //查询字幕
    private void querySubtitle(String videoPath){
        String thunderHash = HashUtils.getFileSHA1(videoPath);
        Map<String, String> shooterParams = new HashMap<>();
        shooterParams.put("filehash", HashUtils.getFileHash(videoPath));
        shooterParams.put("pathinfo", FileUtils.getFileName(videoPath));
        shooterParams.put("format", "json");
        shooterParams.put("lang", "Chn");
        RetrofitService service = RetroFactory.getSubtitleInstance();
        service.queryThunder(thunderHash)
                .zipWith(service.queryShooter(shooterParams), (thunder, shooters) ->
                        SubtitleConverter.transform(thunder, shooters, videoPath))
                .doOnSubscribe(new NetworkConsumer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subtitleObserver);
    }

    //下载字幕
    private void downloadSubtitle(String fileName, String link){
        Request request = new Request.Builder().url(link).build();
        Call call = OkHttpEngine.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ToastUtils.showShort("下载字幕文件失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() != null){
                    String folderPath = AppConfig.getInstance().getDownloadFolder() + com.xyoye.dandanplay2.utils.Constants.DefaultConfig.subtitleFolder;
                    File folder = new File(folderPath);
                    if (!folder.exists()){
                        folder.mkdirs();
                    }
                    String filePath = folderPath + "/" + fileName;
                    boolean isSaveFile = FileIOUtils.writeFileFromIS(filePath, response.body().byteStream());
                    if (isSaveFile){
                        runOnUiThread(() -> mPlayer.setSubtitlePath(filePath));

                    }else {
                        ToastUtils.showShort("保存字幕文件失败");
                    }
                }
            }
        });
    }
}

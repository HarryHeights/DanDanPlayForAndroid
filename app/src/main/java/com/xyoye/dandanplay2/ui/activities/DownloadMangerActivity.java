package com.xyoye.dandanplay2.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.XLConstant;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.base.BaseMvpActivity;
import com.xyoye.dandanplay2.base.BaseRvAdapter;
import com.xyoye.dandanplay2.bean.event.MessageEvent;
import com.xyoye.dandanplay2.bean.event.TorrentBindDanmuEndEvent;
import com.xyoye.dandanplay2.bean.event.TorrentBindDanmuStartEvent;
import com.xyoye.dandanplay2.mvp.impl.DownloadManagerPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.DownloadManagerPresenter;
import com.xyoye.dandanplay2.mvp.view.DownloadManagerView;
import com.xyoye.dandanplay2.service.TorrentService;
import com.xyoye.dandanplay2.ui.weight.item.DownloadManagerItem;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

import butterknife.BindView;

/**
 * Created by YE on 2018/10/27.
 */

public class DownloadMangerActivity extends BaseMvpActivity<DownloadManagerPresenter> implements DownloadManagerView {
    private static final int DIALOG_BIND_DANMU = 1001;

    @BindView(R.id.download_rv)
    RecyclerView downloadRv;

    private BaseRvAdapter<Long> adapter;
    private Runnable refresh;
    private Handler mHandler = IApplication.getMainHandler();

    @Override
    public void initView() {
        setTitle("下载管理");
        downloadRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        downloadRv.setNestedScrollingEnabled(false);
        downloadRv.setItemViewCacheSize(10);
        ((SimpleItemAnimator)downloadRv.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new BaseRvAdapter<Long>(IApplication.taskIdList) {
            @NonNull
            @Override
            public AdapterItem<Long> onCreateItem(int viewType) {
                return new DownloadManagerItem();
            }
        };
        downloadRv.setAdapter(adapter);

        initRefresh();

        initTask();
    }

    private void initTask(){
//        Intent intent = getIntent();
//        String torrentPath = intent.getStringExtra("torrent_path");
//        String animeTitle = intent.getStringExtra("anime_title");
//        List<Integer> indexList = intent.getIntegerArrayListExtra("torrent_indexes");
//        int[] indexes = new int[indexList.size()];
//
//        for (int i=0; i<indexList.size(); i++){
//            indexes[i] = indexList.get(i);
//        }
//
//        String animeFolder = StringUtils.isEmpty(animeTitle) ? "/" : "/"+animeTitle+"/";
//
//        TorrentInfo torrentInfo = XLTaskHelper.getInstance().getTorrentInfo(torrentPath);
//        String saveFolderPath = AppConfig.getInstance().getDownloadFolder();
//        if (torrentInfo.mIsMultiFiles){
//            saveFolderPath = saveFolderPath + animeFolder + torrentInfo.mMultiFileBaseFolder;
//        }else {
//            String folderName = FileUtils.getFileNameNoExtension(torrentInfo.mSubFileInfo[0].mFileName);
//            saveFolderPath = saveFolderPath + animeFolder + folderName;
//        }
//
//
//        long taskId = XLTaskHelper.getInstance().addNewTorrentTask(torrentPath, saveFolderPath, indexes);
//
//        if (taskId == -1){
//            ToastUtils.showShort("创建任务失败");
//            return;
//        }
//
//        XLTaskInfo taskInfo = XLTaskHelper.getInstance().getTaskInfo(taskId);
//        taskInfo.mSourceUrl = torrentPath;
//        taskInfo.mSaveFolder = saveFolderPath;
//        taskInfo.mIndexes = indexes;
//        IApplication.taskIdList.add(taskId);
//        IApplication.taskInfoList.put(taskId, taskInfo);
    }

    private void initRefresh(){
        refresh = () -> {
            adapter.notifyDataSetChanged();
            mHandler.postDelayed(refresh,1000);
        };
        refresh.run();
    }

    @Override
    public void initListener() {

    }

    @NonNull
    @Override
    protected DownloadManagerPresenter initPresenter() {
        return new DownloadManagerPresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_download_manager;
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
        ToastUtils.showShort(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(refresh);

        boolean downloading = false;
        for (long taskId : IApplication.taskIdList) {
            XLTaskInfo taskInfo = XLTaskHelper.getInstance().getTaskInfo(taskId);
            if (taskInfo.mDownloadSize == taskInfo.mFileSize) continue;
            if (taskInfo.mTaskStatus == XLConstant.XLTaskStatus.TASK_RUNNING) {
                downloading = true;
                break;
            }
        }
        if (!downloading){
            if (ServiceUtils.isServiceRunning(TorrentService.class))
                ServiceUtils.stopService(TorrentService.class);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_download:
//                new CommonDialog.Builder(DownloadMangerActivity.this)
//                        .hideCancel()
//                        .setAutoDismiss()
//                        .build()
//                        .show(getResources().getString(R.string.about_download), "关于下载", "确定", "");
                break;
            case R.id.all_start:
                //EventBus.getDefault().post(new TorrentEvent(TorrentEvent.EVENT_ALL_START, -1));
                break;
            case R.id.all_pause:
                //EventBus.getDefault().post(new TorrentEvent(TorrentEvent.EVENT_ALL_PAUSE, -1));
                break;
            case R.id.all_delete:
//                new CommonDialog.Builder(this)
//                        .showExtra()
//                        .setAutoDismiss()
//                        .setOkListener(dialog ->
//                                EventBus.getDefault().post(new TorrentEvent(TorrentEvent.EVENT_ALL_DELETE_TASK, -1)))
//                        .setExtraListener(dialog ->
//                                EventBus.getDefault().post(new TorrentEvent(TorrentEvent.EVENT_ALL_DELETE_FILE, -1)))
//                        .build()
//                        .show("确认删除所有任务？","删除任务和文件");
                break;
            case R.id.tracker_manager:
                //startActivity(new Intent(DownloadMangerActivity.this, TrackerActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_download_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == DIALOG_BIND_DANMU){
                int episodeId = data.getIntExtra("episode_id", -1);
                String danmuPath = data.getStringExtra("path");
                int position = data.getIntExtra("position", -1);
                if (position != -1){
                    TorrentBindDanmuEndEvent bindDanmuEndEvent = new TorrentBindDanmuEndEvent();
                    bindDanmuEndEvent.setDanmuPath(danmuPath);
                    bindDanmuEndEvent.setEpisodeId(episodeId);
                    bindDanmuEndEvent.setPosition(position);
                    EventBus.getDefault().post(bindDanmuEndEvent);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event){
        if (event.getMsg() == MessageEvent.UPDATE_DOWNLOAD_MANAGER)
            adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TorrentBindDanmuStartEvent event){
        Intent intent = new Intent(DownloadMangerActivity.this, DanmuNetworkActivity.class);
        intent.putExtra("video_path", event.getPath());
        intent.putExtra("position", event.getPosition());
        startActivityForResult(intent, DIALOG_BIND_DANMU);
    }
}

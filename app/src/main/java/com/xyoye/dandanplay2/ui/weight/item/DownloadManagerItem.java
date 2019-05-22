package com.xyoye.dandanplay2.ui.weight.item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xunlei.downloadlib.XLDownloadManager;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.XLConstant;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.ui.activities.PlayerManagerActivity;
import com.xyoye.dandanplay2.utils.CommonUtils;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;
import com.xyoye.dandanplay2.utils.thunder.TorrentUtils;

import butterknife.BindView;

/**
 * Created by YE on 2018/10/27.
 */

public class DownloadManagerItem implements AdapterItem<Long> {

    @BindView(R.id.download_title_tv)
    TextView downloadTitleTv;
    @BindView(R.id.download_duration_pb)
    ProgressBar downloadDurationPb;
    @BindView(R.id.download_speed_tv)
    TextView downloadSpeedTv;
    @BindView(R.id.download_duration_tv)
    TextView downloadDurationTv;
    @BindView(R.id.download_info_rl)
    RelativeLayout downloadInfoRl;
    @BindView(R.id.download_status_iv)
    ImageView downloadStatusIv;
    @BindView(R.id.download_status_tv)
    TextView downloadStatusTv;
    @BindView(R.id.download_ctrl_rl)
    RelativeLayout downloadCtrlRl;

    private Context context;
    private String statusStr;

    @Override
    public int getLayoutResId() {
        return R.layout.item_download_torrent;
    }

    @Override
    public void initItemViews(View itemView) {
        context = itemView.getContext();
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Long taskId, int position) {
        XLTaskInfo liveTask = XLTaskHelper.getInstance().getTaskInfo(taskId);
        XLTaskInfo staticTask = IApplication.taskInfoList.get(taskId);
        TorrentInfo torrentInfo = XLTaskHelper.getInstance().getTorrentInfo(staticTask.mSourceUrl);

        int progress = getProgress(liveTask);
        String duration = getDuration(liveTask) + "（" + progress + "%）";
        String title = torrentInfo.mIsMultiFiles ? torrentInfo.mMultiFileBaseFolder : torrentInfo.mSubFileInfo[0].mFileName;
        String speed = "↓ "+TorrentUtils.convertFileSize(liveTask.mDownloadSpeed) +"/s";

        downloadTitleTv.setText(title);
        downloadDurationPb.setProgress(progress);
        downloadSpeedTv.setText(speed);
        downloadDurationTv.setText(duration);
        setStatus(liveTask);

        downloadInfoRl.setOnLongClickListener(v -> {

            return true;
        });

        downloadInfoRl.setOnClickListener(v ->  {
            if (!torrentInfo.mIsMultiFiles){
                String filePath = staticTask.mSaveFolder + "/" + title;
                String playUrl = XLTaskHelper.getInstance().getLoclUrl(filePath);
                PlayerManagerActivity.launchPlayer(context, "边下边播测试", playUrl, "", 0, 0);
            }
        });

        downloadCtrlRl.setOnClickListener(v -> {
            if (liveTask.mTaskStatus == XLConstant.XLTaskStatus.TASK_SUCCESS ||
                    liveTask.mTaskStatus == XLConstant.XLTaskStatus.TASK_FAILED)
                return;
            if (liveTask.mTaskStatus != XLConstant.XLTaskStatus.TASK_RUNNING){
                long newTaskId = XLTaskHelper.getInstance().resumeTask(staticTask);
                //替换旧ID
                IApplication.taskIdList.set(position, newTaskId);
                IApplication.taskInfoList.remove(taskId);
                IApplication.taskInfoList.put(newTaskId, staticTask);
                ToastUtils.showShort("旧id:"+taskId+"  新id:"+newTaskId);
            }else {
                XLTaskHelper.getInstance().pauseTask(liveTask.mTaskId);
            }
        });
    }

    private int getProgress(XLTaskInfo task) {
        if (task.mDownloadSize == task.mFileSize)
            return 100;
        return (int)(task.mDownloadSize * 100 / task.mFileSize);
    }

    private String getDuration(XLTaskInfo task) {
        return CommonUtils.convertFileSize(task.mDownloadSize) + "/" + CommonUtils.convertFileSize(task.mFileSize);
    }

    private void setStatus(XLTaskInfo task) {
        switch (task.mTaskStatus) {
            case XLConstant.XLTaskStatus.TASK_IDLE:
                downloadStatusIv.setImageResource(R.mipmap.ic_download_wait);
                downloadStatusTv.setTextColor(context.getResources().getColor(R.color.text_gray));
                downloadStatusTv.setText("队列中");
                statusStr = "queued";
                break;
            case XLConstant.XLTaskStatus.TASK_RUNNING:
                downloadStatusIv.setImageResource(R.mipmap.ic_download_start);
                downloadStatusTv.setTextColor(context.getResources().getColor(R.color.theme_color));
                downloadStatusTv.setText("下载中");
                statusStr = "seeding";
                break;
            case XLConstant.XLTaskStatus.TASK_STOPPED:
                downloadStatusIv.setImageResource(R.mipmap.ic_download_pause);
                downloadStatusTv.setTextColor(context.getResources().getColor(R.color.theme_color));
                downloadStatusTv.setText("已暂停");
                statusStr = "paused";
                break;
            case XLConstant.XLTaskStatus.TASK_FAILED:
                downloadStatusIv.setImageResource(R.mipmap.ic_download_error);
                downloadStatusTv.setTextColor(context.getResources().getColor(R.color.ared));
                downloadStatusTv.setText("错误");
                statusStr = "error";
                break;
            case XLConstant.XLTaskStatus.TASK_SUCCESS:
                downloadStatusIv.setImageResource(R.mipmap.ic_download_over);
                downloadStatusTv.setTextColor(context.getResources().getColor(R.color.bilibili_pink));
                downloadStatusTv.setText("已完成");
                statusStr = "done";
                break;
            default:
                downloadStatusIv.setImageResource(R.mipmap.ic_download_error);
                downloadStatusTv.setTextColor(context.getResources().getColor(R.color.ared));
                downloadStatusTv.setText("未知码");
                statusStr = "UnKnow";
                break;
        }
    }
}

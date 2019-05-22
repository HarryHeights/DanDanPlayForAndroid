package com.xunlei.downloadlib;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.blankj.utilcode.util.LogUtils;
import com.xunlei.downloadlib.parameter.BtIndexSet;
import com.xunlei.downloadlib.parameter.BtSubTaskDetail;
import com.xunlei.downloadlib.parameter.BtTaskParam;
import com.xunlei.downloadlib.parameter.GetFileName;
import com.xunlei.downloadlib.parameter.GetTaskId;
import com.xunlei.downloadlib.parameter.InitParam;
import com.xunlei.downloadlib.parameter.TorrentFileInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;
import com.xunlei.downloadlib.parameter.XLTaskLocalUrl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oceanzhang on 2017/7/27.
 */

public class XLTaskHelper {
    private AtomicInteger seq = new AtomicInteger(0);

    public static void init(Context context) {
        XLDownloadManager instance = XLDownloadManager.getInstance();
        InitParam initParam = new InitParam();
        initParam.mAppKey = "xzNjAwOQ^^yb==aa214316d5e0a63a5b58db24557fa2^e";
        initParam.mAppVersion = "5.21.2.4170";
        initParam.mStatSavePath = context.getFilesDir().getPath();
        initParam.mStatCfgSavePath = context.getFilesDir().getPath();
        initParam.mPermissionLevel = 2;
        instance.init(context, initParam);
        instance.setOSVersion(Build.VERSION.INCREMENTAL);
        instance.setSpeedLimit(-1, -1);
        XLDownloadManager.getInstance().setUserId("");
    }

    private static class HelperHolder{
        private static XLTaskHelper xlTaskHelper = new XLTaskHelper();
    }

    private XLTaskHelper(){

    }

    public static XLTaskHelper getInstance(){
        return HelperHolder.xlTaskHelper;
    }

    /**
     * 获取任务详情， 包含当前状态，下载进度，下载速度，文件大小
     * mDownloadSize:已下载大小  mDownloadSpeed:下载速度 mFileSize:文件总大小 mTaskStatus:当前状态，0连接中1下载中 2下载完成 3失败 mAdditionalResDCDNSpeed DCDN加速 速度
     * @param taskId
     * @return
     */
    public synchronized XLTaskInfo getTaskInfo(long taskId) {
        XLTaskInfo taskInfo = new XLTaskInfo();
        XLDownloadManager.getInstance().getTaskInfo(taskId,1,taskInfo);
        return taskInfo;
    }

    /**
     * 获取种子详情
     * @param torrentPath
     * @return
     */
    public synchronized TorrentInfo getTorrentInfo(String torrentPath) {
        TorrentInfo torrentInfo = new TorrentInfo();
        XLDownloadManager xlDownloadManager = XLDownloadManager.getInstance();
        xlDownloadManager.getTorrentInfo(torrentPath,torrentInfo);
        return torrentInfo;
    }

    public synchronized long addNewTorrentTask(String torrentPath, String savePath, int[]indexes){
        return addTorrentTask(torrentPath, savePath, indexes, 1);
    }

    /**
     * 添加种子下载任务,如果是磁力链需要先通过addMagentTask将种子下载下来
     * @param torrentPath 种子地址
     * @param savePath 保存路径
     * @param indexes 需要下载的文件索引
     * @return
     */
    private synchronized long addTorrentTask(String torrentPath, String savePath, int[]indexes, int createMode ){
        TorrentInfo torrentInfo = new TorrentInfo();
        XLDownloadManager.getInstance().getTorrentInfo(torrentPath,torrentInfo);
        TorrentFileInfo[] fileInfos = torrentInfo.mSubFileInfo;
        BtTaskParam taskParam = new BtTaskParam();
        taskParam.setCreateMode(createMode);
        taskParam.setFilePath(savePath);
        taskParam.setMaxConcurrent(3);
        taskParam.setSeqId(seq.incrementAndGet());
        taskParam.setTorrentPath(torrentPath);
        GetTaskId getTaskId = new GetTaskId();
        int errorCode = XLDownloadManager.getInstance().createBtTask(taskParam,getTaskId);
        LogUtils.e("创建任务code："+errorCode+" msg："+getErrorMsg(errorCode));
        if(fileInfos.length > 1 && indexes != null && indexes.length > 0) {
            BtIndexSet btIndexSet = new BtIndexSet(indexes.length);
            int i = 0;
            for(int index : indexes) {
                btIndexSet.mIndexSet[i++] = index;
            }
            XLDownloadManager.getInstance().selectBtSubTask(getTaskId.getTaskId(),btIndexSet);
        }
        XLDownloadManager.getInstance().setTaskLxState(getTaskId.getTaskId(), 0, 1);
        XLDownloadManager.getInstance().startTask(getTaskId.getTaskId(), false);
        return getTaskId.getTaskId();
    }

    /**
     * 获取某个文件的本地proxy url,如果是音视频文件可以实现变下边播
     * @param filePath
     * @return
     */
    public synchronized String getLoclUrl(String filePath) {
        XLTaskLocalUrl localUrl = new XLTaskLocalUrl();
        XLDownloadManager.getInstance().getLocalUrl(filePath, localUrl);
        return localUrl.mStrUrl;
    }

    /**
     * 唤醒任务
     */
    public synchronized long resumeTask(XLTaskInfo taskInfo) {
        String torrentPath = taskInfo.mSourceUrl;
        String savePath = taskInfo.mSaveFolder;
        int[] indexes = taskInfo.mIndexes;
        XLDownloadManager.getInstance().releaseTask(taskInfo.mTaskId);
        return addTorrentTask(torrentPath, savePath, indexes, 1);
    }

    /**
     * 暂停任务
     */
    public synchronized int pauseTask(long taskId) {
        return XLDownloadManager.getInstance().stopTask(taskId);
    }

    /**
     * 停止任务 文件保留
     * @param taskId
     */
    public synchronized void stopTask(long taskId) {
        XLDownloadManager.getInstance().stopTask(taskId);
        XLDownloadManager.getInstance().releaseTask(taskId);
    }

    /**
     * 删除一个任务，会把文件也删掉
     * @param taskId
     * @param savePath
     */
    public synchronized void deleteTask(long taskId,final String savePath) {
        stopTask(taskId);
        new Handler(Daemon.looper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    new LinuxFileCommand(Runtime.getRuntime()).deleteDirectory(savePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 通过链接获取文件名
     * @param url
     * @return
     */
    public synchronized String getFileName(String url) {
        if (url.startsWith("thunder://")) url = XLDownloadManager.getInstance().parserThunderUrl(url);
        GetFileName getFileName = new GetFileName();
        XLDownloadManager.getInstance().getFileNameFromUrl(url, getFileName);
        return getFileName.getFileName();
    }

    /**
     * 获取种子文件子任务的详情
     * @param taskId
     * @param fileIndex
     * @return
     */
    public synchronized BtSubTaskDetail getBtSubTaskInfo(long taskId, int fileIndex) {
        BtSubTaskDetail subTaskDetail = new BtSubTaskDetail();
        XLDownloadManager.getInstance().getBtSubTaskInfo(taskId,fileIndex,subTaskDetail);
        return subTaskDetail;
    }

    /**
     * 开启dcdn加速
     * @param taskId
     * @param btFileIndex
     */
    public synchronized void startDcdn(long taskId,int btFileIndex) {
        XLDownloadManager.getInstance().startDcdn(taskId, btFileIndex, "", "", "");
    }


    /**
     * 停止dcdn加速
     * @param taskId
     * @param btFileIndex
     */
    public synchronized void stopDcdn(long taskId,int btFileIndex) {
        XLDownloadManager.getInstance().stopDcdn(taskId,btFileIndex);
    }

    /**
     * 根据XLTaskInfo的errorCode获取错误信息
     * @param errorCode
     */
    public synchronized String getErrorMsg(int errorCode){
        return XLDownloadManager.getInstance().getErrorCodeMsg(errorCode);
    }
}

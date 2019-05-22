package com.xyoye.dandanplay2.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.blankj.utilcode.util.LogUtils;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.ui.activities.DownloadMangerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by xyy on 2018/10/23.
 */

public class TorrentService extends Service {
    private int NOTIFICATION_ID = 1;

    private NotificationManager notificationManager;
    private Handler mHandler = IApplication.getMainHandler();
    private Runnable refresh;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartEvent(){

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("TorrentService onCreate");
        EventBus.getDefault().register(TorrentService.this);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("com.xyoye.dandanplay2.torrentservice.downloadchannel", "下载任务", NotificationManager.IMPORTANCE_LOW);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.enableLights(false);
            channel.setSound(null, null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        startForeground(NOTIFICATION_ID, showNotification());


        refresh = () -> {

            mHandler.postDelayed(refresh,1000);
        };
        refresh.run();
    }

    private Notification showNotification(){
        return null;
    }

    private Notification buildNotification(String downloadStatus, int doneTask, String downLoadSpeed, String uploadSpeed) {

        String msg = downloadStatus;
        msg += " 进度："+doneTask+"/"+0;
        msg += " · ↓"+downLoadSpeed+"/s";
        msg += " · ↑"+uploadSpeed+"/s";

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, DownloadMangerActivity.class),
                0);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("弹弹play")
                .setContentText(msg)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setVibrate(new long[]{0})
                .setSound(null);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.xyoye.dandanplay2.torrentservice.downloadchannel");
        }
        Notification notify = builder.build();
        notify.flags = Notification.FLAG_FOREGROUND_SERVICE ;

        return notify;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("TorrentService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("TorrentService onDestroy");
        mHandler.removeCallbacks(refresh);
        notificationManager.cancel(NOTIFICATION_ID);
        EventBus.getDefault().unregister(TorrentService.this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("TorrentService onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.i("TorrentService onUnbind");
        return super.onUnbind(intent);
    }
}

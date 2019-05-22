package com.xyoye.dandanplay2.bean.event;

/**
 * Created by xyy on 2018/11/22.
 */

public class MessageEvent {
    public final static int UPDATE_DOWNLOAD_MANAGER = 1001;
    public final static int UPDATE_LAN_FOLDER = 1002;
    public final static int UPDATE_TRACKER = 1003;
    public final static int EVENT_BIND_DANMU = 1004;

    private int msg;

    public MessageEvent(int msg){
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}

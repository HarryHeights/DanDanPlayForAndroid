package com.xyoye.dandanplay2.bean.event;

/**
 * Created by xyy on 2018/11/1.
 */

public class RefreshFolderEvent {
    private boolean isReGetData;

    public RefreshFolderEvent(boolean isReGetData) {
        this.isReGetData = isReGetData;
    }

    public boolean isReGetData() {
        return isReGetData;
    }

    public void setReGetData(boolean reGetData) {
        isReGetData = reGetData;
    }
}

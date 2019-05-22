package com.xyoye.dandanplay2.bean;

import com.xyoye.dandanplay2.bean.params.DanmuUploadParam;
import com.xyoye.dandanplay2.utils.net.CommJsonEntity;
import com.xyoye.dandanplay2.utils.net.CommJsonObserver;
import com.xyoye.dandanplay2.utils.net.NetworkConsumer;
import com.xyoye.dandanplay2.utils.net.RetroFactory;

import java.io.Serializable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xyy on 2018/10/9.
 */

public class UploadDanmuBean  extends CommJsonEntity implements Serializable {
    private String cid;

    public UploadDanmuBean(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public static void uploadDanmu(DanmuUploadParam param, String episodeId, CommJsonObserver<UploadDanmuBean> observer, NetworkConsumer consumer){
        RetroFactory.getInstance().uploadDanmu(param.getMap(), episodeId)
                .doOnSubscribe(consumer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

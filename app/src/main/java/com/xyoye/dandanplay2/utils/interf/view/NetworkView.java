package com.xyoye.dandanplay2.utils.interf.view;

/**
 * 网络状态处理接口
 * Created by xyy on 2017/6/23.
 */
public interface NetworkView{

    /**
     * 网络正常
     */
    void normalNetwork();

    /**
     * 网络异常
     */
    void networkAnomaly();
}

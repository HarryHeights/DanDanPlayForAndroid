package com.xyoye.dandanplay.mvp.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.xyoye.dandanplay.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay.bean.ScanFolderBean;
import com.xyoye.dandanplay.bean.event.RefreshFolderEvent;
import com.xyoye.dandanplay.database.DataBaseInfo;
import com.xyoye.dandanplay.database.DataBaseManager;
import com.xyoye.dandanplay.mvp.presenter.VideoScanFragmentPresenter;
import com.xyoye.dandanplay.mvp.view.VideoScanFragmentView;
import com.xyoye.dandanplay.utils.Lifeful;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyoye on 2019/5/14.
 */

public class VideoScanFragmentPresenterImpl extends BaseMvpPresenterImpl<VideoScanFragmentView> implements VideoScanFragmentPresenter {


    public VideoScanFragmentPresenterImpl(VideoScanFragmentView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {

    }

    @Override
    public void process(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void addScanFolder(String path, boolean isScan) {
        DataBaseManager.getInstance()
                .selectTable(11)
                .insert()
                .param(1, path)
                .param(2, isScan ? "1" : "0")
                .execute();

        EventBus.getDefault().post(new RefreshFolderEvent(true));
        queryScanFolderList(isScan);
    }

    @Override
    public void queryScanFolderList(boolean isScan) {
        List<ScanFolderBean> folderList = new ArrayList<>();
        String folderType = isScan ? "1" : "0";
        SQLiteDatabase sqLiteDatabase = DataBaseManager.getInstance().getSQLiteDatabase();
        Cursor cursor = sqLiteDatabase.query(DataBaseInfo.getTableNames()[11], null, "folder_type = ?", new String[]{folderType}, null, null, null);
        while (cursor.moveToNext()) {
            folderList.add(new ScanFolderBean(cursor.getString(1), false));
        }
        cursor.close();
        getView().updateFolderList(folderList);
    }

    @Override
    public void deleteScanFolder(String path, boolean isScan) {
        DataBaseManager.getInstance()
                .selectTable(11)
                .delete()
                .where(1, path)
                .where(2, isScan ? "1" : "0")
                .execute();

        EventBus.getDefault().post(new RefreshFolderEvent(true));
    }
}

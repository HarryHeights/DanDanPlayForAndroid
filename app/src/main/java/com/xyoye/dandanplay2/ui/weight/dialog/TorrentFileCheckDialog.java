package com.xyoye.dandanplay2.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xunlei.downloadlib.parameter.TorrentFileInfo;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseRvAdapter;
import com.xyoye.dandanplay2.ui.weight.item.TorrentFileCheckItem;
import com.xyoye.dandanplay2.utils.CommonUtils;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xyy on 2019/3/6.
 */

public class TorrentFileCheckDialog extends Dialog {

    @BindView(R.id.file_rv)
    RecyclerView fileRv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    @BindView(R.id.confirm_tv)
    TextView confirmTv;

    private BaseRvAdapter<TorrentFileInfo> fileAdapter;
    private TorrentInfo torrentInfo;
    private OnTorrentSelectedListener listener;

    public TorrentFileCheckDialog(@NonNull Context context, TorrentInfo torrentInfo, OnTorrentSelectedListener listener) {
        super(context, R.style.Dialog);
        this.torrentInfo = torrentInfo;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_torrent_file_check);
        ButterKnife.bind(this, this);

        if (torrentInfo.mSubFileInfo.length > 1){
            for (int i=1; i<torrentInfo.mSubFileInfo.length; i++){
                torrentInfo.mSubFileInfo[i].checked = false;
            }
        }

        fileRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fileRv.setNestedScrollingEnabled(false);
        fileRv.setItemViewCacheSize(10);
        fileAdapter = new BaseRvAdapter<TorrentFileInfo>(Arrays.asList(torrentInfo.mSubFileInfo)) {
            @NonNull
            @Override
            public AdapterItem<TorrentFileInfo> onCreateItem(int viewType) {
                return new TorrentFileCheckItem(position ->{
                    for (int i=0; i<torrentInfo.mSubFileInfo.length; i++){
                        torrentInfo.mSubFileInfo[i].checked = (i == position);
                    }
                    fileRv.post(() -> fileAdapter.notifyDataSetChanged());
                });
            }
        };
        fileRv.setAdapter(fileAdapter);

        cancelTv.setOnClickListener(v -> TorrentFileCheckDialog.this.dismiss());

        confirmTv.setOnClickListener(v -> {
            int selectIndex = -1;
            for(int i = 0; i < torrentInfo.mSubFileInfo.length; i++){
                if(torrentInfo.mSubFileInfo[i].checked){
                    selectIndex = torrentInfo.mSubFileInfo[i].mFileIndex;
                }
            }
            if(selectIndex > -1){
                if (CommonUtils.isMediaFile(torrentInfo.mSubFileInfo[selectIndex].mFileName)){
                    listener.onSelected(selectIndex);
                    dismiss();
                }else {
                    ToastUtils.showShort("不是可播放的视频文件");
                }
            }else{
                ToastUtils.showShort("请选择播放文件");
            }
        });
    }

    public interface OnTorrentSelectedListener{
        void onSelected(int indexes);
    }
}

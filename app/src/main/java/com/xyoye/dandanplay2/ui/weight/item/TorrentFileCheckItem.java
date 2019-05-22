package com.xyoye.dandanplay2.ui.weight.item;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xunlei.downloadlib.parameter.TorrentFileInfo;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.utils.CommonUtils;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import butterknife.BindView;

/**
 * Created by xyy on 2019/3/6.
 */

public class TorrentFileCheckItem implements AdapterItem<TorrentFileInfo> {

    @BindView(R.id.check_cb)
    CheckBox checkCb;
    @BindView(R.id.file_name_tv)
    TextView fileNameTv;
    @BindView(R.id.file_size_tv)
    TextView fileSizeTv;

    private TorrentFileCheckListener listener;

    public TorrentFileCheckItem(TorrentFileCheckListener listener){
        this.listener = listener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_torrent_file_check;
    }

    @Override
    public void initItemViews(View itemView) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(TorrentFileInfo model, int position) {
        checkCb.setChecked(model.checked);
        fileNameTv.setText(model.mFileName);
        fileSizeTv.setText(CommonUtils.convertFileSize(model.mFileSize));

        checkCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null)
                listener.onCheck(position, isChecked);
        });
    }

    public interface TorrentFileCheckListener{
        void onCheck(int position, boolean isChecked);
    }
}

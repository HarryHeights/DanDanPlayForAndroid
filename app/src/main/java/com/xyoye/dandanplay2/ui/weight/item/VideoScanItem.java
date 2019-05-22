package com.xyoye.dandanplay2.ui.weight.item;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.bean.ScanFolderBean;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import butterknife.BindView;

/**
 * Created by xyy on 2019/2/15.
 */

public class VideoScanItem implements AdapterItem<ScanFolderBean> {

    @BindView(R.id.folder_tv)
    TextView folderTv;
    @BindView(R.id.folder_cb)
    CheckBox folderCb;

    private OnFolderCheckListener listener;

    public VideoScanItem(OnFolderCheckListener listener){
        this.listener = listener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_video_scan;
    }

    @Override
    public void initItemViews(View itemView) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(ScanFolderBean model, int position) {
        folderTv.setText(model.getFolder());
        folderCb.setChecked(model.isCheck());
        folderCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onCheck(isChecked, position);
        });
    }

    public interface OnFolderCheckListener{
        void onCheck(boolean isCheck, int position);
    }
}

package com.xyoye.dandanplay2.ui.weight.item;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.bean.FolderBean;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import butterknife.BindView;

/**
 * Created by YE on 2018/6/29 0029.
 */

public class FolderItem implements AdapterItem<FolderBean>{
    @BindView(R.id.folder_title)
    TextView folderTitle;
    @BindView(R.id.file_number)
    TextView fileNumber;

    private View mView;
    private Context mContext;
    private PlayFolderListener listener;

    public FolderItem(PlayFolderListener listener){
        if (listener == null)
            throw new NullPointerException("call back not null");
        this.listener = listener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_folder;
    }

    @Override
    public void initItemViews(View itemView) {
        mView = itemView;
        mContext = mView.getContext();
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(FolderBean model, int position) {
        String folder = model.getFolderPath();
        String title = FileUtils.getFileNameNoExtension(folder.substring(0, folder.length()-1));

        folderTitle.setText(title);
        fileNumber.setText(String.valueOf(model.getFileNumber() + " 视频"));

        //是否为上次播放的文件夹
        boolean isLastPlayFolder = false;
        String lastVideoPath = AppConfig.getInstance().getLastPlayVideo();
        if (!StringUtils.isEmpty(lastVideoPath)){
            String folderPath = FileUtils.getDirName(lastVideoPath);
            isLastPlayFolder = folderPath.equals(model.getFolderPath());
        }

        folderTitle.setTextColor(isLastPlayFolder
                ? mContext.getResources().getColor(R.color.theme_color)
                : mContext.getResources().getColor(R.color.text_black));

        fileNumber.setTextColor(isLastPlayFolder
                ? mContext.getResources().getColor(R.color.theme_color)
                : mContext.getResources().getColor(R.color.text_gray));

        mView.setOnClickListener(v -> listener.onClick(model.getFolderPath()));

        mView.setOnLongClickListener(v -> listener.onLongClick(model.getFolderPath(), title));
    }

    public interface PlayFolderListener{
        void onClick(String folderPath);

        boolean onLongClick(String folderPath, String folderName);
    }
}

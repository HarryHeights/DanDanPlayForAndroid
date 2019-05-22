package com.xyoye.dandanplay2.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseRvAdapter;
import com.xyoye.dandanplay2.bean.FileManagerBean;
import com.xyoye.dandanplay2.ui.weight.item.FileManagerItem;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.CommonUtils;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xyy on 2019/2/15.
 */

public class FileManagerDialog extends Dialog{
    public final static int SELECT_FOLDER = 1001;
    public final static int SELECT_DANMU = 1002;
    public final static int SELECT_SUBTITLE = 1003;
    public final static int SELECT_VIDEO = 1004;

    private String rootPath;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.path_tv)
    TextView pathTv;
    @BindView(R.id.file_rv)
    RecyclerView fileRv;
    @BindView(R.id.confirm_tv)
    TextView confirmTv;
    @BindView(R.id.default_tv)
    TextView defaultTv;

    private Context mContext;
    private BaseRvAdapter<FileManagerBean> adapter;
    private List<FileManagerBean> managerList;

    private OnSelectedListener listener;
    private OnItemClickListener itemClickListener;
    private String originFolder;
    private int openType;
    private boolean showDefault = true;

    public FileManagerDialog(@NonNull Context context, int openType, OnSelectedListener listener) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.originFolder = AppConfig.getInstance().getDownloadFolder();
        this.openType = openType;
        this.listener = listener;
    }

    public FileManagerDialog(@NonNull Context context, String originFolder, int openType, OnSelectedListener listener) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.originFolder = StringUtils.isEmpty(originFolder)
                ? AppConfig.getInstance().getDownloadFolder()
                : originFolder;
        this.openType = openType;
        this.listener = listener;
    }

    public FileManagerDialog hideDefault(){
        showDefault = false;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_file_manager);
        ButterKnife.bind(this);

        managerList = new ArrayList<>();

        itemClickListener = (path, isFolder) -> {
            if (isFolder){
                listFolder(path);
            }else{
                if (listener != null)
                    listener.onSelected(path);
                FileManagerDialog.this.dismiss();
            }
        };

        initView();

        listFolder(originFolder);
    }

    @OnClick({R.id.default_tv, R.id.cancel_tv, R.id.confirm_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.default_tv:
                String defaultPath = Environment.getExternalStorageDirectory().getPath();
                listFolder(defaultPath);
                break;
            case R.id.cancel_tv:
                FileManagerDialog.this.dismiss();
                break;
            case R.id.confirm_tv:
                String path = pathTv.getText().toString();
                if (listener != null)
                    listener.onSelected(path);
                FileManagerDialog.this.dismiss();
                break;
        }
    }

    private void initView(){
        adapter = new BaseRvAdapter<FileManagerBean>(managerList) {
            @NonNull
            @Override
            public AdapterItem<FileManagerBean> onCreateItem(int viewType) {
                return new FileManagerItem(itemClickListener);
            }
        };

        defaultTv.setVisibility(showDefault ? View.VISIBLE : View.GONE);
        if (showDefault){
            rootPath = "/";
        }else {
             rootPath = Environment.getExternalStorageDirectory().getPath();
        }

        fileRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        fileRv.setNestedScrollingEnabled(false);
        fileRv.setItemViewCacheSize(10);
        fileRv.setAdapter(adapter);

        switch (openType){
            case SELECT_FOLDER:
                titleTv.setText("选择文件夹");
                confirmTv.setVisibility(View.VISIBLE);
                break;
            case SELECT_DANMU:
                titleTv.setText("选择弹幕文件");
                confirmTv.setVisibility(View.GONE);
                break;
            case SELECT_SUBTITLE:
                titleTv.setText("选择字幕文件");
                confirmTv.setVisibility(View.GONE);
                break;
            case SELECT_VIDEO:
                titleTv.setText("选择视频文件");
                confirmTv.setVisibility(View.GONE);
                break;

        }
    }

    private void listFolder(String path){
        pathTv.setText(path);

        File folder;
        File pathFile = new File(path);
        if (pathFile.isFile())
            folder = pathFile.getParentFile();
        else
            folder = pathFile;

        File[] contents = folder.listFiles();
        List<FileManagerBean> fileList = new ArrayList<>();
        if (contents != null) {
            for (File file : contents) {
                FileManagerBean info = new FileManagerBean();
                if (file.isDirectory()){
                    info.setFolder(true);
                    info.setFile(file);
                    info.setName(file.getName());
                    fileList.add(info);
                } else if (openType == SELECT_DANMU){
                    String ext = FileUtils.getFileExtension(file);
                    if ("XML".equals(ext.toUpperCase())){
                        info.setFolder(false);
                        info.setFile(file);
                        info.setName(file.getName());
                        fileList.add(info);
                    }
                }else if (openType == SELECT_SUBTITLE) {
                    String ext = FileUtils.getFileExtension(file);
                    switch (ext.toUpperCase()) {
                        case "ASS":
                        case "SCC":
                        case "STL":
                        case "SRT":
                        case "TTML":
                            info.setFolder(false);
                            info.setFile(file);
                            info.setName(file.getName());
                            fileList.add(info);
                            break;
                    }
                }else if (openType == SELECT_VIDEO){
                    if (CommonUtils.isMediaFile(file.getAbsolutePath())){
                        info.setFolder(false);
                        info.setFile(file);
                        info.setName(file.getName());
                        fileList.add(info);
                    }
                }
            }
            Collections.sort(fileList, (o1, o2) ->
                    Collator.getInstance(Locale.CHINESE).compare( o1.getName(), o2.getName()));
        }

        if (!rootPath.equals(folder.getAbsolutePath()))
            fileList.add(0, new FileManagerBean(folder, ".." ,true, true));

        updateView(fileList);
    }

    private void updateView(List<FileManagerBean> fileList){
        managerList.clear();
        managerList.addAll(fileList);
        adapter.notifyDataSetChanged();
    }

    public interface OnSelectedListener{
        void onSelected(String path);
    }

    public interface OnItemClickListener{
        void onItemClick(String path, boolean isFolder);
    }
}

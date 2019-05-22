package com.xyoye.dandanplay2.ui.activities;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.app.IApplication;
import com.xyoye.dandanplay2.base.BaseMvcActivity;
import com.xyoye.dandanplay2.ui.weight.dialog.CommonDialog;
import com.xyoye.dandanplay2.ui.weight.preview.ImageViewTouch;
import com.xyoye.dandanplay2.ui.weight.preview.ImageViewTouchBase;
import com.xyoye.dandanplay2.utils.CommonUtils;
import com.xyoye.dandanplay2.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xyy on 2019/4/11.
 */

public class ImagePreviewActivity extends BaseMvcActivity {

    @BindView(R.id.preview_iv)
    ImageViewTouch previewIv;
    @BindView(R.id.back_iv)
    ImageView backIv;

    private String originUrl;

    private Runnable hideViewRunnable = () -> switchScreenStatus(true);

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void initPageView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        originUrl = getIntent().getStringExtra("image_url");

        if (!StringUtils.isEmpty(originUrl)) {
            previewIv.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            Glide.with(this)
                    .load(originUrl)
                    .into(previewIv);
        }

        switchScreenStatus(false);
        IApplication.getMainHandler().removeCallbacks(hideViewRunnable);
        IApplication.getMainHandler().postDelayed(hideViewRunnable, 2000);

        previewIv.setSingleTapListener(() -> {
            switchScreenStatus(false);
            IApplication.getMainHandler().removeCallbacks(hideViewRunnable);
            IApplication.getMainHandler().postDelayed(hideViewRunnable, 2000);
        });

        previewIv.setOnLongClickListener(v -> {
            new CommonDialog.Builder(ImagePreviewActivity.this)
                    .setAutoDismiss()
                    .setOkListener(dialog ->
                            Glide.with(this)
                               .asBitmap()
                               .load(originUrl)
                               .into(new SimpleTarget<Bitmap>() {
                                   @Override
                                   public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                       saveBitmap(resource);
                                   }
                               }))
                    .build()
                    .show("确认下载图片？", "确定", "取消");

            return true;
        });
    }

    @Override
    public void initPageViewListener() {

    }

    @OnClick({R.id.back_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                ImagePreviewActivity.this.finish();
                break;
        }
    }

    private void switchScreenStatus(boolean isFullScreen){
        if (backIv == null) return;
        if (isFullScreen){
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            backIv.setVisibility(View.GONE);
        }else {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            backIv.setVisibility(View.VISIBLE);
        }
    }

    private void saveBitmap(Bitmap bitmap){
        try {
            //make folder
            File folder = new File(Constants.DefaultConfig.imageFolder);
            if (!folder.exists())
                folder.mkdirs();

            //make file
            File coverFile = new File(folder,  CommonUtils.getCurrentFileName("COV", ".jpg"));
            if (coverFile.exists()) {
                coverFile.delete();
            }
            coverFile.createNewFile();

            //save
            FileOutputStream fos = new FileOutputStream(coverFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            ToastUtils.showLong("保存成功："+coverFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showShort("保存失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IApplication.getMainHandler().removeCallbacks(hideViewRunnable);
    }
}

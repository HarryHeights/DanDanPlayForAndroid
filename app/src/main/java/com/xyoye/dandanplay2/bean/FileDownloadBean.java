package com.xyoye.dandanplay2.bean;

import com.xyoye.dandanplay2.utils.net.CommJsonEntity;

import java.io.File;

/**
 * Created by xyy on 2018/12/20.
 */

public class FileDownloadBean extends CommJsonEntity{
    private File file;

    public FileDownloadBean(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}

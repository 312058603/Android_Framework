package com.example.wpx.framework.http.observer;

import java.io.File;

/**
 * 文件下载监听
 */
public interface DownLoadListener {
    void onProgress(long position, long fileSize);
    void onDownLoadOver(File file);
}
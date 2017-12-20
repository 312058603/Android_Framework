package com.example.wpx.framework.manager;

import com.example.wpx.framework.config.FileConfig;
import com.example.wpx.framework.thread.ThreadPoolFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class ApkDownLoadManager {

    private String apkUrl;
    private DownLoadListener listener;
    private boolean interceptFlag;

    public void startDownLoading(String apkUrl, DownLoadListener listener) {
        this.apkUrl = apkUrl;
        this.listener = listener;
        ThreadPoolFactory.getDownLoadPool().execute(new DownLoadTask());
    }

    private class DownLoadTask implements Runnable {
        @Override
        public void run() {
            File ApkFile = new File(FileConfig.PATH_UPDATE_APK);
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                long fileSize = httpURLConnection.getContentLength();
                InputStream is = httpURLConnection.getInputStream();
                FileOutputStream fos = new FileOutputStream(ApkFile);
                long count = 0;
                byte buf[] = new byte[1024];
                do {
                    int tempLen = is.read(buf);
                    count += tempLen;
                    if (listener != null)  //通知进度更新
                        listener.onProgress(count, fileSize);
                    if (tempLen <= 0) {  // 文件已经下载完成
                        if (listener != null)
                            listener.onDownLoadOver(ApkFile);//通知文件下载完成
                        interceptFlag = true;   //跳出循环
                        break;
                    }
                    fos.write(buf, 0, tempLen);
                } while (!interceptFlag);
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

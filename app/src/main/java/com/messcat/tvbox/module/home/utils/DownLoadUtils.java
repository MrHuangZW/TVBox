package com.messcat.tvbox.module.home.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.messcat.kotlin.utils.LogUtil;
import com.messcat.mclibrary.mchttp.uploadAPP.DownLoadService;
import com.messcat.mclibrary.mchttp.uploadAPP.FileCallback;
import com.messcat.mclibrary.mchttp.uploadAPP.FileResponseBody;
import com.messcat.mclibrary.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class DownLoadUtils {

    /**
     * 目标文件存储的文件夹路径
     */
    private static  String destFileDir = "TVBox";
    /**
     * 目标文件存储的文件名
     */
    private static String destFileName = "tvbox.mp4";
    private static String url1, url2;
    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private static Retrofit.Builder retrofit;


    public static void loadVideo(String url,ILoadVideoListener listener) {
        if (url != null) {
            url1 = url.substring(0, url.lastIndexOf("/") + 1);
            url2 = url.substring(url.lastIndexOf("/") + 1);
            if (url1 != null && url2 != null) {
                loadFile(listener);
            }
        }

    }

    /**
     * 下载文件
     */
    private static void loadFile(final ILoadVideoListener listener) {
//        initNotification();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        retrofit.baseUrl(url1)
                .client(initOkHttpClient())
                .build()
                .create(DownLoadService.IFileLoad.class)
                .loadFile(url2)
                .enqueue(new FileCallback(destFileDir, destFileName) {

                    @Override
                    public void onSuccess(File file) {
                        String path = file.getPath();
                        // 安装软件
//                        cancelNotification();
//                        installApk(file);
                        listener.loadSuccess();
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        LogUtil.e("下载进度" + progress * 100 / total);
//                        updateNotification(progress * 100 / total);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        cancelNotification();
                        File file = new File(getFilePath("TVBox", "tvbox.mp4"));
                        if (file.exists())
                            file.delete();
                        listener.loadFail();
                    }
                });
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private static OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    public interface ILoadVideoListener {
        void loadSuccess();
        void loadFail();
    }
    /**
     * 获取SD卡目录
     */
   private static String getSDPath(){
        String sdDis = null;
        boolean sdCardExit = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState();
        if (sdCardExit) {
            sdDis = Environment.getExternalStorageDirectory().getPath();
        }
        return sdDis;
    }

    /**
     * 获取SD卡数据
     */
   private static String  getFilePath(String fileName, String filePaths){
        String filePath = getSDPath() + File.separator + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            File file1 = new File(filePath + File.separator + filePaths);
            if (file1.exists()) {
                return file1.getPath();
            } else {
                return null;
            }
        } else {
            return null;
        }


    }

}

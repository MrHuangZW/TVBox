package com.messcat.tvbox.module.home.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

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

    private static DownLoadUtils mInstance;

    /**
     * 目标文件存储的文件夹路径
     */
    private static String destFileDir = "TVBox";
    /**
     * 目标文件存储的文件名
     */
    private static String destFileName = "tvbox.mp4";
    private static String url,url1, url2;
    private static Retrofit.Builder retrofit;
    private Context mContext;

    private DownLoadUtils(Context context) {
        mContext = context;
    }

    public static DownLoadUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DownLoadUtils.class) {
                if (mInstance == null) {
                    mInstance = new DownLoadUtils(context);
                }
            }
        }
        return mInstance;
    }

    public void loadVideo(String url, final ILoadVideoListener listener) {
        if (url != null) {
            this.url = url;
            url1 = url.substring(0, url.lastIndexOf("/") + 1);
            url2 = url.substring(url.lastIndexOf("/") + 1);
            if (url1 != null && url2 != null&&!url1.equals("null")&&!url2.equals("null")) {
                loadFile(listener);
            }else{
                listener.loadFail();
            }
        }

    }

    /**
     * 下载文件
     */
    private void loadFile(final ILoadVideoListener listener) {
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
                        listener.loadSuccess(url);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        LogUtil.e("下载进度" + progress * 100 / total);
//                        updateNotification(progress * 100 / total);
                        Toast.makeText(mContext,"下载进度:"+ progress * 100 / total,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        cancelNotification();
                        Toast.makeText(mContext,"下载失败:"+ t.getMessage(),Toast.LENGTH_SHORT).show();

                        listener.loadFail();
                    }
                });
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
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
        void loadSuccess(String url);

        void loadFail();
    }

}

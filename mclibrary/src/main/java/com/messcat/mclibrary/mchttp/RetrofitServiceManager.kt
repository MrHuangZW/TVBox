@file:JvmName("RetrofitServiceManager")
@file:JvmMultifileClass

package com.messcat.kotlin.mchttp

import android.util.Log
import com.messcat.mclibrary.BASE_URL
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import com.messcat.mclibrary.base.BaseApplication
import com.messcat.mclibrary.mchttp.MCCookiesManager
import com.messcat.mclibrary.util.checkNetEnable
import okhttp3.*
import java.io.File
import java.io.IOException
import okhttp3.CacheControl
import javax.security.auth.login.LoginException
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.net.URLDecoder


/**
 * Created by zhouwei on 16/11/9.
 */

class RetrofitServiceManager private constructor() {
    private val mRetrofit: Retrofit

    init {

        val httpCacheDirectory = File(BaseApplication.getInstance()?.getExternalCacheDir(), "responses")
        //设置缓存 10M
        val cache = Cache(httpCacheDirectory, 30 * 1024 * 1024)
        /**
         * Cookie管理器
         */
        val cookiesManager = MCCookiesManager()
        // 创建 OKHttpClient
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)//读操作超时时间
        builder.addInterceptor(provideOfflineCacheInterceptor())//离线
        builder.addNetworkInterceptor(provideCacheInterceptor())//在线
        builder.cache(cache)
        builder.cookieJar(cookiesManager)
        var logger = HttpLoggingInterceptor.Logger {
            val text = URLDecoder.decode(it, "utf-8")
            Log.e("OKHttp-----", text)
        }
        //添加日志拦截器
        builder.addInterceptor(HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY))

        // 添加公共参数拦截器
        val commonInterceptor = HttpCommonInterceptor.Builder()
                .addHeaderParams("paltform", "android")
                .addHeaderParams("userToken", "1234343434dfdfd3434")
                .addHeaderParams("userId", "123445")
                .build()
        builder.addInterceptor(commonInterceptor)


        // 创建Retrofit
        mRetrofit = Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .baseUrl(BASE_URL)
                .build()

    }

    /**
     * 有网络
     */
    fun provideCacheInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val response = chain.proceed(chain.request())

                // re-write response header to force use of cache
                // 正常访问同一请求接口（多次访问同一接口），给30秒缓存，超过时间重新发送请求，否则取缓存数据
                val cacheControl = CacheControl.Builder()
                        .maxAge(20, TimeUnit.SECONDS)
                        .build()

                return response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build()
            }
        }
    }

    /**
     * 没有网络
     */
    fun provideOfflineCacheInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()

                /**
                 * 未联网获取缓存数据
                 */
                if (checkNetEnable(BaseApplication.getInstance())) {
                    //在20秒缓存有效，此处测试用，实际根据需求设置具体缓存有效时间
                    val cacheControl = CacheControl.Builder()
                            .maxStale(20, TimeUnit.SECONDS)
                            .build()

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build()
                }

                return chain.proceed(request)
            }
        }
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitServiceManager()
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> create(service: Class<T>): T {
        return mRetrofit.create(service)
    }

    companion object {
        private val DEFAULT_TIME_OUT = 5//超时时间 5s
        private val DEFAULT_READ_TIME_OUT = 5

        /**
         * 获取RetrofitServiceManager
         * @return
         */
        val instance: RetrofitServiceManager
            get() = SingletonHolder.INSTANCE
    }
}

package com.messcat.kotlin.mchttp

import android.util.Log

import java.io.IOException
import java.util.HashMap

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 拦截器

 * 向请求头里添加公共参数
 * Created by zhouwei on 16/11/10.
 */

class HttpCommonInterceptor : Interceptor {

    private val mHeaderParamsMap = HashMap<String, String>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()

        // 添加新的参数，添加到url 中
        /* HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());*/

        // 新的请求

        val requestBuilder = oldRequest.newBuilder()
        requestBuilder.method(oldRequest.method(), oldRequest.body())
        //添加公共参数,添加到header中
        if (mHeaderParamsMap.size > 0) {
            for ((key, value) in mHeaderParamsMap) {
                requestBuilder.header(key, value)
            }
        }

        val newRequest = requestBuilder.build()

        return chain.proceed(newRequest)
    }

    class Builder {
        internal var mHttpCommonInterceptor: HttpCommonInterceptor

        init {
            mHttpCommonInterceptor = HttpCommonInterceptor()
        }

        fun addHeaderParams(key: String, value: String): Builder {
            mHttpCommonInterceptor.mHeaderParamsMap.put(key, value)
            return this
        }

        fun addHeaderParams(key: String, value: Int): Builder {
            return addHeaderParams(key, value.toString())
        }

        fun addHeaderParams(key: String, value: Float): Builder {
            return addHeaderParams(key, value.toString())
        }

        fun addHeaderParams(key: String, value: Long): Builder {
            return addHeaderParams(key, value.toString())
        }

        fun addHeaderParams(key: String, value: Double): Builder {
            return addHeaderParams(key, value.toString())
        }


        fun build(): HttpCommonInterceptor {
            return mHttpCommonInterceptor
        }
    }
}

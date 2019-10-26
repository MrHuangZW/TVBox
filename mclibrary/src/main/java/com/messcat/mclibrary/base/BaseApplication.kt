package com.messcat.mclibrary.base

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

/**
 * Created by Administrator on 2017/8/30 0030.
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        CrashReport.initCrashReport(application, "13c58decec", true);
    }

    companion object {
        //单例模式
        var application: BaseApplication? = null
        fun getInstance() = application
    }
}
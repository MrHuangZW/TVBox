package com.messcat.tvbox

import android.os.Build
import com.messcat.mclibrary.base.BaseApplication
import com.tencent.bugly.crashreport.CrashReport
import java.lang.reflect.Method

/**
 * Created by Administrator on 2017/9/1 0001.
 */
class TvBoxApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        application = this
        CrashReport.initCrashReport(this, "13c58decec", true)
//        ThirdActivity.hookWebView()
        hookWebView()
    }

    private fun hookWebView() {
        try {
            var sdkInt = Build.VERSION.SDK_INT
            var factoryClass = Class.forName("android.webkit.WebViewFactory")
            var field = factoryClass.getDeclaredField("sProviderInstance")
            field.isAccessible = true
            var sProviderInstance = field.get(null)
            if (sProviderInstance != null) {
                return
            }
            var getProviderClassMethod: Method
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass")
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass")
            } else {
                return
            }
            getProviderClassMethod.setAccessible(true)
            var providerClass = getProviderClassMethod.invoke(factoryClass)
            var delegateClass = Class.forName("android.webkit.WebViewDelegate")
            var providerConstructor = providerClass.javaClass.getConstructor(delegateClass)
            if (providerConstructor != null) {
                providerConstructor.isAccessible = true
                var declaredConstructor = delegateClass.getDeclaredConstructor()
                declaredConstructor.isAccessible = true
                sProviderInstance = providerConstructor.newInstance((declaredConstructor.newInstance()))
                field.set("sProviderInstance", sProviderInstance)
            }
        } catch (e: Exception) {

        }

//        var factoryClass: Class<*>? = null
//        factoryClass = Class.forName("android.webkit.WebViewFactory")
//        var getProviderClassMethod: Method? = null
//        var sProviderInstance = null
//        if (Build.VERSION.SDK_INT == 23) {
//            getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass")
//            getProviderClassMethod.isAccessible = true
//            var providerClass = getProviderClassMethod.invoke(factoryClass)
//            var delegateClass: Class<*>? = Class.forName("android.webkit.WebViewDelegate")
//            var constructor = providerClass.javaClass.getConstructor(delegateClass)
//            if (constructor != null) {
//                constructor.isAccessible = true
//                var constructor2: Constructor<*>? = null
//                constructor2 = delegateClass?.declaredConstructors
//            }
//
//        }

    }

    companion object {
        //单例模式
        var application: BaseApplication? = null

        fun getInstance() = application
    }
}
package com.messcat.mclibrary.base

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hengda.smart.jsyz.m.component.LoadingDialog
import com.messcat.kotlin.utils.LoadingDialogManager
import com.messcat.mclibrary.addActivitys
import com.messcat.mclibrary.finishActivitys
import java.lang.reflect.Method
import java.util.concurrent.Executors

/**
 * Created by Administrator on 2017/8/30 0030.
 */
abstract class BaseActivity : AppCompatActivity(), LoadingDialogManager {

    var msgKey1: Int = 1//倒计时发送Handler的参数
    var wifiKey: Int = 2
    var scheduledThreadPool = Executors.newScheduledThreadPool(2)//线程池用于倒计时
    protected var mActivity: Activity? = null
    protected var mContext: Context? = null
    override val loadingDialog by lazy { LoadingDialog(this) }//弹出Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getLayout() > 0) {
            setContentView(getLayout())
        }
        mActivity = this
        mContext = this
        addActivitys(this)
//        initView()
//        initData()
//        initEvent()
    }



    override fun onDestroy() {
        super.onDestroy()
        finishActivitys(this)
    }

    abstract fun getLayout(): Int
    abstract fun initData()
    abstract fun initEvent()
    abstract fun initView()
}
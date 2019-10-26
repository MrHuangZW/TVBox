package com.messcat.mclibrary.base

import android.os.Bundle

/**
 * Created by Administrator on 2017/9/4 0004.
 */
abstract class NotMVPActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
        initEvent()
    }
}
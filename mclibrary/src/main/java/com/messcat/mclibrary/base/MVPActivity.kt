@file:JvmName("MVPActivity")
@file:JvmMultifileClass

package com.messcat.mclibrary.base

import android.os.Build
import android.os.Bundle
import java.lang.reflect.Method

/**
 * Created by Administrator on 2017/8/30 0030.
 */
abstract class MVPActivity<T : BasePresenter<*>> : BaseActivity(), BaseView {

    var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter = initPresenter()
        initView()
        initData()
        initEvent()
//        if (mPresenter != null)
//            mPresenter?.attachView(this as Nothing)
    }



    override fun onDestroy() {
        super.onDestroy()
//        if (mPresenter != null)
//            mPresenter?.detachView()
    }

    protected abstract fun initPresenter(): T
}
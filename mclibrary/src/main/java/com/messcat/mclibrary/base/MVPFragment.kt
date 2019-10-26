package com.messcat.mclibrary.base

import android.os.Bundle
import android.view.View

/**
 * Created by Administrator on 2017/8/30 0030.
 */
abstract class MVPFragment<T : BasePresenter<*>> : BaseFragment(), BaseView {

    var mPresenter: T? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mPresenter = initPresenter()
        initView()
        initData()
        initEvent()
//        if (mPresenter != null) {
//            mPresenter?.attachView(this as Nothing)
//        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (mPresenter != null)
//            mPresenter?.detachView()
    }

    protected abstract fun initPresenter(): T
}
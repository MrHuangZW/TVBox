package com.messcat.mclibrary.base

/**
 * Created by Administrator on 2017/8/30 0030.
 */
interface BasePresenter<V : BaseView> {

    fun attachView(view: V)
    fun detachView()
}
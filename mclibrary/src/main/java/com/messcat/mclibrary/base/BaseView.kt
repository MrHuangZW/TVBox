package com.messcat.mclibrary.base

/**
 * Created by Administrator on 2017/8/30 0030.
 */
interface BaseView {

    fun showError(msg: String?)//错误信息

    fun showLoadingDialog()//弹出Dialog

    fun dismissLoadingDialog()//关闭Dialog
}
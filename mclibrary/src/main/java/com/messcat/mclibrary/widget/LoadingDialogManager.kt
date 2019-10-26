package com.messcat.kotlin.utils

import android.content.Context
import com.hengda.smart.jsyz.m.component.LoadingDialog

/**
 * 网络请求Dialog管理
 * Created by Administrator on 2017/8/18 0018.
 */
interface LoadingDialogManager {

    val loadingDialog: LoadingDialog

    fun showLoadingDialog(context: Context?) {//弹出Dialog
        loadingDialog.showDialog(context!!, "正在加载...", false, null)
    }

    fun hideLaodingDialog() {//隐藏Dialog
        loadingDialog.dismissDialog()
    }
}
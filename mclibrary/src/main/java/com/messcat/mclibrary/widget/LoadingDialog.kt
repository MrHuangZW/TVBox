package com.hengda.smart.jsyz.m.component

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.Window
import com.github.ybq.android.spinkit.style.Wave
import com.messcat.mclibrary.R
import kotlinx.android.synthetic.main.layout_loadingdialog.*
import kotlinx.android.synthetic.main.layout_loadingdialog.view.*


/**
 * 网络请求Dialog
 * Created by Knight_Davion on 2017/6/30.
 */
class LoadingDialog : Dialog {

    var dialog: LoadingDialog? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        loadingBar.setIndeterminateDrawable(Wave())
        super.onWindowFocusChanged(hasFocus)
    }

    fun showDialog(context: Context, message: CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?){
        dialog = LoadingDialog(context, R.style.LoadingDialog)
        var window: Window = dialog?.window!!
        window.setContentView(R.layout.layout_loadingdialog)
        if (!TextUtils.isEmpty(message)) {
            window.decorView.loading.text = message
        } else {
            window.decorView.loading.visibility = View.GONE
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(cancelable)
        dialog?.setOnCancelListener(cancelListener)
        window.attributes.gravity = Gravity.CENTER
        val lp = window.attributes
        lp.dimAmount = 0.2f
        window.attributes = lp
        dialog?.show()
    }

    fun dismissDialog() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }
}
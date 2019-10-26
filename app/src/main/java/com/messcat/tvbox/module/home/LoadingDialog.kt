package com.messcat.tvbox.module.home

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.messcat.tvbox.R

/**
 *author:Bandele
 *date:2018/10/17 0017$
 *describe:
 */

class LoadingDialog {
    private var mDialog: Dialog? = null
    private var window: Window? = null
    private var mProgressBar: ProgressBar? = null

    val isShowing: Boolean
        get() = if (mDialog!!.isShowing) {
            true
        } else {
            false
        }

    constructor(context: Context, message: String) {
        initView(context, message)
    }

    constructor(context: Context) {
        initView(context, null)
    }

    private fun initView(context: Context, message: String?) {
        mDialog = Dialog(context, R.style.CustomDialog)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window = mDialog!!.window
        window!!.setContentView(R.layout.dialog_text_loading)
        val tvMessage = window!!.findViewById<View>(R.id.message) as TextView
        if (message != null) {
            tvMessage.visibility = View.VISIBLE
            tvMessage.text = message
        }
        mProgressBar = window!!.findViewById<View>(R.id.pb_loadingbar) as ProgressBar
    }

    fun show() {
        mDialog!!.setCancelable(true)
        mDialog!!.show()
    }

    fun showUnCalcel() {
        mDialog!!.setCancelable(false)
        mDialog!!.show()
    }

    fun setCancelable(cancelable: Boolean) {
        mDialog!!.setCancelable(cancelable)
    }

    fun dissmiss() {
        if (mDialog!!.isShowing && mDialog!!.context != null) {
            mDialog!!.dismiss()
        }
    }

    fun setMessage(msg: String) {
        window = mDialog!!.window
        val tvMessage = window!!.findViewById<View>(R.id.message) as TextView
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = msg
    }
}

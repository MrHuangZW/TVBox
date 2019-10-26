package com.messcat.mclibrary.util

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.messcat.mclibrary.R
import com.messcat.mclibrary.widget.MCWheelView

/**
 * 单个选择器
 * Created by Administrator on 2017/9/1 0001.
 */
private var mWheelDialog: Dialog? = null
private var wheelView: MCWheelView? = null
private var yfWheelConfirm: TextView? = null
private var yfWheelCancel: TextView? = null

fun Any.showWheelDialog(mContext: Context?, position: Int, wheelList: MutableList<String>?, listener: onClickListeners) {
    mWheelDialog = Dialog(mContext, R.style.dialog)
    val window = mWheelDialog?.getWindow()
    window!!.setContentView(R.layout.dialog_order_wheel)
    window!!.setGravity(Gravity.BOTTOM)
    mWheelDialog?.setCancelable(false)
    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    wheelView = window?.findViewById(R.id.wheelview)
    yfWheelConfirm = window?.findViewById(R.id.yf_wheel_confirm)
    yfWheelCancel = window?.findViewById(R.id.yf_wheel_cancel)
    wheelView?.setItems(wheelList)//加入数据
    wheelView?.setSeletion(position)//默认选中

    //确定
    yfWheelConfirm?.setOnClickListener(View.OnClickListener {
        mWheelDialog?.dismiss()
        //获取到选择的数据和选择的ID
        listener.setOnClickListeners(wheelView?.getWheelBean()?.getContext(), wheelView?.getWheelBean()?.getId())
    })

    //取消
    yfWheelCancel?.setOnClickListener {
        mWheelDialog?.dismiss()
    }

    //显示
    mWheelDialog?.show()
}

public interface onClickListeners {
    fun setOnClickListeners(content: String?, position: Int?)
}

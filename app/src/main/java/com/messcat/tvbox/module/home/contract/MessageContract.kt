package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.MessageBean

/**
 * Created by Administrator on 2017/9/21 0021.
 */
interface MessageContract {
    interface View : BaseView {
        fun checkHotelCodeResult(messageBean: MessageBean?)
    }

    interface Presenter : BasePresenter<MessageContract.View> {
        fun checkHotelCode(diCode: String?, roomNum: String?,tvBox: String?)
    }
}
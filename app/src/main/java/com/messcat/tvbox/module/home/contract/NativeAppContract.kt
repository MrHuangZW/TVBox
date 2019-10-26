package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.ApplicationNewBean
import com.messcat.tvbox.module.home.bean.NativeAppBean


/**
 * Created by Administrator on 2017/8/25 0025.
 */
interface NativeAppContract {
    interface View : BaseView {
        fun getAppInfo(naticeAppBean: ApplicationNewBean?)
    }

    interface Presenter : BasePresenter<NativeAppContract.View> {
        fun getApplicationInfo(id: String?, diCode: String?,roomNum: String?,type:String?)
        fun getApplicationInfo(diCode: String?,roomNum: String?,type:String?)
    }
}
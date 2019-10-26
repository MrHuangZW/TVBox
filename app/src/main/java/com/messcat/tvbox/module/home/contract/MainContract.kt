package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.ApplicationBean
import com.messcat.tvbox.module.home.bean.HomeBean
import com.messcat.tvbox.module.home.bean.WeatherBean
import com.messcat.tvbox.module.home.bean.WifiBean


/**
 * Created by Administrator on 2017/8/23 0023.
 */
interface MainContract {

    interface View : BaseView {
        fun getIndexInfoView(homeBean: HomeBean?,type: String?)
        fun getApplicationView(applicationBean: ApplicationBean?,type: String?)
    }

    interface Presenter : BasePresenter<MainContract.View> {
        fun getIndexInfo(diCode: String?, roomNum: String?, type: String?)
        fun getApplication(diCode: String?, roomNum: String?, type: String?)
    }
}
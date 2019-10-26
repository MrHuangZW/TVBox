package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.AddLoginInfoBean
import com.messcat.tvbox.module.home.bean.WeatherBean
import com.messcat.tvbox.module.home.bean.WelcomeBean
import com.messcat.tvbox.module.home.bean.WifiBean
/**
 * Created by Administrator on 2017/9/11 0011.
 */
interface WelcomeContract {

    interface View : BaseView {
        fun getWelcomeData(welcomeBean: WelcomeBean?, type: String?, loginInfoId: Long?)
        fun getWeather(weatherBean: WeatherBean?)
        fun weatherError(error: String?)
        fun wifiState(wifiBean: WifiBean?)
        fun updateLoginInfo()
        fun updateLoginInfoAgain()
        fun addLoginInfoResponse(t: AddLoginInfoBean?)
        fun addLoginInfoAgainResponse(t: AddLoginInfoBean?)
        fun wifiStateError()
    }

    interface Presenter : BasePresenter<WelcomeContract.View> {
        fun getWelcomeInfo(diCode: String?, roomNum: String?, type: String?, loginInfoId: Long?)
        fun getWifiState(diCode: String?, roomNum: String?)
        fun updateLoginInfo(loginInfoId: Long?)
        fun updateLoginInfoAgain(loginInfoId: Long?)
        fun addLoginInfo(diCode: String?, roomNum: String?)
        fun addLoginInfoAgain(diCode: String?, roomNum: String?)

    }
}
package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/6 0006.
 */
class WifiBean(var message: String?, var status: String?, var result: ResultBean?) : BaseBean<WifiBean.ResultBean>(message, status, result) {
    data class ResultBean(var wifiStatus: Boolean?, var wifiName: String?, var wifiPassword: String?)
}
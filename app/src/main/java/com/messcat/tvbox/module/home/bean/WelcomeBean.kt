package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/11 0011.
 */

data class WelcomeBean(var message: String?, var status: String?, var result: ResultBean?) : BaseBean<WelcomeBean.ResultBean>(message, status, result) {

    data class ResultBean(var wifi: String?, var password: String?, var wechatNum: String?, var welcomeInfo: WelcomeInfoBean?,var loginInfoId:Long?) {

        data class WelcomeInfoBean(var id: Int, var title: String?, var content: String?, var content1: String?, var fontColor: String?, var picture: String?, var hotelInfo: HotelInfoBean?,
                                   var editTime: Long, var status: String?) {

            data class HotelInfoBean(var id: Int, var name: String?, var enName: String?, var level: String?, var wechatNum: String?, var qrCode: String?,
                                     var title: String?, var addTime: Long, var editTime: Long, var editor: Any?, var status: String?, var isDel: String?, var city: String?)
        }
    }
}

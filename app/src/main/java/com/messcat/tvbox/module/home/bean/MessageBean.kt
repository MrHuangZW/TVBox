package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/21 0021.
 */

data class MessageBean(var message: String?, var status: String?, var result: ResultBean?) : BaseBean<MessageBean.ResultBean>(message, status, result) {

    data class ResultBean(var roomNum: String?, var diCode: String?)
}

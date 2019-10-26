package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 *author:Carlos
 *date:2018/12/6 0006$
 *describe:
 */
data class AddLoginInfoBean(var message: String?, var status: String?, var result: AddLoginInfoBean.ResultBean?)
    : BaseBean<AddLoginInfoBean.ResultBean>(message, status, result) {

    data class ResultBean(var loginInfoId: Long?)

}
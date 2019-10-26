package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/18 0018.
 */
data class PlayMovieBean(var message: String?, var status: String?, var result: ResultBean?) : BaseBean<PlayMovieBean.ResultBean>(message, status, result) {
    data class ResultBean(var vedio: String?)
}
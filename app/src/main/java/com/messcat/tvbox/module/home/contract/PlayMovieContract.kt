package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.PlayMovieBean

/**
 * Created by Administrator on 2017/9/18 0018.
 */
interface PlayMovieContract {

    interface View : BaseView {
        fun getVideo(playMovie: PlayMovieBean?)
    }

    interface Presenter : BasePresenter<PlayMovieContract.View> {
        fun getVideo(diCode: String?, roomNum: String?)
    }
}
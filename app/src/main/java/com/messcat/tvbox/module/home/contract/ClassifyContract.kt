package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.ClassifyBean

/**
 * Created by Administrator on 2017/9/5 0005.
 */
interface ClassifyContract {

    interface View : BaseView {
        fun getInfo(classifyBean: ClassifyBean?)
    }

    interface Presenter : BasePresenter<ClassifyContract.View> {
        fun getCategoryInfo(id: String?, diCode: String?, roomNum: String?, type: String?)
    }
}
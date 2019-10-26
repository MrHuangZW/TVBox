package com.messcat.tvbox.module.home.contract

import com.messcat.mclibrary.base.BasePresenter
import com.messcat.mclibrary.base.BaseView
import com.messcat.tvbox.module.home.bean.ClassifyBean


/**
 * Created by Administrator on 2017/8/23 0023.
 */
interface NewsInformationContract {
    interface View : BaseView {
        fun getInfo(classifyBean: ClassifyBean?)
    }

    interface Presenter : BasePresenter<NewsInformationContract.View> {
        fun getCategoryInfo(id: String?, tvBox: String?)
    }
}
package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 *author:Bandele
 *date:2018/9/25 0025$
 *describe:
 */
data class ApplicationNewBean(var message: String?, var status: String?, var result: ResultBean?) : BaseBean<ApplicationNewBean.ResultBean>(message, status, result) {

    data class ResultBean(var id: Int?, var wifi: String?, var password: String?, var wechatNum: String?, var city: String?, var details: MutableList<DetailsBean>?, var category: MutableList<CategoryBean>?) {

        data class DetailsBean(var id: Int?, var title: String?, var picture: String?, var runCode: String?, var version: String?, var orderBy: Int, var apkCheck: Any?, var cdnStatus: Any?, var jumpPage: String?, var appFile: Any?, var addTime: Long, var editTime: Long, var status: String?, var isDel: String?)

        data class CategoryBean(var id: Int?, var name: String?, var nameEnglish: Any?, var vstats: Any?, var picture: String?, var cnamcode: String?, var picture1: Any?, var picture2: Any?, var codengh: String?, var fatherId: Any?, var levels: Any?, var type: String?, var orderBy: Int, var position: Any?, var editTime: Long, var status: String?, var isDel: String?)
    }
}



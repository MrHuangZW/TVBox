package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/6 0006.
 */

data class NativeAppBean(var message: String?, var status: String?, var result: ResultBean?) : BaseBean<NativeAppBean.ResultBean>(message, status, result) {


    data class ResultBean(var id: Any?, var wifi: String?, var password: String?, var wechatNum: String?, var category: MutableList<CategoryBean>?, var apps: MutableList<AppsBean>?) {

        data class CategoryBean(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: Any?, var type: String?, var orderBy: Int,
                                var position: Any?, var editTime: Any?, var status: String?, var isDel: String?)

        data class AppsBean(var id: Int, var title: String?, var categoryId: CategoryIdBean?, var picture: String?, var runCode: String?, var version: String?,
                            var orderBy: Int, var apkCheck: Any?, var cdnStatus: Any?, var appFile: String?, var addTime: Long, var editTime: Long,
                            var status: String?, var isDel: String?, var jumpPage: String?) {

            data class CategoryIdBean(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: Any?, var type: String?,
                                      var orderBy: Int, var position: Any?, var editTime: Any?, var status: String?, var isDel: String?)
        }
    }
}

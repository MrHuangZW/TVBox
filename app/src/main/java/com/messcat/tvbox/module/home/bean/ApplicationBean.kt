package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 *author:Bandele
 *date:2018/9/13 0013$
 *describe:
 */
data class ApplicationBean(var message: String?, var status: String?, var result: ApplicationBean.ResultBean?)
    : BaseBean<ApplicationBean.ResultBean>(message, status, result) {

    data class ResultBean(var applica: List<ApplicationBean.ResultBean.ApplicaBean>?) {

        data class ApplicaBean(var id: Int?, var hid: String?, var appurl: String?, var appname: String?, var englishname: String?, var type: String?, var codename: String?, var jumpPage: String) {
            data class ApplicaBean(var id: Int?, var hid: String?, var appurl: String?, var appname: String?, var englishname: String?, var type: String?, var codename: String?) {
                data class ApplicaBean(var id: Int?, var hid: String?, var appurl: String?, var appname: String?, var englishname: String?, var type: String?, var codename: String?, var jumpPage: String) {

                }
            }

        }
    }
}
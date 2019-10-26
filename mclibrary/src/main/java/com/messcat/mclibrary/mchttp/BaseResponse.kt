package com.messcat.kotlin.mchttp

/**

 * 网络请求结果 基类
 * Created by zhouwei on 16/11/10.
 */

class BaseResponse<T> {
    var status: Int = 0
    var message: String? = null

    var data: T? = null

    val isSuccess: Boolean
        get() = status == 200
}

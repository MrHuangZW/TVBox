package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/4 0004.
 */
data class HomeBean(var message: String?, var status: String?, var result: ResultBean?)
    : BaseBean<HomeBean.ResultBean>(message, status, result) {

    data class ResultBean(var title: String?, var film: FilmBean?, var roomInfo: RoomInfoBean?,
                          var live: LiveBean?, var categorys2: List<Categorys2Bean>?, var categorys1: List<Categorys1Bean>?,
                          var responseTime: Long?, var tv: TvBean) {

        data class FilmBean(var id: Int?, var title: String?, var categoryId: CategoryIdBean?, var picture: String?, var runCode: String?,
                            var version: String?, var orderBy: Int?, var apkCheck: Any?, var appFile: String?, var addTime: Long?,
                            var editTime: Long?, var status: String?, var isDel: String?) {

            data class CategoryIdBean(var id: Int?, var name: String?, var picture: String?, var fatherId: Any?, var levels: Any?,
                                      var type: String?, var orderBy: Int?, var status: String?, var isDel: String?)
        }

        data class RoomInfoBean(var id: Int?, var hotelId: HotelIdBean?, var roomNum: String?, var wifiName: String?, var wifiPassword: String?,
                                var qrCode: String?, var name: String?, var tvBox: String?, var addTime: Long?,
                                var editTime: Long?, var status: String?, var isDel: String?) {

            data class HotelIdBean(var id: Int?, var name: String?, var enName: String?, var level: String?, var wechatNum: String?,
                                   var qrCode: String?, var addTime: Long?, var editTime: Long?,
                                   var editor: Any?, var status: String?, var isDel: String?)
        }

        data class TvBean(var id: Int?, var title: String?, var categoryId: CategoryIdBean, var picture: String, var runCode: String, var version: String,
                          var orderBy: String?, var jumpPage: String?) {
            data class CategoryIdBean(var id: String?, var name: String?)
        }

        data class LiveBean(var id: Int?, var title: String?, var categoryId: CategoryIdBeanX?, var picture: String?, var runCode: String?, var version: String?,
                            var orderBy: Int?, var apkCheck: Any?, var cdnStatus: Any?, var appFile: String?, var addTime: Long?, var editTime: Long?,
                            var status: String?, var isDel: String?) {


            data class CategoryIdBeanX(var id: Int?, var name: String?, var picture: String?, var fatherId: Any?, var levels: Any?, var type: String?,
                                       var orderBy: Int, var status: String?, var isDel: String?)
        }

        data class Categorys2Bean(var id: Int?, var name: String?, var picture: String?, var picture1: String?, var picture2: String?, var fatherId: FatherIdBean?, var levels: String?, var type: String?,
                                  var orderBy: Int?, var status: String?, var isDel: String?, var position: String,var cnamcode:String,var jumpPage:String?) {


            data class FatherIdBean(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: Any?,
                                    var orderBy: Int, var status: String?, var isDel: String?)
        }

        data class Categorys1Bean(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: Any?,
                                  var orderBy: Int, var status: String?, var isDel: String?)
    }
}
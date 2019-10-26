package com.messcat.tvbox.module.home.bean

import com.messcat.mclibrary.base.BaseBean

/**
 * Created by Administrator on 2017/9/5 0005.
 */

data class ClassifyBean(var message: String?, var status: String?, var result: ResultBean?) :
        BaseBean<ClassifyBean.ResultBean?>(message, status, result) {

    data class ResultBean(val wifi: String, val password: String, val goodsDetails: MutableList<GoodDetails>?, val wechatNum: String,
                          val city: String, val categorys3: MutableList<Categorys3>?, val newsDetails: MutableList<NewsDetail>?, val categorys2: MutableList<Categorys2Bean>?) {


        data class Categorys2Bean(val id: Int, val name: String, val nameEnglish: String, val vstats: String, val picture: String,
                                  val cnamcode: Any, val picture1: Any, val picture2: Any, val codengh: Any, val fatherId: Any,
                                  val levels: String, val type: String, val orderBy: Int, val position: String, val editTime: Long,
                                  val status: String, val isDel: String, val list2: MutableList<List2Bean>, val list3: List<Any>) {
            data class List2Bean(val id: Int, val name: String, val nameEnglish: String, val vstats: Any, val picture: Any,
                                 val cnamcode: Any, val picture1: Any, val picture2: Any, val codengh: Any, val fatherId: Any,
                                 val levels: String, val type: Any, val orderBy: Int, val position: Any, val editTime: Long,
                                 val status: String, val isDel: String, val list2: List<Any>, val list3: MutableList<List3Bean>) {
                data class List3Bean(val id: Int, val title: String, val tutleEnglish: String, val content: String,
                                     val contentEnglish: String, val picture: String, val categoryId: Any,
                                     val hotel: Any, val addTime: Long, val editTime: Long, val status: String, val isDel: String
                )
            }
        }

        data class GoodDetails(var id: String?, var title: String?, var price: String?, var picture: String?, var categoryId: CategoryId?, var addTime: String,
                               var editTime: String?, var status: String?, var isDel: String?) {
            data class CategoryId(var id: String?, var name: String?, var picture: String?, var fatherId: FatherId?, var levels: String?, var type: String?,
                                  var orderBy: String?, var status: String?, var isDel: String?) {
                data class FatherId(var id: String?, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: String?,
                                    var orderBy: String?, var status: String?, var isDel: String?)
            }
        }

        data class Categorys3(
                val id: Int,
                val name: String,
                val nameEnglish: String,
                val vstats: Any,
                val picture: Any,
                val cnamcode: Any,
                val picture1: Any,
                val picture2: Any,
                val codengh: Any,
                val fatherId: Any,
                val levels: String,
                val type: Any,
                val orderBy: Int,
                val position: Any,
                val editTime: Long,
                val status: String,
                val isDel: String,
                val list2: List<Any>,
                val list3: List<List3>
        )

        data class List3(
                val id: Int,
                val title: String,
                val tutleEnglish: String,
                val content: String,
                val contentEnglish: String,
                val picture: String,
                val categoryId: Any,
                val hotel: Any,
                val addTime: Long,
                val editTime: Long,
                val status: String,
                val isDel: String
        )

        data class NewsDetail(
                val id: Int,
                val title: String,
                val tutleEnglish: String,
                val content: String,
                val contentEnglish: String,
                val picture: String,
                val categoryId: Any,
                val hotel: Any,
                val addTime: Long,
                val editTime: Long,
                val status: String,
                val isDel: String
        )
    }
}
//    data class ResultBean(var wifi: String?, var password: String, var goodsDetails: MutableList<GoodDetails>?, var wechatNum: String?,
//                          var newsDetails: MutableList<NewsDetailsBean>?, var categorys2: MutableList<Categorys2Bean>?,
//                          var categorys3: MutableList<Categorys3Bean>?) {
//        data class GoodDetails(var id: String?, var title: String?, var price: String?, var picture: String?, var categoryId: CategoryId?, var addTime: String,
//                               var editTime: String?, var status: String?, var isDel: String?) {
//            data class CategoryId(var id: String?, var name: String?, var picture: String?, var fatherId: FatherId?, var levels: String?, var type: String?,
//                                  var orderBy: String?, var status: String?, var isDel: String?) {
//                data class FatherId(var id: String?, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: String?,
//                                    var orderBy: String?, var status: String?, var isDel: String?)
//            }
//        }
//
//        data class NewsDetailsBean(var id: Int, var title: String?, var content: String?, var picture: String?,
//                                   var categoryId: CategoryIdBean?,
//                                   var addTime: Long, var editTime: Long, var status: String?, var isDel: String?) {
//
//            data class CategoryIdBean(var id: Int, var name: String?, var picture: Any?, var fatherId: FatherIdBeanX?, var levels: String?,
//                                      var type: Any?, var orderBy: Int, var status: String?, var isDel: String?) {
//
//                data class FatherIdBeanX(var id: Int, var name: String?, var picture: String?, var fatherId: FatherIdBean?, var levels: String?,
//                                         var type: String?, var orderBy: Int, var status: String?, var isDel: String?) {
//
//                    data class FatherIdBean(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: Any?,
//                                            var orderBy: Int, var status: String?, var isDel: String?)
//                }
//            }
//        }
//
//        data class Categorys2Bean(var id: Int, var name: String?, var picture: String?, var fatherId: FatherIdBeanXX?,
//                                  var levels: String?, var type: String?, var orderBy: Int, var status: String?, var isDel: String?,var list2: MutableList<NewsDetailsBean>?) {
//            /**
//             * id : 42
//             * name : 酒店服务
//             * picture : /upload/enterprise/image/201709041559535582489c880.png
//             * fatherId : {"id":41,"name":"酒店管理","picture":"/upload/enterprise/image/201709041617585335010567d.png","fatherId":null,"levels":"1","type":null,"orderBy":2,"status":"1","isDel":"1"}
//             * levels : 2
//             * type : 2
//             * orderBy : 5
//             * status : 1
//             * isDel : 1
//             */
//            data class List2Bean(var id:Int,var name: String?,var nameEnglish:String?,var ){
//
//            }
//
//
//            data class FatherIdBeanXX(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: Any?,
//                                      var orderBy: Int, var status: String?, var isDel: String?) {
//                /**
//                 * id : 41
//                 * name : 酒店管理
//                 * picture : /upload/enterprise/image/201709041617585335010567d.png
//                 * fatherId : null
//                 * levels : 1
//                 * type : null
//                 * orderBy : 2
//                 * status : 1
//                 * isDel : 1
//                 */
//
//            }
//        }
//
//        data class Categorys3Bean(var id: Int, var name: String?, var picture: Any?, var fatherId: FatherIdBeanXXXX?, var levels: String?, var type: Any?,
//                                  var orderBy: Int, var status: String?, var isDel: String?) {
//            /**
//             * id : 54
//             * name : 温泉
//             * picture : null
//             * fatherId : {"id":42,"name":"酒店服务","picture":"/upload/enterprise/image/201709041559535582489c880.png","fatherId":{"id":41,"name":"酒店管理","picture":"/upload/enterprise/image/201709041617585335010567d.png","fatherId":null,"levels":"1","type":null,"orderBy":2,"status":"1","isDel":"1"},"levels":"2","type":"2","orderBy":5,"status":"1","isDel":"1"}
//             * levels : 3
//             * type : null
//             * orderBy : 4
//             * status : 1
//             * isDel : 1
//             */
//
//
//            data class FatherIdBeanXXXX(var id: Int, var name: String?, var picture: String?, var fatherId: FatherIdBeanXXX?, var levels: String?,
//                                        var type: String?, var orderBy: Int, var status: String?, var isDel: String?) {
//                /**
//                 * id : 42
//                 * name : 酒店服务
//                 * picture : /upload/enterprise/image/201709041559535582489c880.png
//                 * fatherId : {"id":41,"name":"酒店管理","picture":"/upload/enterprise/image/201709041617585335010567d.png","fatherId":null,"levels":"1","type":null,"orderBy":2,"status":"1","isDel":"1"}
//                 * levels : 2
//                 * type : 2
//                 * orderBy : 5
//                 * status : 1
//                 * isDel : 1
//                 */
//
//                data class FatherIdBeanXXX(var id: Int, var name: String?, var picture: String?, var fatherId: Any?, var levels: String?, var type: Any?,
//                                           var orderBy: Int, var status: String?, var isDel: String?) {
//                    /**
//                     * id : 41
//                     * name : 酒店管理
//                     * picture : /upload/enterprise/image/201709041617585335010567d.png
//                     * fatherId : null
//                     * levels : 1
//                     * type : null
//                     * orderBy : 2
//                     * status : 1
//                     * isDel : 1
//                     */
//
//                }
//            }
//        }
//    }


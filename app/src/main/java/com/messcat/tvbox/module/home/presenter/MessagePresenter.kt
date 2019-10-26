package com.messcat.tvbox.module.home.presenter

import com.messcat.kotlin.mchttp.ObjectLoader
import com.messcat.mclibrary.base.RxPresenter
import com.messcat.tvbox.module.home.bean.MessageBean
import com.messcat.tvbox.module.home.contract.MessageContract
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import rx.Observer

/**
 * Created by Administrator on 2017/9/21 0021.
 */
class MessagePresenter(mView: MessageContract.View, checkHotelCodeLoader: CheckHotelCodeLoader) :
        RxPresenter<MessageContract.View>(), MessageContract.Presenter {

    var mCheckHotelCodeLoader = checkHotelCodeLoader
    var view = mView

    override fun checkHotelCode(diCode: String?, roomNum: String?, tvBox: String?) {
        mCheckHotelCodeLoader.getCheckHotelCode(diCode, roomNum, tvBox).subscribe(object : Observer<MessageBean> {
            override fun onNext(t: MessageBean?) {
                if ("200".equals(t?.status)) {
                    view.checkHotelCodeResult(t)
                } else {
                    view.showError(t?.message)
                }
            }

            override fun onError(e: Throwable?) {
                view.showError("请检查网络连接是否正常")
            }

            override fun onCompleted() {

            }

        })
    }

    class CheckHotelCodeLoader(checkHotelCodeHttp: CheckHotelCodeHttp) : ObjectLoader() {
        var mCheckHotelCodeHttp = checkHotelCodeHttp
        fun getCheckHotelCode(diCode: String?, roomNum: String?, tvBox: String?): Observable<MessageBean>
                = observe(mCheckHotelCodeHttp.CheckHotelCode(diCode, roomNum, tvBox))
    }

    interface CheckHotelCodeHttp {
        @FormUrlEncoded
        @POST("epIndex/checkHotelCode")
        fun CheckHotelCode(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("tvBox") tvBox: String?): Observable<MessageBean>
    }
}
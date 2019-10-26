package com.messcat.tvbox.module.home.presenter

import com.messcat.kotlin.mchttp.ObjectLoader
import com.messcat.mclibrary.base.RxPresenter
import com.messcat.mclibrary.util.getWifiIP
import com.messcat.tvbox.module.home.bean.ApplicationBean
import com.messcat.tvbox.module.home.bean.HomeBean
import com.messcat.tvbox.module.home.bean.WeatherBean
import com.messcat.tvbox.module.home.bean.WifiBean
import com.messcat.tvbox.module.home.contract.MainContract
import retrofit2.http.*
import rx.Observable
import rx.Observer

/**
 * Created by Administrator on 2017/8/23 0023.
 */
class MainPresenter(mView: MainContract.View?, loader: MainLoader?) : RxPresenter<MainContract.View>()
        , MainContract.Presenter {


    private var view = mView
    private var mainLoader = loader

    override fun getIndexInfo(diCode: String?, roomNum: String?, type: String?) {
//        view?.showLoadingDialog()
        mainLoader?.getIndexInfo(diCode, roomNum, type)?.subscribe(object : Observer<HomeBean> {
            override fun onError(e: Throwable?) {
                view?.showError(e?.toString())
//                view?.dismissLoadingDialog()
            }

            override fun onNext(t: HomeBean?) {
                if ("200".equals(t?.status)) {
//                    view?.dismissLoadingDialog()
                    view?.getIndexInfoView(t, type)
                } else {
//                    view?.dismissLoadingDialog()
                    view?.showError(t?.message)
                }
            }

            override fun onCompleted() {
//                view?.dismissLoadingDialog()
            }

        })
    }

    override fun getApplication(diCode: String?, roomNum: String?, type: String?) {
//        view?.showLoadingDialog()
        mainLoader?.getApplication(diCode, roomNum, type)?.subscribe(object : Observer<ApplicationBean> {
            override fun onError(e: Throwable?) {
                view?.showError(e?.toString())
//                view?.dismissLoadingDialog()
            }

            override fun onNext(t: ApplicationBean?) {
                if ("200".equals(t?.status)) {
//                    view?.dismissLoadingDialog()
                    view?.getApplicationView(t, type)
                } else {
//                    view?.dismissLoadingDialog()
                    view?.showError(t?.message)
                }
            }

            override fun onCompleted() {
//                view?.dismissLoadingDialog()
            }

        })
    }


    class MainLoader(http: MainHttp) : ObjectLoader() {

        private var mainHttp: MainHttp = http

        fun getIndexInfo(diCode: String?, roomNum: String?, type: String?): Observable<HomeBean> = observe(mainHttp.indexInfo(diCode, roomNum, type))

        fun getApplication(diCode: String?, roomNum: String?, type: String?): Observable<ApplicationBean> = observe(mainHttp.getApplication(diCode, roomNum, type))

    }

    interface MainHttp {
        @FormUrlEncoded
        @POST("epIndex/getIndexInfo")
        fun indexInfo(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("type") type: String?): Observable<HomeBean>

        @FormUrlEncoded
        @POST("epIndex/getApplication")
        fun getApplication(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("type") type: String?): Observable<ApplicationBean>

    }
}
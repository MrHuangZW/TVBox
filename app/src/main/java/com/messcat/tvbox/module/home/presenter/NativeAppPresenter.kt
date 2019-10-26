package com.messcat.tvbox.module.home.presenter

import android.util.Log
import android.widget.Toast
import com.messcat.kotlin.mchttp.ObjectLoader
import com.messcat.mclibrary.base.RxPresenter
import com.messcat.tvbox.module.home.bean.ApplicationNewBean
import com.messcat.tvbox.module.home.bean.NativeAppBean
import com.messcat.tvbox.module.home.contract.NativeAppContract
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import rx.Observer
import java.util.*

/**
 * Created by Administrator on 2017/8/25 0025.
 */
class NativeAppPresenter(mView: NativeAppContract.View, loader: NativeAppLoader) : RxPresenter<NativeAppContract.View>(), NativeAppContract.Presenter {

    private var view = mView
    private var mLoader = loader

    override fun getApplicationInfo(id: String?, diCode: String?, roomNum: String?, type: String?) {
        view.showLoadingDialog()
        mLoader.getApplicationInfo(id, diCode, roomNum, type).subscribe(object : Observer<ApplicationNewBean> {
            override fun onError(e: Throwable?) {
                view.dismissLoadingDialog()
                view.showError(e.toString())
            }

            override fun onCompleted() {
                view.dismissLoadingDialog()
            }

            override fun onNext(t: ApplicationNewBean?) {
                if ("200".equals(t?.status)) {
                    view.getAppInfo(t)
                } else {
                    view.showError(t?.message)
                }
            }

        })
    }

    override fun getApplicationInfo(diCode: String?, roomNum: String?, type: String?) {
        view.showLoadingDialog()
        mLoader.getApplicationInfo(diCode, roomNum, type).subscribe(object : Observer<ApplicationNewBean> {
            override fun onError(e: Throwable?) {
                view.dismissLoadingDialog()
                view.showError(e.toString())
            }

            override fun onCompleted() {
                view.dismissLoadingDialog()
            }

            override fun onNext(t: ApplicationNewBean?) {
//                view.getAppInfo()
//                if ("200".equals(t?.status)) {
//                    view.getAppInfo(t)
//                } else {
//                    view.showError(t?.message)
//                }
            }

        })
    }

    class NativeAppLoader(nativeApp: NativeAppHttp) : ObjectLoader() {
        private var nativeAppHttp: NativeAppHttp = nativeApp

        fun getApplicationInfo(id: String?, diCode: String?, roomNum: String?, type: String?): Observable<ApplicationNewBean> = observe(nativeAppHttp.applicationInfo(id, diCode, roomNum, type))

        fun getApplicationInfo(diCode: String?, roomNum: String?, type: String?): Observable<ApplicationNewBean> = observe(nativeAppHttp.applicationInfo(diCode, roomNum, type))
    }

    interface NativeAppHttp {
        @FormUrlEncoded
        @POST("epIndex/getApplicationInfo")
        fun applicationInfo(@Field("id") id: String?, @Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("type") type: String?): Observable<ApplicationNewBean>

        @FormUrlEncoded
        @POST("epIndex/getApplicationInfo")
        fun applicationInfo(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("type") type: String?): Observable<ApplicationNewBean>
    }
}
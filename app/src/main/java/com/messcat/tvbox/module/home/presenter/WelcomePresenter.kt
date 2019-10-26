package com.messcat.tvbox.module.home.presenter

import android.util.Log
import com.messcat.kotlin.mchttp.ObjectLoader
import com.messcat.mclibrary.base.RxPresenter
import com.messcat.tvbox.module.Constants
import com.messcat.tvbox.module.home.bean.*
import com.messcat.tvbox.module.home.contract.WelcomeContract
import retrofit2.http.*
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1

/**
 * Created by Administrator on 2017/9/11 0011.
 */
class WelcomePresenter(mView: WelcomeContract.View, welcomeLoader: WelcomeLoader) : RxPresenter<WelcomeContract.View>(), WelcomeContract.Presenter {


    private var view = mView
    private var mWelcomeLoader = welcomeLoader
    override fun getWelcomeInfo(diCode: String?, roomNum: String?, type: String?, loginInfoId: Long?) {
        view.showLoadingDialog()
        mWelcomeLoader.getWelcomeInfo(diCode, roomNum, type, loginInfoId).doOnNext(object : Action1<WelcomeBean> {
            override fun call(t: WelcomeBean?) {
                if ("200".equals(t?.status)) {
                    Log.d("lyhlog", "请求成功")
                    view.getWelcomeData(t, type, loginInfoId)
                } else {
                    Log.d("lyhlog", "请求失败")
                    view.showError(t?.message)
                    view.dismissLoadingDialog()
                }
            }

        }).flatMap(object : Func1<WelcomeBean, Observable<WeatherBean>> {
            override fun call(t: WelcomeBean?): Observable<WeatherBean> {
                return mWelcomeLoader?.getWeather(t?.result?.welcomeInfo?.hotelInfo?.city, Constants.key)
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WeatherBean> {
                    override fun onCompleted() {
                        view.dismissLoadingDialog()
                    }

                    override fun onError(e: Throwable?) {
                        view?.weatherError(e.toString())
                        view.dismissLoadingDialog()
                    }

                    override fun onNext(t: WeatherBean?) {
                        if ("200".equals(t?.resultcode)) {
                            view?.getWeather(t)
                        } else {
                            view?.weatherError("获取不到天气信息")
                        }
                    }

                })
    }

    override fun getWifiState(diCode: String?, roomNum: String?) {
        mWelcomeLoader?.getWifiStatus(diCode, roomNum)?.subscribe(object : Observer<WifiBean> {
            override fun onCompleted() {
                view?.dismissLoadingDialog()
            }

            override fun onNext(t: WifiBean?) {
                if ("200".equals(t?.status)) {
                    view?.wifiState(t)
                } else {
                    view?.showError(t?.message)
                    view.wifiStateError()
                }
            }

            override fun onError(e: Throwable?) {
                view?.showError(e.toString())
                view?.dismissLoadingDialog()
                view.wifiStateError()
            }
        })
    }

    override fun updateLoginInfo(loginInfoId: Long?) {
        mWelcomeLoader?.updateLoginInfo(loginInfoId)?.subscribe(object : Observer<UpdateLoginInfoBean> {
            override fun onCompleted() {
                view?.dismissLoadingDialog()
            }

            override fun onNext(t: UpdateLoginInfoBean?) {
                if ("200".equals(t?.status)) {
                    view?.updateLoginInfo()
                } else {
//                    view?.showError(t?.message)
                }
            }

            override fun onError(e: Throwable?) {
//                view?.showError(e.toString())
                view?.dismissLoadingDialog()
            }
        })
    }

    override fun updateLoginInfoAgain(loginInfoId: Long?) {
        mWelcomeLoader?.updateLoginInfoAgain(loginInfoId)?.subscribe(object : Observer<UpdateLoginInfoBean> {
            override fun onCompleted() {
                view?.dismissLoadingDialog()
            }

            override fun onNext(t: UpdateLoginInfoBean?) {
                if ("200".equals(t?.status)) {
                    view?.updateLoginInfoAgain()
                } else {
//                    view?.showError(t?.message)
                }
            }

            override fun onError(e: Throwable?) {
//                view?.showError(e.toString())
                view?.dismissLoadingDialog()
            }
        })
    }

    override fun addLoginInfo(diCode: String?, roomNum: String?) {
        mWelcomeLoader.addLoginInfo(diCode, roomNum).subscribe(object : Observer<AddLoginInfoBean> {
            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: AddLoginInfoBean?) {
                if ("200".equals(t?.status)) {
                    view?.addLoginInfoResponse(t)
                }
            }

            override fun onCompleted() {
            }

        })
    }

    override fun addLoginInfoAgain(diCode: String?, roomNum: String?) {
        mWelcomeLoader.addLoginInfoAgain(diCode, roomNum).subscribe(object : Observer<AddLoginInfoBean> {
            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: AddLoginInfoBean?) {
                if ("200".equals(t?.status)) {
                    view?.addLoginInfoAgainResponse(t)
                }
            }

            override fun onCompleted() {
            }

        })
    }

    class WelcomeLoader(welcomeHttp: WelcomeHttp) : ObjectLoader() {
        private var mWelcomeHttp = welcomeHttp
        fun getWelcomeInfo(diCode: String?, roomNum: String?, type: String?, loginInfoId: Long?): Observable<WelcomeBean> = observe(mWelcomeHttp.welcomeInfo(diCode, roomNum, type, loginInfoId))

        fun getWeather(cityname: String?, key: String?): Observable<WeatherBean> = observe(mWelcomeHttp.weather(cityname, key))

        fun getWifiStatus(diCode: String?, roomNum: String?): Observable<WifiBean> = observe(mWelcomeHttp.wifiStatus(diCode, roomNum))

        fun updateLoginInfo(loginInfoId: Long?): Observable<UpdateLoginInfoBean> = observe(mWelcomeHttp.updateLoginInfo(loginInfoId))

        fun updateLoginInfoAgain(loginInfoId: Long?): Observable<UpdateLoginInfoBean> = observe(mWelcomeHttp.updateLoginInfo(loginInfoId))

        fun addLoginInfo(diCode: String?, roomNum: String?): Observable<AddLoginInfoBean> = observe(mWelcomeHttp.addLoginInfo(diCode, roomNum))

        fun addLoginInfoAgain(diCode: String?, roomNum: String?): Observable<AddLoginInfoBean> = observe(mWelcomeHttp.addLoginInfo(diCode, roomNum))
    }

    interface WelcomeHttp {
        @FormUrlEncoded
        @POST("epIndex/getWelcomeInfo")
        fun welcomeInfo(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("type") type: String?, @Field("loginInfoId") loginInfoId: Long?): Observable<WelcomeBean>

        @GET("http://v.juhe.cn/weather/index")
        fun weather(@Query("cityname") cityname: String?, @Query("key") key: String?): Observable<WeatherBean>

        @FormUrlEncoded
        @POST("epIndex/getWifiStatus")
        fun wifiStatus(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?): Observable<WifiBean>

        @FormUrlEncoded
        @POST("epIndex/updateLoginInfo")
        fun updateLoginInfo(@Field("loginInfoId") loginInfoId: Long?): Observable<UpdateLoginInfoBean>

        @FormUrlEncoded
        @POST("epIndex/addLoginInfo")
        fun addLoginInfo(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?): Observable<AddLoginInfoBean>
    }
}
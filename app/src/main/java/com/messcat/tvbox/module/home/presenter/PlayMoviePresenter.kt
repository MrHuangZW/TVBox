package com.messcat.tvbox.module.home.presenter

import com.messcat.kotlin.mchttp.ObjectLoader
import com.messcat.mclibrary.base.RxPresenter
import com.messcat.tvbox.module.home.bean.PlayMovieBean
import com.messcat.tvbox.module.home.contract.PlayMovieContract
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import rx.Observer

/**
 * Created by Administrator on 2017/9/18 0018.
 */
class PlayMoviePresenter(mView: PlayMovieContract.View, playMovieLoader: PlayMovieLoader?) :
        RxPresenter<PlayMovieContract.View>(), PlayMovieContract.Presenter {

    private var mPlayMovieLoader = playMovieLoader
    private var view = mView

    override fun getVideo(diCode: String?, roomNum: String?) {
        mPlayMovieLoader?.getHotelVedio(diCode,roomNum)?.subscribe(object : Observer<PlayMovieBean> {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view.showError(e.toString())
            }

            override fun onNext(t: PlayMovieBean?) {
                if ("200".equals(t?.status)) {
                    view.getVideo(t)
                } else {
                    view.showError(t?.message)
                }
            }
        })
    }

    class PlayMovieLoader(playMovieHttp: PlayMovieHttp) : ObjectLoader() {
        private var mPlayMovieHttp = playMovieHttp
        fun getHotelVedio(diCode: String?, roomNum: String?): Observable<PlayMovieBean> = observe(mPlayMovieHttp.hotelvedio(diCode, roomNum))
    }

    interface PlayMovieHttp {
        @FormUrlEncoded
        @POST("epIndex/getHotelVedio")
        fun hotelvedio(@Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?): Observable<PlayMovieBean>
    }
}
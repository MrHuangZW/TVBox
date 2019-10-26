package com.messcat.tvbox.module.home.presenter

import com.messcat.kotlin.mchttp.ObjectLoader
import com.messcat.mclibrary.base.RxPresenter
import com.messcat.mclibrary.util.showWheelDialog
import com.messcat.tvbox.module.home.bean.ClassifyBean
import com.messcat.tvbox.module.home.contract.ClassifyContract
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable
import rx.Observer

/**
 * Created by Administrator on 2017/9/5 0005.
 */
class ClassifyPresenter(mView: ClassifyContract.View?, loader: ClassifyLoader?) : RxPresenter<ClassifyContract.View>(), ClassifyContract.Presenter {

    private var view = mView
    private var classifyLoader = loader

    override fun getCategoryInfo(id: String?, diCode: String?, roomNum: String?, type: String?) {
        view?.showLoadingDialog()
        classifyLoader?.getCategoryInfo(id, diCode, roomNum, type)?.subscribe(object : Observer<ClassifyBean> {
            override fun onError(e: Throwable?) {
                view?.showError(e?.toString())
                view?.dismissLoadingDialog()
            }

            override fun onCompleted() {
                view?.dismissLoadingDialog()
            }

            override fun onNext(t: ClassifyBean?) {
                if ("200".equals(t?.status)) {
                    view?.getInfo(t)
                } else {
                    view?.showError(t?.message)
                }
            }

        })
    }

    class ClassifyLoader(infor: ClassifyHttp) : ObjectLoader() {
        var classifyHttp: ClassifyHttp = infor

        fun getCategoryInfo(id: String?, diCode: String?, roomNum: String?, type: String?): Observable<ClassifyBean> =
                observe(classifyHttp.categoryInfo(id, diCode, roomNum, type))
    }

    interface ClassifyHttp {
        @FormUrlEncoded
        @POST("epIndex/getCategoryInfo")
        fun categoryInfo(@Field("id") id: String?, @Field("diCode") diCode: String?, @Field("roomNum") roomNum: String?, @Field("type") type: String?): Observable<ClassifyBean>
    }
}
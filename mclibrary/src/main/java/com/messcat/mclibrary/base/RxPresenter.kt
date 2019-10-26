package com.messcat.mclibrary.base

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 *基于Rx的Presenter封装,控制订阅的生命周期
 * Created by Administrator on 2017/8/11 0011.
 */
open class RxPresenter<V : BaseView> : BasePresenter<V> {

    var mView: V? = null
    var mCompositeSubscription: CompositeSubscription? = null

    fun unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription!!.unsubscribe()
        }
    }

    fun addSubscribe(subscription: Subscription?) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeSubscription()
        }
        mCompositeSubscription!!.add(subscription)
    }

    override fun attachView(view: V) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
        unSubscribe()
    }
}
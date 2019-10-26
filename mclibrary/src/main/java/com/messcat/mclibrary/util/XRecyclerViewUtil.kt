package com.messcat.mclibrary.util

import android.support.v7.widget.RecyclerView
import com.jcodecraeer.xrecyclerview.XRecyclerView


/**
 * Created by Administrator on 2017/9/1 0001.
 */

private var isRefresh: Boolean? = null
private var pageNo: Int = 0

/**
 * 初始化XRecyclerView
 * 参数1 XRecyclerView
 * 参数2 滑动操作
 * 参数3 是否固定
 * 参数4 动画
 */
fun Any.initXRecyclerView(xrecyclerView: XRecyclerView?, scrollingEnabled: Boolean, fixedSize: Boolean, animator: RecyclerView.ItemAnimator?) {
    xrecyclerView?.setNestedScrollingEnabled(scrollingEnabled)
    xrecyclerView?.setHasFixedSize(fixedSize)
    xrecyclerView?.setItemAnimator(animator)
}

/**
 * 实现上拉刷新下拉加载的操作
 *
 * 参数1 XRecyclerView
 * 参数2 刷新的样式
 * 参数3 加载的样式
 * 参数4 回调接口
 */
fun Any.refreshXRecyclerView(xrecyclerView: XRecyclerView?, refershStyle: Int?, loadingStyle: Int?, listener: OnLoadingListener?) {
    xrecyclerView?.setLoadingListener(object : XRecyclerView.LoadingListener {
        override fun onRefresh() {
            isRefresh = true
            pageNo = 1
            listener?.onRefresh(isRefresh, pageNo)
        }

        override fun onLoadMore() {
            isRefresh = false
            pageNo++
            listener?.onLoading(isRefresh, pageNo)
        }
    })
    xrecyclerView?.setRefreshProgressStyle(refershStyle!!)
    xrecyclerView?.setLoadingMoreProgressStyle(loadingStyle!!)
}

/**
 * 刷新
 * 参数1 XRecyclerView
 */
fun Any.xRefresh(xrecyclerView: XRecyclerView?) {
    xrecyclerView?.refresh()
}

interface OnLoadingListener {
    fun onRefresh(isRefresh: Boolean?, pageNo: Int?)
    fun onLoading(isRefresh: Boolean?, pageNo: Int?)
}
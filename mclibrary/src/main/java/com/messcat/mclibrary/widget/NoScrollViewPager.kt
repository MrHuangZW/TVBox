package com.messcat.kotlin.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 禁止侧滑的ViewPager
 * Created by Administrator on 2017/8/11 0011.
 */
class NoScrollViewPager(context: Context?, attrs: AttributeSet?) : ViewPager(context, attrs) {

    var scroll: Boolean = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (scroll) {
            return false
        } else {
            return super.onTouchEvent(ev)
        }
    }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        if (scroll) {
            return false
        } else {
            return super.onInterceptHoverEvent(event)
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    /**
     * 设置滑动的时候不需要时间
     */
    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }

    fun setNoScroll(scroll: Boolean) {
        this.scroll = scroll
    }
}
package com.messcat.mclibrary.widget

import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation



/**
 * 动画实现累
 * Created by Administrator on 2017/9/7 0007.
 */
class ScaleAnimEffect {
    private var fromXScale: Float = 0.toFloat()
    private var toXScale: Float = 0.toFloat()
    private var fromYScale: Float = 0.toFloat()
    private var toYScale: Float = 0.toFloat()
    private var duration: Long = 0

    // private long offSetDuration;

    /**
     * 设置缩放参数
     *
     * @param fromXScale
     * 初始X轴缩放比例
     * @param toXScale
     * 目标X轴缩放比例
     * @param fromYScale
     * 初始Y轴缩放比例
     * @param toYScale
     * 目标Y轴缩放比例
     * @param duration
     * 动画持续时间
     */
    fun setAttributs(fromXScale: Float, toXScale: Float,
                     fromYScale: Float, toYScale: Float, duration: Long) {
        this.fromXScale = fromXScale
        this.fromYScale = fromYScale
        this.toXScale = toXScale
        this.toYScale = toYScale
        this.duration = duration
    }

    fun createAnimation(): Animation {
        val anim = ScaleAnimation(fromXScale, toXScale,
                fromYScale, toYScale, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        anim.fillAfter = true
        anim.interpolator = AccelerateInterpolator()
        anim.duration = duration
        return anim
    }

    fun alphaAnimation(fromAlpha: Float, toAlpha: Float,
                       duration: Long, offsetDuration: Long): Animation {
        val anim = AlphaAnimation(fromAlpha, toAlpha)
        anim.duration = duration
        anim.startOffset = offsetDuration
        anim.interpolator = AccelerateInterpolator()
        return anim
    }
}
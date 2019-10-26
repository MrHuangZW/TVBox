package com.messcat.mclibrary.util

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.Banner
import com.youth.banner.loader.ImageLoader

/**
 * Created by Administrator on 2017/9/1 0001.
 */

/**
 * 初始化Banner
 * 参数1 Banner
 * 参数2 Activity
 * 参数3 自动切换时间
 * 参数4 Banner高度
 */
fun Any.initBanner(banner: Banner?, activity: Activity?, time: Int, height: Int) {
    val layoutParams = banner?.getLayoutParams()
    val width = activity?.getWindowManager()?.getDefaultDisplay()?.getWidth()
    layoutParams?.height = height
    banner?.setLayoutParams(layoutParams)
    banner?.setDelayTime(time)
    banner?.setImageLoader(
            object : ImageLoader() {
                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                    Glide.with(context).load(path).into(imageView)//Glide框架
                }
            })
}

/**
 * 启动Banner
 * 参数1 Banner
 * 参数2 图片路径集合
 */
fun Any.stratBanner(banner: Banner?, listUrl: MutableList<String>) {
    banner?.setImages(listUrl)//图片地址集合
    banner?.start()//开始播放
}
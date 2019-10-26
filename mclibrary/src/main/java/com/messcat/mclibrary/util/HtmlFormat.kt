package com.messcat.mclibrary.util

import org.jsoup.Jsoup

/**
 * Html代码适配手机
 * Created by Administrator on 2017/8/30 0030.
 */
fun getNewContent(htmltext: String): String {

    val doc = Jsoup.parse(htmltext)
    val elements = doc.getElementsByTag("img")
    for (element in elements) {
        element.attr("width", "100%").attr("height", "auto")
    }
    return doc.toString()
}
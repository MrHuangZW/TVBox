package com.messcat.kotlin.utils

import android.text.format.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2017/8/23 0023.
 */
private val TIP = "yyyy年MM月"
private val SIM = "yyyy-MM-dd"
private val COX = "yyyy-MM-dd HH:mm:ss"
private val COM = "yyyyMMddHHmmss"
private val MMDD = "MM月dd日"
private val DAY = "dd"

/**
 * 将时间戳转换为 COX
 *
 * @param time
 * @return
 */
fun Any.getTodayDateTime(time: Long): String {
    val format = SimpleDateFormat(COX)
    val date = Date(time)
    return format.format(date)
}

/**
 * 将时间戳转换为 TIP
 *
 * @param time
 * @return
 */
fun Any.getYearDate(time: Long): String {
    val format = SimpleDateFormat(TIP)
    val date = Date(time)
    return format.format(date)
}

/**
 * 将时间戳转化为日期
 *
 * @param time
 * @return
 */
fun Any.getDay(time: Long): String {
    val format = SimpleDateFormat(DAY)
    val date = Date(time)
    return format.format(date)
}

/**
 * 将时间戳转换为 MMDD
 *
 * @param time
 * @return
 */
fun Any.getMonthEndDay(time: String): String? {
    var re_StrTime: String? = null
    val sdf = SimpleDateFormat(MMDD)
    // 例如：cc_time=1291778220
    val lcc_time = java.lang.Long.valueOf(time)!!
    re_StrTime = sdf.format(Date(lcc_time * 1000L))
    return re_StrTime
}

/**
 * 将时间戳转为代表"距现在多久之前"的字符串
 *
 * @param timeStr 时间戳
 * @return
 */
fun Any.getStandardDate(timeStr: String): String {

    val sb = StringBuffer()

    val t = java.lang.Long.parseLong(timeStr)
    val time = Math.abs(System.currentTimeMillis() - t)
    val mill = Math.ceil((time / 1000).toDouble()).toLong()//秒前

    val minute = Math.ceil((time.toFloat() / 60f / 1000.0f).toDouble()).toLong()// 分钟前

    val hour = Math.ceil((time.toFloat() / 60f / 60f / 1000.0f).toDouble()).toLong()// 小时

    val day = Math.ceil((time.toFloat() / 24f / 60f / 60f / 1000.0f).toDouble()).toLong()// 天前

    if (day - 1 > 0) {
        sb.append(day.toString() + "天")
    } else if (hour - 1 > 0) {
        if (hour >= 24) {
            sb.append("1天")
        } else {
            sb.append(hour.toString() + "小时")
        }
    } else if (minute - 1 > 0) {
        if (minute == 60L) {
            sb.append("1小时")
        } else {
            sb.append(minute.toString() + "分钟")
        }
    } else if (mill - 1 > 0) {
        if (mill == 60L) {
            sb.append("1分钟")
        } else {
            sb.append(mill.toString() + "秒")
        }
    } else {
        sb.append("刚刚")
    }
    if (sb.toString() != "刚刚") {
        sb.append("前")
    }
    return sb.toString()
}

/**
 * 获取当前年月
 *
 * @return
 */
fun Any.getYYMM(): String {
    val time = Time("GMT+8")
    time.setToNow()
    val year = time.year
    val month = time.month
    val day = time.monthDay
    val minute = time.minute
    val hour = time.hour
    val sec = time.second
    return year.toString() + month.toString()
}

/**
 * 获取当前月日
 *
 * @return
 */
fun Any.getMMDD(): String {
    val time = Time("GMT+8")
    time.setToNow()
    val year = time.year
    val month = time.month
    val day = time.monthDay
    val minute = time.minute
    val hour = time.hour
    val sec = time.second
    return month.toString() + day.toString()
}

/**
 * 得出时间差
 *
 * @param nowtime
 * @param endtime
 * @return
 */
fun Any.timeDifference(nowtime: String, endtime: Long): Long {
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var diff: Long = 0
    try {
        //系统时间转化为Date形式
        val dstart = format.parse("2016-12-17 15:24:53")
        //活动结束时间转化为Date形式
        //            Date dend = format.parse(endtime);
        //算出时间差，用ms表示
        val data = dstart.time
        diff = dstart.time + endtime - dstart.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    //返回时间差
    return diff
}

/**
 * 获取当前年月日
 */
fun Any.getYear(): String {
    var c: Calendar = Calendar.getInstance()
    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
    var mYear: String = c.get(Calendar.YEAR).toString()// 获取当前年份
    var mMonth: String = (c.get(Calendar.MONTH) + 1).toString()// 获取当前月份
    var mDay: String = c.get(Calendar.DAY_OF_MONTH).toString()// 获取当前月份的日期号码
//    var mWay: String = c.get(Calendar.DAY_OF_WEEK).toString()//星期
//    if ("1".equals(mWay)) {
//        mWay = "天"
//    } else if ("2".equals(mWay)) {
//        mWay = "一"
//    } else if ("3".equals(mWay)) {
//        mWay = "二"
//    } else if ("4".equals(mWay)) {
//        mWay = "三"
//    } else if ("5".equals(mWay)) {
//        mWay = "四"
//    } else if ("6".equals(mWay)) {
//        mWay = "五"
//    } else if ("7".equals(mWay)) {
//        mWay = "六"
//    }
    return "$mYear-$mMonth-$mDay"
}

/**
 * 获取当前时分秒
 */
fun Any.getTime(): String {
    var c: Calendar = Calendar.getInstance()
    c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
    var mHour: String = c.get(Calendar.HOUR_OF_DAY).toString()//时
    var mMinute: String = c.get(Calendar.MINUTE).toString()//分
    var mSecond: String = c.get(Calendar.SECOND).toString()//秒
    if (mMinute.toInt() < 10) {
        mMinute = "0$mMinute"
    }
    return "$mHour:$mMinute"
}

/**
 * 获取时间戳
 */
fun Any.getUnixTime(): String {
    return (System.currentTimeMillis() / 1000L).toString()
}
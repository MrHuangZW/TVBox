@file:JvmName("SPUtil")
@file:JvmMultifileClass

package com.messcat.mclibrary.util

import android.content.Context
import android.content.SharedPreferences
import com.messcat.mclibrary.base.*
import java.util.ArrayList

/**
 * Created by Administrator on 2017/9/21 0021.
 */
private val CONFIG = "config"

/**
 * 获取SharedPreferences实例对象
 *
 * @param fileName
 */
private fun getSharedPreference(fileName: String): SharedPreferences {
    return BaseApplication.getInstance()?.getSharedPreferences(fileName, Context.MODE_PRIVATE)!!
}

/**
 * 保存一个String类型的值！
 */
fun putString(key: String, value: String) {
    val editor = getSharedPreference(CONFIG).edit()
    editor.putString(key, value).apply()
}

/**
 * 获取String的value
 */
fun getStrings(key: String, defValue: String?): String? {
    val sharedPreference = getSharedPreference(CONFIG)
    return sharedPreference.getString(key, defValue)
}

/**
 * 获取String的value
 */
fun Any.getSPString(key: String, defValue: String?): String? {
    val sharedPreference = getSharedPreference(CONFIG)
    return sharedPreference.getString(key, defValue)
}

/**
 * 保存一个Boolean类型的值！
 */
fun Any.putBoolean(key: String, value: Boolean?) {
    val editor = getSharedPreference(CONFIG).edit()
    editor.putBoolean(key, value!!).apply()
}

/**
 * 获取boolean的value
 */
fun Any.getBoolean(key: String, defValue: Boolean?): Boolean {
    val sharedPreference = getSharedPreference(CONFIG)
    return sharedPreference.getBoolean(key, defValue!!)
}

/**
 * 保存一个int类型的值！
 */
fun Any.putInt(key: String, value: Int) {
    val editor = getSharedPreference(CONFIG).edit()
    editor.putInt(key, value).apply()
}

/**
 * 获取int的value
 */
fun Any.getInt(key: String, defValue: Int): Int {
    val sharedPreference = getSharedPreference(CONFIG)
    return sharedPreference.getInt(key, defValue)
}

/**
 * 保存一个float类型的值！
 */
fun Any.putFloat(fileName: String, key: String, value: Float) {
    val editor = getSharedPreference(fileName).edit()
    editor.putFloat(key, value).apply()
}

/**
 * 获取float的value
 */
fun Any.getFloat(key: String, defValue: Float?): Float {
    val sharedPreference = getSharedPreference(CONFIG)
    return sharedPreference.getFloat(key, defValue!!)
}

/**
 * 保存一个long类型的值！
 */
fun Any.putLong(key: String, value: Long) {
    val editor = getSharedPreference(CONFIG).edit()
    editor.putLong(key, value).apply()
}

/**
 * 获取long的value
 */
fun Any.getLong(key: String, defValue: Long): Long {
    val sharedPreference = getSharedPreference(CONFIG)
    return sharedPreference.getLong(key, defValue)
}

/**
 * 取出List<String>
 *
 * @param key List<String> 对应的key
 * @return List<String>
</String></String></String> */
fun Any.getStrListValue(key: String): List<String> {
    val strList = ArrayList<String>()
    val size = getInt(key + "size", 0)
    //Log.d("sp", "" + size);
    for (i in 0..size - 1) {
        strList.add(getStrings(key + i, null)!!)
    }
    return strList
}

/**
 * 存储List<String>
 *
 * @param key     List<String>对应的key
 * @param strList 对应需要存储的List<String>
</String></String></String> */
fun Any.putStrListValue(key: String, strList: List<String>?) {
    if (null == strList) {
        return
    }
    // 保存之前先清理已经存在的数据，保证数据的唯一性
    removeStrList(key)
    val size = strList.size
    putInt(key + "size", size)
    for (i in 0..size - 1) {
        putString(key + i, strList[i])
    }
}

/**
 * 清空List<String>所有数据
 *
 * @param key List<String>对应的key
</String></String> */
fun Any.removeStrList(key: String) {
    val size = getInt(key + "size", 0)
    if (0 == size) {
        return
    }
    remove(key + "size")
    for (i in 0..size - 1) {
        remove(key + i)
    }
}


/**
 * 清空对应key数据
 */
fun Any.remove(key: String) {
    val editor = getSharedPreference(CONFIG).edit()
    editor.remove(key).apply()
}

fun Any.getNightMode(): Boolean {
    return getBoolean(KEY_MODE_NIGHT, false)
}

fun Any.setNightMode(nightMode: Boolean) {
    putBoolean(KEY_MODE_NIGHT, nightMode)
}

/**
 * 保存登录信息
 *
 * @param member
 */
fun Any.saveMember(member: String?) {
    if (member == null)
        return
    putString(MEMBER, member)
}

/**
 * 获取登录信息
 *
 * @return
 */
fun getMember(): String? = getStrings(MEMBER, null)

/**
 * 保存主页底部中文数据
 */
fun Any.saveMainBottom(sel: String?) {
    if (sel == null) {
        return
    }
    putString(MAIN_BOTTOM, sel)
}

/**
 * 保存主页底部英文数据
 */
fun Any.saveEnglishMainBottom(sel: String?) {
    if (sel == null) {
        return
    }
    putString(ENGLISH_MAIN_BOTTOM, sel)
}


/**
 * 保存个人中心中文数据
 */
fun Any.saveMain(sel: String?) {
    if (sel == null) {
        return
    }
    putString(MAIN, sel)
}

/**
 * 保存个人中心英文数据
 */
fun Any.saveEnglishMain(sel: String?) {
    if (sel == null) {
        return
    }
    putString(ENGLISH_MAIN, sel)
}

fun Any.getWifiBean():String?=getStrings("wifibean","");
fun Any.saveWifiBean(wifibean:String?){
    if (wifibean==null){
        return
    }
    putString("wifibean",wifibean)
}

/**
 * 获取首页数据
 * @return
 */
fun Any.getMain(): String? = getStrings(MAIN, null)

/**
 * 获取首页数据
 * @return
 */
fun Any.getEnglishMain(): String? = getStrings(ENGLISH_MAIN, null)

/**
 * 获取首页底部中文数据
 * @return
 */
fun Any.getMainBottom(): String? = getStrings(MAIN_BOTTOM, null)

/**
 * 获取首页底部英文数据
 * @return
 */
fun Any.getEnglishMainBottom(): String? = getStrings(ENGLISH_MAIN_BOTTOM, null)

/**
 * 保存欢迎中文页面数据
 */
fun saveWelcome(welcome: String?) {
    if (welcome == null) {
        return
    }
    putString(WELCOME, welcome)
}

/**
 * 保存欢迎英文页面数据
 */
fun saveEnglishWelcome(welcome: String?) {
    if (welcome == null) {
        return
    }
    putString(ENGLISH_WELCOME, welcome)
}

/**
 * 获取欢迎页面中文数据
 */
fun Any.getWelcome(): String? = getStrings(WELCOME, null)

/**
 * 获取欢迎界面英文数据
 */
fun Any.getEnglishWelcome():String?=getStrings(ENGLISH_WELCOME, null)

/**
 * 保存天气信息
 */
fun saveWeather(weather: String?) {
    if (weather == null) {
        return
    }
    putString(WEATHER, weather)
}

/**
 * 获取天气信息
 */
fun Any.getWeather(): String? = getStrings(WEATHER, null)

/**
 * 保存分类数据
 */
fun Any.saveClass(cla: String) {
    if (cla == null) {
        return
    }
    putString(CLA, cla)
}

/**
 * 获取分类数据
 */
fun Any.getClass(): String? = getStrings(CLA, null)

/**
 * 保存本地应用信息
 */
fun Any.saveNativeApp(native: String) {
    if (native == null) {
        return
    }
    putString(NATIVE, native)
}

/**
 * 获取本地应用信息
 */
fun Any.getNativeApp(): String? = getStrings(NATIVE, null)

/**
 * 吃喝玩了
 */
fun Any.saveSkittles(skittles: String) {
    if (skittles == null) {
        return
    }
    putString(SKITTLES, skittles)
}

/**
 * 获取数据
 */
fun Any.getSkittles(): String? = getStrings(SKITTLES, null)

/**
 * 左上
 */
fun Any.saveLeftTop(leftTop: String) {
    if (leftTop == null) {
        return
    }
    putString(LEFTTOP, leftTop)
}

/**
 * 获取左上
 */
fun Any.getLeftTop(): String? = getStrings(LEFTTOP, null)

/**
 * 左下
 */
fun Any.saveLeftBotton(leftBotton: String) {
    if (leftBotton == null) {
        return
    }
    putString(LEFTBOTTON, leftBotton)
}

/**
 * 获取左下
 */
fun Any.getLeftBotton(): String? = getStrings(LEFTBOTTON, null)

/**
 * 右上
 */
fun Any.saveRightTop(rightTop: String) {
    if (rightTop == null) {
        return
    }
    putString(RIGHTTOP, rightTop)
}

/**
 * 获取右上
 */
fun Any.getRightTop(): String? = getStrings(RIGHTTOP, null)

/**
 * 右下
 */
fun Any.saveRightBotton(rightBotton:String){
    if (rightBotton == null) {
        return
    }
    putString(RIGHTBOTTON, rightBotton)
}

/**
 * 获取右下
 */
fun Any.getRightBotton(): String? = getStrings(RIGHTBOTTON, null)
/**
 * 删除登录信息
 */
fun Any.clearSP(key: String) {
    remove(key)
}

/**
 * 退出登陆
 * 清除所有数据
 */
fun Any.clear() {
    val edit = getSharedPreference(CONFIG).edit()
    edit.clear()
    edit.commit()
}
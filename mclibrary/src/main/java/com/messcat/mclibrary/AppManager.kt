package com.messcat.mclibrary

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.util.*

/**
 * activity管理
 */

private var activityStack: Stack<Activity>? = Stack()

/**
 * 添加Activity到堆栈
 */
fun Any.addActivitys(activity: Activity) {
    if (activityStack == null) {
        activityStack = Stack<Activity>()
    }
    activityStack!!.add(activity)
}

/**
 * 获取当前Activity（堆栈中最后一个压入的）
 */
fun Any.currentActivitys(): Activity? {
    try {
        val activity = activityStack!!.lastElement()
        return activity
    } catch (e: Exception) {
        //            e.printStackTrace();
        return null
    }

}

/**
 * 获取当前Activity的前一个Activity
 */
fun Any.preActivitys(): Activity? {
    val index = activityStack!!.size - 2
    if (index < 0) {
        return null
    }
    val activity = activityStack!![index]
    return activity
}

/**
 * 结束当前Activity（堆栈中最后一个压入的）
 */
fun Any.finishActivitys() {
    val activity = activityStack!!.lastElement()
    finishActivitys(activity)
}

/**
 * 结束指定的Activity
 */
fun Any.finishActivitys(activitys: Activity?) {
    var activity: Activity? = activitys
    if (activity != null) {
        activityStack!!.remove(activity)
        activity.finish()
    }
}

/**
 * 移除指定的Activity
 */
fun Any.removeActivitys(activitys: Activity?) {
    var activity: Activity? = activitys
    if (activity != null) {
        activityStack!!.remove(activity)
        activity = null
    }
}

/**
 * 结束指定类名的Activity
 */
fun Any.finishActivitys(cls: Class<*>) {
    try {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivitys(activity)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/**
 * 结束所有Activity
 */
fun Any.finishAllActivitys() {
    var i = 0
    val size = activityStack!!.size
    while (i < size) {
        if (null != activityStack!![i]) {
            activityStack!![i].finish()
        }
        i++
    }
    activityStack!!.clear()
}

/**
 * 返回到指定的activity

 * @param cls
 */
fun Any.returnToActivitys(cls: Class<*>) {
    while (activityStack!!.size != 0)
        if (activityStack!!.peek().javaClass == cls) {
            break
        } else {
            finishActivitys(activityStack!!.peek())
        }
}

/**
 * 是否已经打开指定的activity
 * @param cls
 * *
 * @return
 */
fun Any.isOpenActivitys(cls: Class<*>): Boolean {
    if (activityStack != null) {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (cls == activityStack!!.peek().javaClass) {
                return true
            }
            i++
        }
    }
    return false
}

/**
 * 退出应用程序

 * @param context       上下文
 * *
 * @param isBackground  是否开开启后台运行
 */
fun Any.AppExits(context: Context?, isBackground: Boolean?) {
    try {
        finishAllActivitys()
        val activityMgr = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityMgr.restartPackage(context?.packageName!!)
    } catch (e: Exception) {

    } finally {
        // 注意，如果您有后台程序运行，请不要支持此句子
        if (isBackground!!) {
            System.exit(0)
        }
    }
}

/**
 * Activity跳转动画
 * @param activity
 */
fun jump2Next(activity: Activity) {
    activity.overridePendingTransition(R.anim.tran_next_in,
            R.anim.tran_next_out)
}

/**
 * 跳转Activity
 */
fun goToActivity(activity: Activity?, cls: Class<*>) = activity?.startActivity(Intent(activity, cls))

fun Any.goToActivity(activity: Activity?, cls: Class<*>, bundle: Bundle?) {
    var intent: Intent = Intent(activity, cls)
    intent.putExtras(bundle)
    activity?.startActivity(intent)
}
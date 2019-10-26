@file:JvmName("LogUtil")
@file:JvmMultifileClass

package com.messcat.kotlin.utils
import android.util.Log

/**
 * Log日志
 */
val TAGs = "TVBox"

fun v(msg: String?) = Log.v(TAGs, msg)

fun d(msg: String?) = Log.d(TAGs, msg)

fun i(msg: String?) = Log.i(TAGs, msg)

fun w(msg: String?) = Log.w(TAGs, msg)

fun e(msg: String?) = Log.e(TAGs, msg)

fun wtf(msg: String?) = Log.wtf(TAGs, msg)


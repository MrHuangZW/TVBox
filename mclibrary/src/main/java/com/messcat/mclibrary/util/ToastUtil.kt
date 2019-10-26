package com.messcat.kotlin.utils

import android.content.Context
import android.widget.Toast

/**
 * Toast
 * Created by Administrator on 2017/8/23 0023.
 */
/**
 * Toast短
 */
fun Any.toast(msg: String?,context: Context?) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

/**
 * Toast长
 */
fun Any.long_toast(msg: String?,context: Context?) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

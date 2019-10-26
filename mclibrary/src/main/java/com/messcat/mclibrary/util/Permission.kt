package com.messcat.mclibrary.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by Administrator on 2017/8/31 0031.
 */
fun Any.getLocationPermission(activity: Activity?) {

    val WRITE_COARSE_LOCATION_REQUEST_CODE = 1
    //开启位置权限
    if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //申请WRITE_EXTERNAL_STORAGE权限
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                WRITE_COARSE_LOCATION_REQUEST_CODE)//自定义的code
    }


}
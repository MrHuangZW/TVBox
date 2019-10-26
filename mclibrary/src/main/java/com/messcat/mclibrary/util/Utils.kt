package com.messcat.tvbox.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.widget.ImageView
import com.bumptech.glide.Glide
import android.content.ComponentName
import android.content.Context.WIFI_SERVICE
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import com.messcat.mclibrary.R
import java.util.*
import android.hardware.usb.UsbDevice.getDeviceId
import android.provider.Settings
import android.telephony.TelephonyManager
import java.util.UUID.randomUUID
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.widget.Toast
import com.messcat.kotlin.utils.e
import java.lang.reflect.AccessibleObject.setAccessible
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.os.ResultReceiver
import android.util.Log
import com.messcat.mclibrary.util.getClass
import java.lang.reflect.Field


/**
 * Created by Administrator on 2017/8/24 0024.
 */
/**
 * 用object替换class可以声明一个单例对象
 * Created by Administrator on 2017/8/17 0017.
 * 工具类 所有公共的操作都写在这里
 */

/**
 * 获取版本名称
 */
fun Any.versionName(context: Context): String = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName

/**
 * 获取版本版本号
 */
fun Any.versionCode(context: Context): Int = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode

/**
 * Activity获取屏幕的宽度
 */
fun Any.width(activity: Activity): Int = activity.windowManager.defaultDisplay.width

fun Any.height(activity: Activity): Int = activity.windowManager.defaultDisplay.height


/**
 * 获取当前屏幕截图，包含状态栏
 */
fun Activity.snapShotWithStatusBar(activity: Activity): Bitmap {
    window.decorView.isDrawingCacheEnabled = true
    window.decorView.buildDrawingCache()
    var bp = Bitmap.createBitmap(window.decorView.drawingCache, 0, 0, width(activity), height(activity))
    window.decorView.destroyDrawingCache()
    return bp
}

fun Activity.snapShotWithoutStatusBar(activity: Activity): Bitmap {
    window.decorView.isDrawingCacheEnabled = true
    window.decorView.buildDrawingCache()
    val frame = Rect()
    window.decorView.getWindowVisibleDisplayFrame(frame)
    val statusBarHeight = frame.top
    var bp = Bitmap.createBitmap(window.decorView.drawingCache, 0, statusBarHeight,
            width(activity), height(activity) - statusBarHeight)
    window.decorView.destroyDrawingCache()
    return bp
}

/**
 * 设置和加载图片
 */
fun Any.loadImage(activity: Context?, path: Any?, imageView: ImageView?) = Glide.with(activity).load(path)
        .placeholder(R.drawable.default_image)
        .error(R.drawable.default_image)
        .centerCrop()
        .into(imageView)

//打开其它APP,指定界面
fun Any.openOthemApp(context: Activity?, packageName: String?, className: String?) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val cn = ComponentName(packageName, className)
    intent.component = cn
    context!!.startActivity(intent)
}

//打开其它APP
fun Any.openOthemApp(context: Activity?, packageName: String?) {
    val packageManager = context!!.getPackageManager()
    var intent = Intent()
    intent = packageManager?.getLaunchIntentForPackage(packageName)!!
    context!!.startActivity(intent)
}

//获取手机的唯一标示UUID
fun Any.getUUID(context: Context?): String {
    val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val tmDevice: String
    val tmSerial: String
    val tmPhone: String
    val androidId: String
    tmDevice = "" + tm.deviceId
    tmSerial = "" + tm.simSerialNumber
    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID)
    val deviceUuid = UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
    val uniqueId = deviceUuid.toString()
    return uniqueId
}


/**
 * 创建Wifi热点
 * wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE)
 */
fun Any.setWifiAppEnabload(wifiManager: WifiManager?, WIFI_HOTSPOT_SSID: String?, sharedKey: String?, mActivity: Activity?): Boolean {
    if (wifiManager?.isWifiEnabled!!) { // disable WiFi in any case
        //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
        wifiManager?.setWifiEnabled(false)
    }
    var method: Method? = null
    try {
        method = wifiManager!!::class.java.getMethod("setWifiApEnabled",
                WifiConfiguration::class.java, Boolean::class.javaPrimitiveType)
        method!!.setAccessible(true)
        val netConfig = WifiConfiguration()
        netConfig.SSID = WIFI_HOTSPOT_SSID
        netConfig.preSharedKey = sharedKey
        netConfig.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN)
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
        netConfig.allowedKeyManagement
                .set(WifiConfiguration.KeyMgmt.WPA_PSK)
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.CCMP)
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.TKIP)
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.CCMP)
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.TKIP)

        return method!!.invoke(wifiManager, netConfig, true) as Boolean

    } catch (e: Exception) {
        return false
    }
}

private var mResultReceiver: ResultReceiver? = null
private var handler: Handler? = null

/**
 * 关闭WiFi热点
 */
fun Any.closeWifiHotspot(wifiManager: WifiManager?, WIFI_HOTSPOT_SSID: String?, sharedKey: String?): Boolean {
    if (!wifiManager?.isWifiEnabled!!) { // disable WiFi in any case
        //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
        wifiManager?.setWifiEnabled(true)
    }
    var method: Method? = null
    try {
        method = wifiManager!!::class.java.getMethod("setWifiApEnabled",
                WifiConfiguration::class.java, Boolean::class.javaPrimitiveType)
        method!!.setAccessible(true)
        val netConfig = WifiConfiguration()
        netConfig.SSID = WIFI_HOTSPOT_SSID
        netConfig.preSharedKey = sharedKey
        netConfig.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN)
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA)
        netConfig.allowedKeyManagement
                .set(WifiConfiguration.KeyMgmt.WPA_PSK)
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.CCMP)
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.TKIP)
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.CCMP)
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.TKIP)

        return method!!.invoke(wifiManager, netConfig, false) as Boolean

    } catch (e: Exception) {
        return false
    }
}

//判断APP是否存在
fun Any.isInstallApp(mPackageManager: PackageManager, packageName: String?): Boolean {
    try {
        mPackageManager.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
        return true

    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }
}

/**
 * 检测GPS是否打开
 *
 * @return
 */
fun Any.checkGPSIsOpen(mContext: Context?): Boolean {
    val isOpen: Boolean
    val locationManager = mContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    return isOpen
}

fun isWifiApOpen(context: Context?): Boolean {
    var manager = context?.getSystemService(Context.WIFI_SERVICE)
    var method = manager!!::class.java.getDeclaredMethod("getWifiApState")
    var state = method.invoke(manager)
    var field = manager::class.java.getDeclaredField("WIFI_AP_STATE_ENABLED")
    var value = field.get(manager)
    return state == value
}
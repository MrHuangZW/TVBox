@file:JvmName("NetWorkUtils")
@file:JvmMultifileClass
package com.messcat.mclibrary.util


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.net.SocketException

/**
 * Created by Administrator on 2017/8/30 0030.
 */
/**
 * 控制WIFI开关
 */
fun Any.toggleWiFi(context: Context?, enabled: Boolean) {
    var wm = context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
    wm.setWifiEnabled(enabled)
}

/**
 * 控制移动网络开关
 */
fun Any.toggleMobileData(context: Context, enabled: Boolean) {
    val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var conMgrClass: Class<*>? = null // ConnectivityManager类
    var iConMgrField: Field? = null // ConnectivityManager类中的字段
    var iConMgr: Any? = null // IConnectivityManager类的引用
    var iConMgrClass: Class<*>? = null // IConnectivityManager类
    var setMobileDataEnabledMethod: Method? = null // setMobileDataEnabled方法
    try {
        // 取得ConnectivityManager类
        conMgrClass = Class.forName(conMgr.javaClass.name)
        // 取得ConnectivityManager类中的对象mService
        iConMgrField = conMgrClass!!.getDeclaredField("mService")
        // 设置mService可访问
        iConMgrField!!.setAccessible(true)
        // 取得mService的实例化类IConnectivityManager
        iConMgr = iConMgrField!!.get(conMgr)
        // 取得IConnectivityManager类
        iConMgrClass = Class.forName(iConMgr!!.javaClass.name)
        // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
        setMobileDataEnabledMethod = iConMgrClass!!.getDeclaredMethod("setMobileDataEnabled", java.lang.Boolean.TYPE)
        // 设置setMobileDataEnabled方法可访问
        setMobileDataEnabledMethod!!.setAccessible(true)
        // 调用setMobileDataEnabled方法
        setMobileDataEnabledMethod!!.invoke(iConMgr, enabled)
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: SecurityException) {
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    }
}

@SuppressLint("MissingPermission")
        /**
 * 判断网络是否连接
 */
fun Any.isConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.state == NetworkInfo.State.CONNECTING) {
                return true
            }
        }
    }
    return false
}

@SuppressLint("MissingPermission")
        /**
 * 判断是否是WIFI连接
 */
fun isWIFI(context: Context?): Boolean {
    if (checkNetEnable(context)) {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null && ConnectivityManager.TYPE_WIFI != null) {
            return connectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
        }else{
            return false
        }
    }else{
        return false
    }
}

@SuppressLint("MissingPermission")
        /**
 * 检查网络是否可用
 *
 * @param paramContext
 * @return
 */
fun checkNetEnable(paramContext: Context?): Boolean {
    val localNetworkInfo = (paramContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
    return if (localNetworkInfo != null && localNetworkInfo.isAvailable) true else false
}

/**
 * 获取当前ip地址
 *
 * @param context
 * @return
 */
fun Any.getLocalIpAddress(context: Context): String {
    try {
        val wifiManager = context
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val i = wifiInfo.ipAddress
        return int2ip(i)
    } catch (ex: Exception) {

    }

    return "0.0.0.0"
}

/**
 * 将ip的整数形式转换成ip形式
 *
 * @param ipInt
 * @return
 */
fun Any.int2ip(ipInt: Int): String {
    val sb = StringBuilder()
    sb.append(ipInt and 0xFF).append(".")
    sb.append(ipInt shr 8 and 0xFF).append(".")
    sb.append(ipInt shr 16 and 0xFF).append(".")
    sb.append(ipInt shr 24 and 0xFF)
    return sb.toString()
}

/**
 * 获取WIFI IP地址
 *
 * @param context
 * @return
 */
fun Any.getWifiIP(context: Context): String {
    //获取wifi服务
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    //判断wifi是否开启
    if (!wifiManager.isWifiEnabled) {
        wifiManager.isWifiEnabled = true
    }
    val wifiInfo = wifiManager.connectionInfo
    val ipAddress = wifiInfo.ipAddress
    return intToIp(ipAddress)
}

/**
 * 获取GPRS IP
 *
 * @return
 */
fun Any.getLocalIpAddress(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    return inetAddress.hostAddress.toString()
                }
            }
        }
    } catch (ex: SocketException) {
    }

    return null
}

private fun intToIp(i: Int): String {

    return (i and 0xFF).toString() + "." +
            (i shr 8 and 0xFF) + "." +
            (i shr 16 and 0xFF) + "." +
            (i shr 24 and 0xFF)
}

/**
 *是否连接wifi
 */
fun Any.WifiState(context: Context): Boolean? {
    val currentapiVersion = Build.VERSION.SDK_INT
    if (currentapiVersion < 23) {
        return CheckWifiState23(context)
    } else {
        return CheckWifiState23New(context)
    }
}

//API版本23以下时调用此方法进行检测
//因为API23后getNetworkInfo(int networkType)方法被弃用
//检测当前的网络状态
fun CheckWifiState23(context: Context): Boolean {
    //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
    var connMgr: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
    //NetworkInfo对象包含网络连接的所有信息
    //步骤3：根据需要取出网络连接信息
    //获取WIFI连接的信息
    var networkInfo: NetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    var isWifiConn: Boolean = networkInfo.isConnected//wifi是否连接
    return isWifiConn
}

fun CheckWifiState23New(context: Context): Boolean? {
    //获得ConnectivityManager对象
    var connMgr: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //获取所有网络连接的信息
    var networks: Array<out Network>? = connMgr.allNetworks
    //用于存放网络连接信息
    var networkInfo: NetworkInfo? = null
    //通过循环将网络信息逐个取出来
    for (net in networks!!) {
        //获取ConnectivityManager对象对应的NetworkInfo对象
        networkInfo = connMgr.getNetworkInfo(net)
    }
    if ("WIFI".equals(networkInfo?.typeName)) {
        return networkInfo?.isConnected
    } else {
        return false
    }
}

fun CheckMobileConnState23(context: Context): Boolean? {
    //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
    var connMgr: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
    //NetworkInfo对象包含网络连接的所有信息
    //步骤3：根据需要取出网络连接信息
    //获取移动数据连接的信息
    var networkInfo: NetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
    var isMobileConn: Boolean = networkInfo.isConnected()//移动数据是否连接
    return isMobileConn
}
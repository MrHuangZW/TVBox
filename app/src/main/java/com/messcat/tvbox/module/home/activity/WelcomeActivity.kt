//package com.messcat.tvbox.module.home.activity
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.net.Uri
//import android.net.wifi.WifiManager
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.os.Message
//import android.provider.Settings
//import android.view.KeyEvent
//import android.view.View
//import com.google.gson.Gson
//import com.messcat.kotlin.mchttp.RetrofitServiceManager
//import com.messcat.kotlin.utils.*
//import com.messcat.mclibrary.BASE_URL
//import com.messcat.mclibrary.base.LANGUAGE_TYPE
//import com.messcat.mclibrary.base.MEMBER
//import com.messcat.mclibrary.base.MVPActivity
//import com.messcat.mclibrary.finishActivitys
//import com.messcat.mclibrary.goToActivity
//import com.messcat.mclibrary.util.*
//import com.messcat.tvbox.R
//import com.messcat.tvbox.module.MainActivity
//import com.messcat.tvbox.module.home.bean.*
//import com.messcat.tvbox.module.home.contract.WelcomeContract
//import com.messcat.tvbox.module.home.presenter.WelcomePresenter
//import com.messcat.tvbox.utils.closeWifiHotspot
//import com.messcat.tvbox.utils.loadImage
//import com.messcat.tvbox.utils.setWifiAppEnabload
//import kotlinx.android.synthetic.main.activity_welcome.*
//import java.util.concurrent.TimeUnit
//
//
///**
// * Created by Administrator on 2017/9/11 0011.
// */
//
//class WelcomeActivity : MVPActivity<WelcomePresenter>(), WelcomeContract.View {
//    override fun addLoginInfoResponse(t: AddLoginInfoBean?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun addLoginInfoAgainResponse(t: AddLoginInfoBean?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun updateLoginInfoAgain() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun updateLoginInfo() {
//    }
//
//    private var city: String? = null
//    private var temperature: String? = null
//    private var weather: String? = null
//    private var buffer = StringBuffer()
//    private var wifiManager: WifiManager? = null
//
//    override fun weatherError(error: String?) {
//    }
//
//    override fun getWeather(weatherBean: WeatherBean?) {
//        saveWeather(Gson().toJson(weatherBean))
//        city = weatherBean?.result?.today?.city
//        temperature = weatherBean?.result?.today?.temperature
//        weather = weatherBean?.result?.today?.weather
//        tv_box_address_weather.text = weatherBean?.result?.today?.weather
//        tv_box_address_dy.text = weatherBean?.result?.today?.temperature
//
//        if (isContains(weatherBean?.result?.today?.weather!!, "多云")) {
//            tv_box_weather.setImageResource(R.mipmap.weather_cloudy)
//        } else if (isContains(weatherBean?.result?.today?.weather!!, "雨")) {//判断字符串中是否有那个中文
//            tv_box_weather.setImageResource(R.mipmap.weather_rain)
//        } else {
//            tv_box_weather.setImageResource(R.mipmap.weather_sun)
//        }
//    }
//
//    override fun getWelcomeData(welcomeBean: WelcomeBean?, type: String?,id:Long?) {
//        saveWelcome(Gson().toJson(welcomeBean))
//        if (welcomeBean?.result?.welcomeInfo?.fontColor != null) {
//            tv_box_welcome.setTextColor(Color.parseColor(welcomeBean?.result?.welcomeInfo?.fontColor))
//            tv_box_name.setTextColor(Color.parseColor(welcomeBean?.result?.welcomeInfo?.fontColor))
//        }
//        tv_box_welcome.text = welcomeBean?.result?.welcomeInfo?.title
//        tv_box_name.text = welcomeBean?.result?.welcomeInfo?.content
//        tv_box_wifi_name.text = welcomeBean?.result?.wifi
//        tv_box_wifi_password.text = welcomeBean?.result?.password
//        tv_box_content.text = welcomeBean?.result?.welcomeInfo?.content1
//        loadImage(mContext, BASE_URL + welcomeBean?.result?.welcomeInfo?.picture, tv_box_bg)
//    }
//
//    override fun showError(msg: String?) {
//        getWelcomeData()
//    }
//
//    override fun showLoadingDialog() {
//
//    }
//
//    override fun dismissLoadingDialog() {
//
//    }
//
//    override fun initPresenter(): WelcomePresenter = WelcomePresenter(this, WelcomePresenter.WelcomeLoader(
//            RetrofitServiceManager.instance.create(WelcomePresenter.WelcomeHttp::class.java)))
//
//    override fun getLayout(): Int = R.layout.activity_welcome
//
//    override fun initData() {
//        if (checkNetEnable(mContext)) {
//            checkIntent.visibility = View.GONE
//            var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
//            if (messageBean != null) {
//                var type = "0"
//                if (LANGUAGE_TYPE.equals("chinese")) {
//                    type = "1"
//                } else {
//                    type = "0"
//                }
////                mPresenter?.getWelcomeInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, type,loginInfoId)
//                mPresenter?.getWifiState(messageBean?.result?.diCode, messageBean?.result?.roomNum)
//            } else {
//                goToActivity(mActivity, MessageActivity::class.java)
//            }
//        } else {
//            checkIntent.visibility = View.VISIBLE
//            getWelcomeData()
//        }
//    }
//
//    private fun getWelcomeData() {
//        if (getWelcome() != null) {
//            var mWelcomeBean = Gson().fromJson(getWelcome(), WelcomeBean::class.java)
//            if (mWelcomeBean != null) {
//                if (mWelcomeBean?.result?.welcomeInfo?.fontColor != null) {
//                    tv_box_welcome.setTextColor(Color.parseColor(mWelcomeBean?.result?.welcomeInfo?.fontColor))
//                    tv_box_name.setTextColor(Color.parseColor(mWelcomeBean?.result?.welcomeInfo?.fontColor))
//                }
//                tv_box_welcome.text = mWelcomeBean?.result?.welcomeInfo?.title
//                tv_box_name.text = mWelcomeBean?.result?.welcomeInfo?.content
//                tv_box_wifi_name.text = mWelcomeBean?.result?.wifi
//                tv_box_wifi_password.text = mWelcomeBean?.result?.password
//                tv_box_content.text = mWelcomeBean?.result?.welcomeInfo?.content1
////                loadImage(mContext, BASE_URL + mWelcomeBean?.result?.welcomeInfo?.picture, tv_box_bg)
//            }
//        }
//    }
//
//    override fun initEvent() {
//        tv_box_help.setOnFocusChangeListener { view, b ->
//            if (b) {
//                tv_box_help.setBackgroundResource(R.mipmap.welcome_help_bg)
//            } else {
//                tv_box_help.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
//            }
//        }
//        tv_box_menu.setOnFocusChangeListener { view, b ->
//            if (b) {
//                tv_box_menu.setBackgroundResource(R.mipmap.welcome_menu_bg)
//            } else {
//                tv_box_menu.setBackgroundResource(R.mipmap.welcome_menu_bg_hover)
//            }
//        }
//        tv_box_help.setOnClickListener {
//            goToActivity(mActivity!!, HelpActivity::class.java)
//        }
//        tv_box_menu.setOnClickListener {
//            var bundle = Bundle()
//            bundle.putString("city", city)
//            bundle.putString("temperature", temperature)
//            bundle.putString("weather", weather)
//            goToActivity(mActivity!!, MainActivity::class.java, bundle)
//        }
//    }
//
//    @SuppressLint("WifiManagerLeak")
//    override fun initView() {
//        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
//        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
//    }
//
//    private var runnable = Runnable {
//        val msg = Message()
//        msg.what = msgKey1
//        mHandler.sendMessage(msg)
//    }
//
//    private val mHandler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            when (msg.what) {
//                msgKey1 -> {
//                    tv_box_year.text = getYear()
//                    tv_box_time.text = getTime()
//                }
//                else -> {
//                }
//            }
//        }
//    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        when (keyCode) {
//            KeyEvent.KEYCODE_DPAD_DOWN -> {
//                buffer.append("下")
//            }
//            KeyEvent.KEYCODE_DPAD_UP -> {
//                buffer.append("上")
//            }
//            KeyEvent.KEYCODE_DPAD_LEFT -> {
//                buffer.append("左")
//                if ("上上下下左右左右左".equals(buffer.toString())) {
//                    buffer.delete(0, buffer.length)
//                    clearSP(MEMBER)
//                    goToActivity(mActivity, MessageActivity::class.java)
//                    finishActivitys(mActivity)
//                }
//            }
//            KeyEvent.KEYCODE_DPAD_RIGHT -> {
//                buffer.append("右")
//            }
//            else -> {
//                if (buffer != null && buffer.length > 0) {
//                    buffer.delete(0, buffer.length)
//                }
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (buffer != null && buffer.length > 0) {
//            buffer.delete(0, buffer.length)
//        }
//    }
//
//    override fun wifiState(wifiBean: WifiBean?) {
//        if (wifiBean?.result?.wifiStatus!!) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!Settings.System.canWrite(this)) {
//                    var intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                            Uri.parse("package:" + getPackageName()))
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivityForResult(intent, 10010)
//                } else {
//                    setWifiAppEnabload(wifiManager, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword,mActivity)
//                }
//            } else {
//                setWifiAppEnabload(wifiManager, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword,mActivity)
//            }
//        } else {
//            closeWifiHotspot(wifiManager, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (scheduledThreadPool != null) {
//            scheduledThreadPool.shutdown()
//        }
//    }
//}

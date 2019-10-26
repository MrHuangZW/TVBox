package com.messcat.tvbox.module.home.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.messcat.kotlin.mchttp.RetrofitServiceManager
import com.messcat.kotlin.utils.getTime
import com.messcat.kotlin.utils.getYear
import com.messcat.kotlin.utils.long_toast
import com.messcat.kotlin.utils.w
import com.messcat.mclibrary.BASE_URL
import com.messcat.mclibrary.base.LANGUAGE_TYPE
import com.messcat.mclibrary.base.MEMBER
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.mclibrary.goToActivity
import com.messcat.mclibrary.util.*
import com.messcat.tvbox.R
import com.messcat.tvbox.module.MainActivity
import com.messcat.tvbox.module.home.activity.MessageActivity
import com.messcat.tvbox.module.home.bean.*
import com.messcat.tvbox.module.home.contract.WelcomeContract
import com.messcat.tvbox.module.home.presenter.WelcomePresenter
import com.messcat.tvbox.utils.closeWifiHotspot
import com.messcat.tvbox.utils.loadImage
import com.messcat.tvbox.utils.setWifiAppEnabload
import kotlinx.android.synthetic.main.activity_new_welcome.*
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/9/11 0011.
 */

class NewWelcomeActivity : MVPActivity<WelcomePresenter>(), WelcomeContract.View, View.OnFocusChangeListener, View.OnClickListener {


    var loginInfoIdAgain: Long = 0
    var loginInfoId: Long = 0
    var recLen2: Int = 0
    var recLen3: Int = 0
    var wB: WelcomeBean? = null
    var nightTag: Int = 0//0表示12点的上传日志标记未开启 1表示开启

    override fun addLoginInfoAgainResponse(t: AddLoginInfoBean?) {
        loginInfoIdAgain = t?.result?.loginInfoId!!
        if (loginInfoIdAgain != 0L) {
            recLen3 = 0
            handler3.postDelayed(runnable3, 1000)
        }
    }

    override fun addLoginInfoResponse(t: AddLoginInfoBean?) {
        loginInfoId = t?.result?.loginInfoId!!
        if (loginInfoId != 0L) {
            recLen2 = 0
            handler2.postDelayed(runnable2, 1000)
        }
    }

    override fun updateLoginInfo() {
        handler2.removeCallbacksAndMessages(runnable2)
    }

    override fun updateLoginInfoAgain() {
        if (nightTag == 1) {
            handler3.removeCallbacksAndMessages(runnable3)
            nightTag = 0
        }
    }

    override fun onClick(v: View?) {
        var bundle = Bundle()
        bundle.putString("city", city)
        bundle.putString("temperature", temperature)
        bundle.putString("weather", weather)
        goToActivity(mActivity!!, MainActivity::class.java, bundle)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0?.id) {
            R.id.tv_english -> {
                if (p1) {
                    LANGUAGE_TYPE = "english"
                    tv_english.scaleX = 1.4f
                    tv_english.scaleY = 1.4f
                    tv_english.setBackgroundResource(R.drawable.text_little_gold_radius)
                    tv_english.setTextColor(resources.getColor(R.color.color_white))
                    var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
                    if (messageBean != null) {
                        if (checkNetEnable(mContext)) {
                            var type = "0"
                            if ("english".equals(LANGUAGE_TYPE)) {
                                type = "0"
                            } else if ("chinese".equals(LANGUAGE_TYPE)) {
                                type = "1"
                            }
                            tv_respect.text = "Dear Customer:"
                            mPresenter?.getWelcomeInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, type, loginInfoId)
                        } else {
                            getWelcomeData()
                        }
                    } else {
                        goToActivity(mActivity, MessageActivity::class.java)
                    }
                } else {
                    tv_english.scaleX = 1f
                    tv_english.scaleY = 1f
                    LANGUAGE_TYPE = "chinese"
                    tv_english.setBackgroundResource(R.drawable.text_little_radius)
                    tv_english.setTextColor(resources.getColor(R.color.colorc4a992))
                }
            }
            R.id.tv_chinese -> {
                if (p1) {
                    LANGUAGE_TYPE = "chinese"
                    tv_chinese.scaleX = 1.4f
                    tv_chinese.scaleY = 1.4f
                    tv_chinese.setBackgroundResource(R.drawable.text_little_gold_radius)
                    tv_chinese.setTextColor(resources.getColor(R.color.color_white))
                    var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
                    if (messageBean != null) {
                        if (checkNetEnable(mContext)) {
                            var type = "0"
                            if ("english".equals(LANGUAGE_TYPE)) {
                                type = "0"
                            } else if ("chinese".equals(LANGUAGE_TYPE)) {
                                type = "1"
                            }
                            tv_respect.text = "尊敬的客户:"
                            mPresenter?.getWelcomeInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, type, loginInfoId)
                        } else {
                            getWelcomeData()
                        }
                    } else {
                        goToActivity(mActivity, MessageActivity::class.java)
                    }
                } else {
                    tv_chinese.scaleX = 1f
                    tv_chinese.scaleY = 1f
                    LANGUAGE_TYPE = "english"
                    tv_chinese.setBackgroundResource(R.drawable.text_little_radius)
                    tv_chinese.setTextColor(resources.getColor(R.color.colorc4a992))
                }
            }
            else -> {

            }

        }
    }

    private var city: String? = null
    private var temperature: String? = null
    private var weather: String? = null
    private var buffer = StringBuffer()
    private var wifiManager: WifiManager? = null
    override fun getLayout(): Int = R.layout.activity_new_welcome
    override fun weatherError(error: String?) {
    }

    override fun getWeather(weatherBean: WeatherBean?) {
        saveWeather(Gson().toJson(weatherBean))
        city = weatherBean?.result?.today?.city
        temperature = weatherBean?.result?.today?.temperature
        weather = weatherBean?.result?.today?.weather
//        tv_box_address_weather.text = weatherBean?.result?.today?.weather
//        tv_box_address_dy.text = weatherBean?.result?.today?.temperature
//
//        if (isContains(weatherBean?.result?.today?.weather!!, "多云")) {
//            iv_box_weather.setImageResource(R.mipmap.weather_cloudy_white)
//        } else if (isContains(weatherBean?.result?.today?.weather!!, "雨")) {//判断字符串中是否有那个中文
//            iv_box_weather.setImageResource(R.mipmap.weather_rain)
//        } else {
//            iv_box_weather.setImageResource(R.mipmap.weather_sun_white)
//        }
    }

    override fun getWelcomeData(welcomeBean: WelcomeBean?, type: String?, returnId: Long?) {
        wB = welcomeBean
        if (type.equals("0")) {
            saveEnglishWelcome(Gson().toJson(welcomeBean))
        } else {
            saveWelcome(Gson().toJson(welcomeBean))
        }
        if (welcomeBean?.result?.welcomeInfo?.fontColor != null) {
            tv_box_name.setTextColor(Color.parseColor(welcomeBean?.result?.welcomeInfo?.fontColor))
            tv_box_ename.setTextColor(Color.parseColor(welcomeBean?.result?.welcomeInfo?.fontColor))
        }
        tv_box_name.text = welcomeBean?.result?.welcomeInfo?.title
        tv_box_ename.text = welcomeBean?.result?.welcomeInfo?.content
//        if (LANGUAGE_TYPE.equals("english")) {
//            tv_wifi.text = "SSID:" + welcomeBean?.result?.wifi + "     PASSWORD:" + welcomeBean?.result?.password
//            tv_control.text = "Please press the OK key to enter the menu"
//        } else {
//            tv_control.text = "请按【OK】键进入菜单,按遥控器【上下左右】键选择项目"
//            tv_wifi.text = "WIFI名称:" + welcomeBean?.result?.wifi + "     WIFI密码:" + welcomeBean?.result?.password
//        }

        if (LANGUAGE_TYPE.equals("english")) {
            tv_box_address_dy.text = "WiFiSSID:" + welcomeBean?.result?.wifi
            tv_box_address_weather.text = "PASSWORD:" + welcomeBean?.result?.password
            tv_control.text = "Using the remote control (OK) button to enter the menu."
        } else {
            tv_box_address_dy.text = "WiFi名称:" + welcomeBean?.result?.wifi
            tv_box_address_weather.text = "WiFi密码:" + welcomeBean?.result?.password
            tv_control.text = "请按【OK】键进入菜单,按遥控器【上下左右】键选择项目"
        }
//        tv_box_wifi_name.text = welcomeBean?.result?.wifi
//        tv_box_wifi_password.text = welcomeBean?.result?.password
        tv_box_content.loadData(welcomeBean?.result?.welcomeInfo?.content1, "text/html; charset=UTF-8", null)
        tv_box_content.setBackgroundColor(0)
        loadImage(mContext, BASE_URL + welcomeBean?.result?.welcomeInfo?.picture, tv_box_bg)
//        loadImage(mContext, BASE_URL + welcomeBean?.result?.welcomeInfo?.hotelInfo?.qrCode, iv_logo)
        Glide.with(mContext).load(BASE_URL + welcomeBean?.result?.welcomeInfo?.hotelInfo?.qrCode).into(iv_logo)
        if ("0".equals(welcomeBean?.result?.welcomeInfo?.hotelInfo?.status)) {
            tv_text.visibility = View.VISIBLE
            tv_text2.visibility = View.VISIBLE
            rl_text_background.visibility = View.VISIBLE
            tv_chinese.isEnabled = false
            tv_english.isEnabled = false
        } else {
            tv_text.visibility = View.GONE
            tv_text2.visibility = View.GONE
            rl_text_background.visibility = View.GONE
            tv_chinese.isEnabled = true
            tv_english.isEnabled = true
        }
//        object : Thread() {
//            override fun run() {
//                super.run()
//                if (loginInfoId > 0) {
//                    var recLen = 0
//                    val timer = object : CountDownTimer(60 * 1000 * 30, 1000) {
//                        override fun onTick(millisUntilFinished: Long) {
//                            recLen++
//                        }
//
//                        override fun onFinish() {
//                            mPresenter!!.updateLoginInfo(welcomeBean?.result?.loginInfoId)
//                        }
//                    }.start()
//                }
//            }
//        }.start()
        if (loginInfoId != 0L) {
            recLen2 = 0
            handler2.postDelayed(runnable2, 1000)
        }

    }


//            Class<?> providerClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
//            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
//            Constructor<?> providerConstructor = providerClass.getConstructor(delegateClass);
//            if (providerConstructor != null) {
//                providerConstructor.setAccessible(true);
//                Constructor<?> declaredConstructor = delegateClass.getDeclaredConstructor();
//                declaredConstructor.setAccessible(true);
//                sProviderInstance = providerConstructor.newInstance(declaredConstructor.newInstance());
//                log.debug("sProviderInstance:{}", sProviderInstance);
//                field.set("sProviderInstance", sProviderInstance);
//            }
//            log.debug("Hook done!");
//        } catch (Throwable e) {
//            log.error(e);
//        }
//    }

    internal var handler2 = Handler()

    internal var runnable2: Runnable = object : Runnable {
        override fun run() {
            recLen2++
            if (recLen2 == 30 * 60) {
                mPresenter!!.updateLoginInfo(loginInfoId)
            }
            handler2.postDelayed(this, 1000)
        }
    }

    internal var handler3 = Handler()

    internal var runnable3: Runnable = object : Runnable {
        override fun run() {
            recLen3++
            if (recLen3 == 30 * 60) {
                mPresenter!!.updateLoginInfo(loginInfoIdAgain)
            }
            handler3.postDelayed(this, 1000)
        }
    }

    override fun showError(msg: String?) {
        if ("酒店房间已停用！".equals(msg)) {
            tv_text.visibility = View.VISIBLE
            tv_text2.visibility = View.VISIBLE
            rl_text_background.visibility = View.VISIBLE
            tv_chinese.isEnabled = false
            tv_english.isEnabled = false
        } else {
            tv_text.visibility = View.GONE
            tv_text2.visibility = View.GONE
            rl_text_background.visibility = View.GONE
            tv_chinese.isEnabled = true
            tv_english.isEnabled = true
        }
        getWelcomeData()
    }

    override fun wifiStateError() {
        setWifiStatusNoNet()
    }

    override fun showLoadingDialog() {

    }

    override fun dismissLoadingDialog() {

    }

    override fun initPresenter(): WelcomePresenter = WelcomePresenter(this, WelcomePresenter.WelcomeLoader(
            RetrofitServiceManager.instance.create(WelcomePresenter.WelcomeHttp::class.java)))


    override fun initData() {
        loginInfoId = 0
        loginInfoIdAgain = 0
        if (checkNetEnable(mContext)) {
//            checkIntent.visibility = View.GONE
            var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
            if (messageBean != null) {
//                mPresenter?.getWelcomeInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, "1",loginInfoId)
//                mPresenter?.getWelcomeInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, "0",loginInfoId)
                mPresenter?.getWifiState(messageBean?.result?.diCode, messageBean?.result?.roomNum)
                mPresenter?.addLoginInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum)
            } else {
                goToActivity(mActivity, MessageActivity::class.java)
            }
        } else {
            long_toast("请检查网络连接是否正常", mActivity)
//            checkIntent.visibility = View.VISIBLE
            getWelcomeData()
            setWifiStatusNoNet()
        }
    }

    private fun getWelcomeData() {
        if (LANGUAGE_TYPE.equals("english")) {
            if (getEnglishWelcome() != null) {
                var mWelcomeBean = Gson().fromJson(getEnglishWelcome(), WelcomeBean::class.java)
                if (mWelcomeBean != null) {
                    if (mWelcomeBean?.result?.welcomeInfo?.fontColor != null) {
                        tv_box_name.setTextColor(Color.parseColor(mWelcomeBean?.result?.welcomeInfo?.fontColor))
                        tv_box_ename.setTextColor(Color.parseColor(mWelcomeBean?.result?.welcomeInfo?.fontColor))
                    }
                    tv_box_name.text = mWelcomeBean?.result?.welcomeInfo?.title
                    tv_box_ename.text = mWelcomeBean?.result?.welcomeInfo?.content
//                tv_box_wifi_name.text = mWelcomeBean?.result?.wifi
//                tv_box_wifi_password.text = mWelcomeBean?.result?.password
                    tv_box_address_dy.text = "WiFiSSID:" + mWelcomeBean?.result?.wifi
                    tv_box_address_weather.text = "PASSWORD:" + mWelcomeBean?.result?.password
                    tv_control.text = "Using the remote control (OK) button to enter the menu."
                    tv_box_content.loadData(mWelcomeBean?.result?.welcomeInfo?.content1, "text/html; charset=UTF-8", null)
                    tv_box_content.setBackgroundColor(0)
                    loadImage(mContext, BASE_URL + mWelcomeBean?.result?.welcomeInfo?.picture, tv_box_bg)
//                    loadImage(mContext, BASE_URL + mWelcomeBean?.result?.welcomeInfo?.hotelInfo?.qrCode, iv_logo)
                    Glide.with(mContext).load(BASE_URL + mWelcomeBean?.result?.welcomeInfo?.hotelInfo?.qrCode).into(iv_logo)
//                    if ("0".equals(mWelcomeBean?.result?.welcomeInfo?.hotelInfo?.status)) {
//                        tv_text.visibility = View.VISIBLE
//                        tv_text2.visibility = View.VISIBLE
//                        rl_text_background.visibility = View.VISIBLE
//                        tv_chinese.isEnabled = false
//                        tv_english.isEnabled = false
//                    } else {
//                        tv_text.visibility = View.GONE
//                        tv_text2.visibility = View.GONE
//                        rl_text_background.visibility = View.GONE
//                        tv_chinese.isEnabled = true
//                        tv_english.isEnabled = true
//                    }
                }
            }
        } else {
            if (getWelcome() != null) {
                var mWelcomeBean = Gson().fromJson(getWelcome(), WelcomeBean::class.java)
                if (mWelcomeBean != null) {
                    if (mWelcomeBean?.result?.welcomeInfo?.fontColor != null) {
                        tv_box_name.setTextColor(Color.parseColor(mWelcomeBean?.result?.welcomeInfo?.fontColor))
                        tv_box_ename.setTextColor(Color.parseColor(mWelcomeBean?.result?.welcomeInfo?.fontColor))
                    }
                    tv_box_name.text = mWelcomeBean?.result?.welcomeInfo?.title
                    tv_box_ename.text = mWelcomeBean?.result?.welcomeInfo?.content
//                tv_box_wifi_name.text = mWelcomeBean?.result?.wifi
//                tv_box_wifi_password.text = mWelcomeBean?.result?.password
                    tv_box_address_dy.text = "WiFi名称:" + mWelcomeBean?.result?.wifi
                    tv_box_address_weather.text = "WiFi密码:" + mWelcomeBean?.result?.password
                    tv_control.text = "请按【OK】键进入菜单,按遥控器【上下左右】键选择项目"
                    tv_box_content.loadData(mWelcomeBean?.result?.welcomeInfo?.content1, "text/html; charset=UTF-8", null)
                    tv_box_content.setBackgroundColor(0)
                    loadImage(mContext, BASE_URL + mWelcomeBean?.result?.welcomeInfo?.picture, tv_box_bg)
//                    loadImage(mContext, BASE_URL + mWelcomeBean?.result?.welcomeInfo?.hotelInfo?.qrCode, iv_logo)
                    Glide.with(mContext).load(BASE_URL + mWelcomeBean?.result?.welcomeInfo?.hotelInfo?.qrCode).into(iv_logo)
//                    if ("0".equals(mWelcomeBean?.result?.welcomeInfo?.hotelInfo?.status)) {
//                        tv_text.visibility = View.VISIBLE
//                        tv_text2.visibility = View.VISIBLE
//                        rl_text_background.visibility = View.VISIBLE
//                        tv_chinese.isEnabled = false
//                        tv_english.isEnabled = false
//                    } else {
//                        tv_text.visibility = View.GONE
//                        tv_text2.visibility = View.GONE
//                        rl_text_background.visibility = View.GONE
//                        tv_chinese.isEnabled = true
//                        tv_english.isEnabled = true
//                    }
                }
            }
        }
    }

    override fun initEvent() {
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
        tv_chinese.setOnClickListener {
            LANGUAGE_TYPE = "chinese"
            var bundle = Bundle()
            bundle.putString("city", city)
            bundle.putString("temperature", temperature)
            bundle.putString("weather", weather)
            goToActivity(mActivity!!, MainActivity::class.java, bundle)
        }
        tv_english.setOnClickListener {
            LANGUAGE_TYPE = "english"
            var bundle = Bundle()
            bundle.putString("city", city)
            bundle.putString("temperature", temperature)
            bundle.putString("weather", weather)
            goToActivity(mActivity!!, MainActivity::class.java, bundle)
        }
    }

    @SuppressLint("WifiManagerLeak")
    override fun initView() {
        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        tv_chinese.setOnFocusChangeListener(this)
        tv_chinese.setOnClickListener(this)
        tv_english.setOnFocusChangeListener(this)
        tv_english.setOnClickListener(this)
        tv_box_content.isFocusable = false
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        mActivity!!.registerReceiver(mIntentReceiver, filter, null, null)
    }

    private var runnable = Runnable {
        val msg = Message()
        msg.what = msgKey1
        mHandler.sendMessage(msg)
    }

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                msgKey1 -> {
                    tv_box_year.text = getYear()
                    tv_box_time.text = getTime()
                }
                else -> {
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                buffer.append("下")
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                buffer.append("上")
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                buffer.append("左")
                if ("上上下下左右左右左".equals(buffer.toString())) {
                    buffer.delete(0, buffer.length)
                    clearSP(MEMBER)
                    goToActivity(mActivity, MessageActivity::class.java)
                    finishActivitys(mActivity)
                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                buffer.append("右")
            }
            else -> {
                if (buffer != null && buffer.length > 0) {
                    buffer.delete(0, buffer.length)
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("MissingSuperCall")
    override fun onPause() {
        super.onPause()
        if (buffer != null && buffer.length > 0) {
            buffer.delete(0, buffer.length)
        }
    }

    override fun wifiState(wifiBean: WifiBean?) {
        saveWifiBean(Gson().toJson(wifiBean))
       setWifiStatus(wifiBean)
    }

    private fun setWifiStatusNoNet(){
        if(!TextUtils.isEmpty(getWifiBean())){
            var wifiBean  = Gson().fromJson(getWifiBean(), WifiBean::class.java)
            setWifiStatus(wifiBean)
        }


    }

    private fun setWifiStatus(wifiBean: WifiBean?){
        if (wifiBean?.result?.wifiStatus!!) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("lyhlog","00000000")
                if (!Settings.System.canWrite(this)) {
                    var intent = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + getPackageName()))
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
////                        closeWifiHotspot(wifiManager,wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword)
//                        ThirdActivity.openWifi(this, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword)
//                    } else {
                        setWifiAppEnabload(wifiManager, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword, mActivity)
//                    }
                }
            } else {
                setWifiAppEnabload(wifiManager, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword, mActivity)
            }
        } else {
            closeWifiHotspot(wifiManager, wifiBean?.result?.wifiName, wifiBean?.result?.wifiPassword)
        }
    }

    private val mIntentReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_TIME_TICK || action == Intent.ACTION_TIMEZONE_CHANGED) {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                val second = calendar.get(Calendar.SECOND)
                if (hour == 0) {
                    if (minute == 0) {
                        if (second == 0) {
                            nightTag = 1
                            recLen3 = 0
                            var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
                            if (messageBean != null) {
                                mPresenter?.addLoginInfoAgain(messageBean?.result?.diCode, messageBean?.result?.roomNum)
                            }
                        }
                    }
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown()
        }
    }

}

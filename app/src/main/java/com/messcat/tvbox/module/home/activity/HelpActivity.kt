package com.messcat.tvbox.module.home.activity

import android.os.Handler
import android.os.Message
import android.view.View
import com.google.gson.Gson
import com.messcat.kotlin.utils.getTime
import com.messcat.kotlin.utils.getYear
import com.messcat.kotlin.utils.isContains
import com.messcat.mclibrary.base.LANGUAGE_TYPE
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.mclibrary.util.checkNetEnable
import com.messcat.mclibrary.util.getWeather
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.WeatherBean
import com.messcat.tvbox.module.home.contract.HelpContract
import com.messcat.tvbox.module.home.presenter.HelpPresenter
import kotlinx.android.synthetic.main.activity_help.*
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/8/24 0024.
 */
class HelpActivity : MVPActivity<HelpPresenter>(), HelpContract.View, View.OnFocusChangeListener {
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            tv_box_back.scaleX = 1.2f
            tv_box_back.scaleY = 1.2f
            tv_box_back.setBackgroundResource(R.mipmap.welcome_help_bg)
        } else {
            tv_box_back.scaleX = 1f
            tv_box_back.scaleY = 1f
            tv_box_back.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
        }
    }

    private lateinit var weather: WeatherBean
    override fun showLoadingDialog() {
    }

    override fun dismissLoadingDialog() {
    }

    override fun showError(msg: String?) {
    }

    override fun getLayout(): Int = R.layout.activity_help

    override fun initPresenter(): HelpPresenter = HelpPresenter()

    override fun initView() {
        if (checkNetEnable(mContext)) {
            if (intent.extras != null) {
                if (intent.extras.getString("city") != null) {
                    tv_box_address.text = intent.extras.getString("city")
                }
                if (intent.extras.getString("temperature") != null) {
                    tv_box_t.text = intent.extras.getString("temperature")
                }
                if (intent.extras.getString("weather") != null) {
                    tv_box_weather.text = intent.extras.getString("weather")
                    if (intent.extras.getString("weather") != null) {
                        if (isContains(intent.extras.getString("weather")!!, "多云")) {
                            tv_box_image_sun.setImageResource(R.mipmap.weather_cloudy)
                        } else if (isContains(intent.extras.getString("weather")!!, "雨")) {//判断字符串中是否有那个中文
                            tv_box_image_sun.setImageResource(R.mipmap.weather_rain)
                        } else {
                            tv_box_image_sun.setImageResource(R.mipmap.weather_sun)
                        }
                    }
                }
                if (LANGUAGE_TYPE.equals("chinese")) {
                    tv_wifi_name.text = "WiFi名称:"
                    tv_wifi_password.text = "WiFi密码:"
                    tv_message0.text = "遥控器使用说明"
                    tv_message1.text = "1、使用遥控器前请先检查是否损坏，按下任意按钮【 指 示 灯 】是否亮起，如不能正常亮起或外观损坏请联系服务员及时更换。"
                    tv_message2.text = "2、红色【 电 源 】键为红外开关机按钮,操作时需对准电视机"
                    tv_message3.text = "3、使用遥控器[上 下 左 右 ]键选择对应项目"
                    tv_message4.text = "4、使用遥控器【  O K 】键进入栏目，观看电视、电影过程中按下【 O K 】键弹出节目菜单"
                    tv_message5.text = "5、使用【返 回 键 】退出当前栏目，部分栏目需要按下两次【返 回 键 】退出"
                    tv_message6.text = "6、使用【 主 页 】键返回到系统主页面"
                    tv_message7.text = "7、使用【 音 量 + / 音 量 - 】键调节音量"
                } else {
                    tv_wifi_name.text = "WiFiSSID:"
                    tv_wifi_password.text = "PASSWORD:"
                    tv_message0.text = "Remote Control User Guide"
                    tv_message1.text = "1 Please check before using the remote control. Press any button to see if the indicator light is bright, if it is not, or the exterior is damaged, please contact us for replacement."
                    tv_message2.text = "2 The red (Power) button is infrared on/off button and should be aligned with the TV when operating."
                    tv_message3.text = "3 Using the remote control (Up, Down, Right, Down) button to select the corresponding item."
                    tv_message4.text = "4 Using the remote control (OK) button to enter the column. During the TV and movie, press the (OK) button to pop up the menu."
                    tv_message5.text = "5 Using the (Back) button to exit current section. Some sections need to press (Back) button twice to exit."
                    tv_message6.text = "6 Using the (Home) button to return to the main page."
                    tv_message7.text = "7 Using the ( VOL+, VOL-) button to raise or lower the volume."
                }
                if (intent.extras.getString("wifiName") != null) {
                    tv_box_wifi_name.text = intent.extras.getString("wifiName")
                }
                if (intent.extras.getString("wifiPassword") != null) {
                    tv_box_wifi_password.text = intent.extras.getString("wifiPassword")
                }
            }
        } else {
            weather = Gson().fromJson(getWeather(), WeatherBean::class.java)
            if (weather.result?.today?.city != null) {
                tv_box_address.text = weather.result?.today?.city
            }
            if (weather.result?.today?.temperature != null) {
                tv_box_t.text = weather.result?.today?.temperature
            }
            if (weather.result?.today?.weather != null) {
                tv_box_weather.text = weather.result?.today?.weather
                if (isContains(weather.result?.today?.weather!!, "多云")) {
                    tv_box_image_sun.setImageResource(R.mipmap.weather_cloudy)
                } else if (isContains(weather.result?.today?.weather!!, "雨")) {//判断字符串中是否有那个中文
                    tv_box_image_sun.setImageResource(R.mipmap.weather_rain)
                } else {
                    tv_box_image_sun.setImageResource(R.mipmap.weather_sun)
                }
            }
        }
        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
        tv_box_back.setOnFocusChangeListener(this)
    }

    override fun initEvent() {
        tv_box_back.setOnFocusChangeListener(this)
        tv_box_back.setOnClickListener {
            finishActivitys(mActivity)
        }
    }

    override fun initData() {
    }

    private var runnable: Runnable = Runnable {
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

    override fun onDestroy() {
        super.onDestroy()
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown()
        }
    }
}
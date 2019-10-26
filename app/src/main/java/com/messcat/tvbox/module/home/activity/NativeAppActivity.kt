package com.messcat.tvbox.module.home.activity

import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.messcat.kotlin.mchttp.RetrofitServiceManager
import com.messcat.kotlin.utils.*
import com.messcat.mclibrary.BASE_URL
import com.messcat.mclibrary.SpacesItemDecoration
import com.messcat.mclibrary.base.LANGUAGE_TYPE
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.mclibrary.goToActivity
import com.messcat.mclibrary.util.*
import com.messcat.tvbox.R
import com.messcat.tvbox.TvBoxApplication
import com.messcat.tvbox.module.home.adapter.*
import com.messcat.tvbox.module.home.bean.*
import com.messcat.tvbox.module.home.contract.NativeAppContract
import com.messcat.tvbox.module.home.presenter.NativeAppPresenter
import com.messcat.tvbox.utils.isInstallApp
import com.messcat.tvbox.utils.loadImage
import com.messcat.tvbox.utils.openOthemApp
import kotlinx.android.synthetic.main.activity_native_app.*
import kotlinx.android.synthetic.main.activity_native_app.tv_box_time
import kotlinx.android.synthetic.main.activity_native_app.tv_box_year
import kotlinx.android.synthetic.main.activity_new_welcome.*
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/8/23 0023.
 */
class NativeAppActivity : MVPActivity<NativeAppPresenter>(), NativeAppContract.View {

    private var mId: String? = null
    private var nativeAppAdapter: NativeAppAdapter? = null
    private var nativeAppsAdapter: NativeAppsAdapter? = null
    private var isFirst = true
    private var messageBean: MessageBean? = null


    override fun getAppInfo(applicationNewBean: ApplicationNewBean?) {
        saveNativeApp(Gson().toJson(applicationNewBean))
        if (isFirst) {
            nativeAppAdapter?.refreshList(applicationNewBean?.result?.category)
        }
        if (applicationNewBean?.result?.details != null && applicationNewBean?.result?.details?.size!! > 0) {
            nativeAppsAdapter?.refreshList(applicationNewBean?.result?.details)
            val array: List<String>? = applicationNewBean?.result?.category?.get(0)?.codengh?.split(",")
            nativeAppsAdapter?.setArray(array)
        }
        tv_box_wifi_name.text = applicationNewBean?.result?.wifi
        tv_box_wifi_password.text = applicationNewBean?.result?.password
        if (mId != null) {
            for ((index, value) in applicationNewBean?.result?.category?.withIndex()!!) {
                if (value != null) {
                    if (mId?.equals(value.id.toString())!!) {
                        nativeAppAdapter?.initSelect(index)
                    }
                }
            }
        } else {
            nativeAppAdapter?.initSelect(0)
        }
//        if (loadingDialog != null) {
//            loadingDialog.dismissDialog()
//        }
    }

    override fun showLoadingDialog() {
    }

    override fun dismissLoadingDialog() {
    }


    override fun showError(msg: String?) {
//        if (loadingDialog != null) {
//            loadingDialog.dismissDialog()
//        }
        setNativeData()
    }

    override fun getLayout(): Int = R.layout.activity_native_app

    override fun initPresenter(): NativeAppPresenter = NativeAppPresenter(this,
            NativeAppPresenter.NativeAppLoader(RetrofitServiceManager.instance.create(NativeAppPresenter.NativeAppHttp::class.java)))

    override fun initView() {
        messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
        tv_box_recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        tv_box_shopping.layoutManager = GridLayoutManager(mContext, 8)
        tv_box_shopping.addItemDecoration(SpacesItemDecoration(10, 10, 10, 10))
        nativeAppAdapter = NativeAppAdapter(mContext)
        nativeAppsAdapter = NativeAppsAdapter(mContext)
        tv_box_recycler.adapter = nativeAppAdapter
        tv_box_shopping.adapter = nativeAppsAdapter
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
            }
        } else {
            setNativeData()
            var weather = Gson().fromJson(getWeather(), WeatherBean::class.java)
            if (weather != null && weather.result != null) {
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
        }
    }

    override fun initEvent() {
        tv_box_back.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_box_back.setBackgroundResource(R.mipmap.welcome_help_bg)
            } else {
                tv_box_back.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
            }
        }

        tv_box_back.setOnClickListener {
            finishActivitys(NativeAppActivity::class.java)
        }
        nativeAppAdapter?.setOnItemStateListener(object : NativeAppAdapter.onSelectListener {
            override fun onSelectItemListener(category: ApplicationNewBean.ResultBean.CategoryBean, relativeLayout: RelativeLayout, position: Int) {
                isFirst = false
                mId = category.id.toString()
                if (checkNetEnable(mContext)) {
                    if (messageBean != null) {
                        var type = "0"
                        if (LANGUAGE_TYPE.equals("chinese")) {
                            type = "1"
                        } else {
                            type = "0"
                        }
                        if (TextUtils.isEmpty(mId)) {
                            mPresenter?.getApplicationInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                        } else {
                            mPresenter?.getApplicationInfo(mId, messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                        }
                    } else {
                        goToActivity(mActivity, MessageActivity::class.java)
                    }
                } else {
                    long_toast("请检查你的网络", mContext)
                }
            }
        })

        nativeAppsAdapter?.setListener(object : NativeAppsAdapter.OnSelectItemListener {
            override fun setOnSelectItemListener(objectData: ApplicationNewBean.ResultBean.DetailsBean, position: Int) {
                if (isInstallApp(packageManager, objectData.runCode)) {
                    if (objectData.jumpPage != null && objectData.jumpPage != "") {
                        openOthemApp(mActivity, objectData.runCode, objectData.jumpPage);
                    } else {
                        openOthemApp(mActivity, objectData.runCode)
                    }
                } else {
                    long_toast("没有安装该应用", mContext)
                }
            }
        })
    }

    override fun initData() {
        if (checkNetEnable(mContext)) {
            if (messageBean != null) {
                var type = "0"
                if (LANGUAGE_TYPE.equals("chinese")) {
                    type = "1"
                } else {
                    type = "0"
                }
//                loadingDialog.showDialog(mActivity!!, "正在加载中", false, null)
                if (TextUtils.isEmpty(mId)) {
                    mPresenter?.getApplicationInfo("", messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                } else {
                    mPresenter?.getApplicationInfo(mId, messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                }
            } else {
                goToActivity(mActivity, MessageActivity::class.java)
            }
        } else {
            setNativeData()
            long_toast("请检查你的网络", mContext)
        }
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

    fun setNativeData() {
        if (getNativeApp() != null) {
            var naticeAppBean = Gson().fromJson(getNativeApp(), ApplicationNewBean::class.java)
            if (naticeAppBean != null && naticeAppBean.result != null) {
                if (isFirst) {
                    nativeAppAdapter?.refreshList(naticeAppBean?.result?.category)
                }
                nativeAppsAdapter?.refreshList(naticeAppBean?.result?.details)
                if (LANGUAGE_TYPE.equals("chinese")) {
                    tv_wifi_name.text = "WiFi名称:"
                    tv_wifi_password.text = "WiFi密码:"
                } else {
                    tv_wifi_name.text = "WiFiSSID:"
                    tv_wifi_password.text = "PASSWORD:"
                }
                tv_box_wifi_name.text = naticeAppBean?.result?.wifi
                tv_box_wifi_password.text = naticeAppBean?.result?.password
                if (mId != null) {
                    for ((index, value) in naticeAppBean?.result?.category?.withIndex()!!) {
                        if (value != null) {
                            if (mId?.equals(value.id.toString())!!) {
                                nativeAppAdapter?.initSelect(index)
                            }
                        }
                    }
                } else {
                    nativeAppAdapter?.initSelect(0)
                }
            }
        }   else if (getWelcome() != null) {
            var mWelcomeBean = Gson().fromJson(getWelcome(), WelcomeBean::class.java)
            if (mWelcomeBean != null) {
                if (LANGUAGE_TYPE.equals("chinese")) {
                    tv_wifi_name.text = "WiFi名称:"
                    tv_wifi_password.text = "WiFi密码:"
                } else {
                    tv_wifi_name.text = "WiFiSSID:"
                    tv_wifi_password.text = "PASSWORD:"
                }
                tv_box_wifi_name.text = mWelcomeBean?.result?.wifi
                tv_box_wifi_password.text = mWelcomeBean?.result?.password
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
package com.messcat.tvbox.module.home.activity

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.RelativeLayout
import com.messcat.kotlin.mchttp.RetrofitServiceManager
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.tvbox.module.home.adapter.NewsClassAdapter
import com.messcat.tvbox.module.home.bean.ClassifyBean
import com.messcat.tvbox.module.home.contract.ClassifyContract
import com.messcat.tvbox.module.home.presenter.ClassifyPresenter
import java.util.concurrent.TimeUnit
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.messcat.kotlin.utils.*
import com.messcat.mclibrary.BASE_URL
import com.messcat.mclibrary.SpacesItemDecoration
import com.messcat.mclibrary.base.BaseApplication
import com.messcat.mclibrary.base.LANGUAGE_TYPE
import com.messcat.mclibrary.base.MEMBER
import com.messcat.mclibrary.goToActivity
import com.messcat.mclibrary.util.*
import com.messcat.tvbox.R
import com.messcat.tvbox.R.id.*
import com.messcat.tvbox.module.home.adapter.NewsInformationAdapter
import com.messcat.tvbox.module.home.adapter.ShoppingPicAdapter
import com.messcat.tvbox.module.home.bean.MessageBean
import com.messcat.tvbox.module.home.bean.WeatherBean
import com.messcat.tvbox.utils.loadImage
import kotlinx.android.synthetic.main.activity_classify.*


/**
 * Created by Administrator on 2017/8/22 0022.
 */

class ClassifyActivity : MVPActivity<ClassifyPresenter>(), ClassifyContract.View {

    private var newsClassAdapter: NewsClassAdapter? = null
    private var id: String? = null
    private var newsInformationAdapter: NewsInformationAdapter? = null
    private var mCategoryBean: ClassifyBean.ResultBean? = null
    private var mList3Bean: ClassifyBean.ResultBean.Categorys2Bean.List2Bean.List3Bean? = null
    private var shoppingAdapter: ShoppingPicAdapter? = null
    private var isFirst = true
    private var messageBean: MessageBean? = null
    private lateinit var weather: WeatherBean
    private lateinit var type: String
    private var hasGet = false
    private var classifyBeanData: ClassifyBean? = null
    var typeSelectPosition: Int = 0
    private var infoType: Int = 0//记录目录下的是商品还是新闻 1是商品 2是新闻

    override fun getLayout(): Int = R.layout.activity_classify

    override fun showError(msg: String?) {
//        if (loadingDialog != null) {
//            loadingDialog.dismissDialog()
//        }
        setClassifyData()
    }

    override fun showLoadingDialog() {
    }

    override fun dismissLoadingDialog() {
    }

    override fun getInfo(classifyBean: ClassifyBean?) {
        hasGet = true
        classifyBeanData = classifyBean
        when (type) {
            "左上" -> {
                saveLeftTop(Gson().toJson(classifyBean))
            }
            "左下" -> {
                saveLeftBotton(Gson().toJson(classifyBean))
            }
            "中间" -> {
                saveClass(Gson().toJson(classifyBean))
            }
            "右上" -> {
                saveRightTop(Gson().toJson(classifyBean))
            }
            "右下" -> {
                saveRightBotton(Gson().toJson(classifyBean))
            }
            else -> {
                saveSkittles(Gson().toJson(classifyBean))
            }
        }
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_wifi_name.text = "WiFi名称:"
            tv_wifi_password.text = "WiFi密码:"
        } else {
            tv_wifi_name.text = "WiFiSSID:"
            tv_wifi_password.text = "PASSWORD:"
        }
        tv_box_wifi_name?.text = classifyBean?.result?.wifi
        tv_box_wifi_password.text = classifyBean?.result?.password
        if (isFirst) {
            newsClassAdapter?.refreshList(classifyBean?.result?.categorys2!!)
//            newsInformationAdapter?.refreshList(classifyBean?.result?.categorys2!![0].list2)
        }
        for ((index, value) in classifyBean?.result?.categorys2?.withIndex()!!) {
            if (value != null) {
                if (id?.equals(value.id.toString())!!) {
                    newsClassAdapter?.initSelect(index)
                    newsClassAdapter?.setFocus(index)
                    when (classifyBean?.result?.categorys2!![index].type) {
                        "1" -> {
                            infoType = 1
                            if (tv_box_shop.visibility == View.GONE) {
                                tv_box_shop.visibility = View.VISIBLE
                            }
                            if (tv_box_news.visibility == View.VISIBLE) {
                                tv_box_news.visibility = View.GONE
                            }
                            if (isFirst) {

                            } else {
                                getShoppData(classifyBean?.result)
                            }
                        }
                        "2" -> {
                            infoType = 2
                            if (tv_box_news.visibility == View.GONE) {
                                tv_box_news.visibility = View.VISIBLE
                            }
                            if (tv_box_shop.visibility == View.VISIBLE) {
                                tv_box_shop.visibility = View.GONE
                            }
                            if (isFirst) {
//                                getNewsData(classifyBean?.result, 0)
//                                newsInformationAdapter?.refreshList(classifyBean?.result?.categorys2!![0].list2)
                            } else {
                                getNewsData(classifyBean?.result, 0)
                                newsInformationAdapter?.refreshList(classifyBean?.result?.categorys2!![index].list2)
                            }
                        }
                        else -> {
                            long_toast("没有你选择的类型", mContext)
                        }
                    }
                } else {
//                    if (id?.equals(value.fatherId?.id.toString())!!) {
//                        newsClassAdapter?.initSelect(0)
//                        if (tv_box_news.visibility == View.GONE) {
//                            tv_box_news.visibility = View.VISIBLE
//                        }
//                        newsInformationAdapter?.refreshList(classifyBean?.result?.categorys3)
//                        getNewsData(classifyBean?.result, 0)
//                    }
                }
            }
        }
//        if (loadingDialog != null) {
//            loadingDialog.dismissDialog()
//        }
    }

    override fun initPresenter(): ClassifyPresenter = ClassifyPresenter(this,
            ClassifyPresenter.ClassifyLoader(RetrofitServiceManager.instance.create(ClassifyPresenter.ClassifyHttp::class.java)))

    override fun initView() {
        if (intent.extras.getString("id") != null) {
            id = intent.extras.getString("id")
            type = intent.extras.getString("type")
        }
        messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
        tv_box_recycler.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        newsClassAdapter = NewsClassAdapter(mContext)
        tv_box_recycler.adapter = newsClassAdapter
        newsInformartion()
        shoppingAdapter()
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
    }

    fun newsInformartion() {
        tv_box_classify3.layoutManager = LinearLayoutManager(mContext)
        newsInformationAdapter = NewsInformationAdapter(mContext)
        tv_box_classify3.adapter = newsInformationAdapter
    }

    fun shoppingAdapter() {
        tv_box_shopping.layoutManager = StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL)
        tv_box_shopping.addItemDecoration(SpacesItemDecoration(10, 10, 10, 10))
        shoppingAdapter = ShoppingPicAdapter(mContext)
        tv_box_shopping.adapter = shoppingAdapter
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
            finishActivitys(ClassifyActivity::class.java)
        }
        //上面分类的适配器
        newsClassAdapter?.setOnItemStateListener(object : NewsClassAdapter.onSelectListener {
            override fun onSelectItemListener(category: ClassifyBean.ResultBean.Categorys2Bean, relativeLayout: RelativeLayout, position: Int) {
                isFirst = false
                id = category.id.toString()
                typeSelectPosition = position
                if (messageBean != null) {
                    var type = "0"
                    if (LANGUAGE_TYPE.equals("chinese")) {
                        type = "1"
                    } else if (LANGUAGE_TYPE.equals("english")) {
                        type = "0"
                    }
                    if (infoType == 1) {
                        mPresenter?.getCategoryInfo(id, messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                    } else {
                        if (hasGet) {
                            if (classifyBeanData != null) {
                                getInfo(classifyBeanData)
                            }
                        } else {
                            mPresenter?.getCategoryInfo(id, messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                        }
                    }
                } else {
                    goToActivity(mActivity, MessageActivity::class.java)
                }
            }
        })
        newsInformationAdapter?.setListener(object : NewsInformationAdapter.OnSelectListener {
            override fun setOnSelectListener(id: String, textView: TextView, position: Int) {
                if (isFirst) {

                } else {
                    getNewsData(mCategoryBean, position)
                }
            }
        })

        shoppingAdapter?.setListener(object : ShoppingPicAdapter.OnSelectListener {
            override fun setOnSelectListener() {
                long_toast("当前尚未开通该功能", mContext)
            }
        })
    }


    fun getNewsData(categoryBean: ClassifyBean.ResultBean?, position: Int) {
        mCategoryBean = categoryBean
        /** if (categoryBean?.newsDetails?.size!! >= position + 1 &&
        categoryBean?.newsDetails!![position] != null &&
        categoryBean?.newsDetails!![position].picture != null) {
        //            loadImage(mContext, BASE_URL + categoryBean?.newsDetails!![position].picture, tv_box_hotel_image)
        Glide.with(mContext).load(BASE_URL + categoryBean?.newsDetails!![position].picture).error(R.mipmap.icon_moren).into(tv_box_hotel_image)
        //            tv_box_hotel_content.isFocusable = false
        //            tv_box_hotel_content.settings.javaScriptEnabled = true
        //            tv_box_hotel_content.settings.defaultTextEncodingName = "utf-8"
        tv_box_hotel_content.loadData(categoryBean?.newsDetails!![position].content, "text/html; charset=UTF-8", null)
        tv_box_hotel_content.setBackgroundColor(0)
        //            tv_box_hotel_content.background.alpha = 2 **/
        if (categoryBean!!.categorys2!![typeSelectPosition].list2 != null) {
            if (categoryBean!!.categorys2!![typeSelectPosition].list2.size > 0) {
                if (position <= categoryBean!!.categorys2!![typeSelectPosition].list2.size) {
//                    if (categoryBean!!.categorys2!![typeSelectPosition].list2[position]?.list3.size > 0) {
                    if (categoryBean != null && categoryBean.categorys2!![typeSelectPosition]?.list2 != null && categoryBean.categorys2!![typeSelectPosition].list2[position]?.list3!! != null && categoryBean.categorys2!![typeSelectPosition].list2[position]?.list3.size > 0) {
                        tv_box_hotel_content.loadData(categoryBean.categorys2!![typeSelectPosition].list2[position]?.list3[0].content, "text/html; charset=UTF-8", null)
                        tv_box_hotel_content.setBackgroundColor(0)
                        if (mActivity != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (mActivity is Activity && (mActivity as Activity).isDestroyed) {
                                    return
                                } else {
                                    loadImage(mActivity, BASE_URL + categoryBean.categorys2!![typeSelectPosition].list2[position]?.list3[0].picture, tv_box_hotel_image)
                                }
                            }
//                            Glide.with(BaseApplication.application).load(BASE_URL + categoryBean.categorys2!![typeSelectPosition].list2[position]?.list3[0].picture).diskCacheStrategy(DiskCacheStrategy.RESULT).error(R.mipmap.icon_moren).into(tv_box_hotel_image)
                        }
                    } else {
                        long_toast("没有获取到数据", mContext)
                        tv_box_hotel_image.setImageResource(R.mipmap.icon_moren)
                        tv_box_hotel_content.loadData("", "text/html; charset=UTF-8", null)
                        tv_box_hotel_content.setBackgroundColor(0)
                    }
//                    }
//                else {
//                        long_toast("没有获取到数据11111111111111", mContext)
//                    }
                } else {
                    long_toast("没有获取到数据", mContext)
                    tv_box_hotel_image.setImageResource(R.mipmap.icon_moren)
                    tv_box_hotel_content.loadData("", "text/html; charset=UTF-8", null)
                    tv_box_hotel_content.setBackgroundColor(0)
                }
            } else {
                long_toast("没有获取到数据", mContext)
                tv_box_hotel_image.setImageResource(R.mipmap.icon_moren)
                tv_box_hotel_content.loadData("", "text/html; charset=UTF-8", null)
                tv_box_hotel_content.setBackgroundColor(0)
            }
        }

        newsInformationAdapter?.initSelect(position)
    }

    fun getShoppData(result: ClassifyBean.ResultBean?) {
        if (result?.goodsDetails != null) {
            shoppingAdapter?.refreshList(result?.goodsDetails)
        }
    }

    override fun initData() {
        if (checkNetEnable(mContext)) {
            if (messageBean != null) {
                var code = messageBean?.result?.diCode
                var num = messageBean?.result?.roomNum
                var type = "0"
                if (LANGUAGE_TYPE.equals("chinese")) {
                    type = "1"
                } else if (LANGUAGE_TYPE.equals("english")) {
                    type = "0"
                }
                mPresenter?.getCategoryInfo(id, messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
            } else {
                goToActivity(mActivity, MessageActivity::class.java)
            }
        } else {
            long_toast("请检查你的网络", mContext)
            setClassifyData()
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

    fun setClassifyData() {
        var classifyBean: ClassifyBean? = null
        when (type) {
            "左上" -> {
                if (getLeftTop() != null) {
                    classifyBean = Gson().fromJson(getLeftTop(), ClassifyBean::class.java)
                }
            }
            "左下" -> {
                if (getLeftBotton() != null) {
                    classifyBean = Gson().fromJson(getLeftBotton(), ClassifyBean::class.java)
                }
            }
            "中间" -> {
                if (getClass() != null) {
                    classifyBean = Gson().fromJson(getClass(), ClassifyBean::class.java)
                }
            }
            "右上" -> {
                if (getRightTop() != null) {
                    classifyBean = Gson().fromJson(getRightTop(), ClassifyBean::class.java)
                }
            }
            "右下" -> {
                if (getRightBotton() != null) {
                    classifyBean = Gson().fromJson(getRightBotton(), ClassifyBean::class.java)
                }
            }
            else -> {
                if (getSkittles() != null) {
                    classifyBean = Gson().fromJson(getSkittles(), ClassifyBean::class.java)
                }
            }
        }
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_wifi_name.text = "WiFi名称:"
            tv_wifi_password.text = "WiFi密码:"
        } else {
            tv_wifi_name.text = "WiFiSSID:"
            tv_wifi_password.text = "PASSWORD:"
        }
        tv_box_wifi_name?.text = classifyBean?.result?.wifi
        tv_box_wifi_password.text = classifyBean?.result?.password
        if (classifyBean != null && classifyBean.result != null) {
            if (isFirst) {
                newsClassAdapter?.refreshList(classifyBean?.result?.categorys2!!)
            }
            for ((index, value) in classifyBean?.result?.categorys2?.withIndex()!!) {
                if (value != null) {
                    if (id?.equals(value.id.toString())!!) {
                        newsClassAdapter?.initSelect(index)
                        newsClassAdapter?.setFocus(index)
                        when (classifyBean?.result?.categorys2!![index].type) {
                            "1" -> {
                                if (tv_box_shop.visibility == View.GONE) {
                                    tv_box_shop.visibility = View.VISIBLE
                                }
                                if (tv_box_news.visibility == View.VISIBLE) {
                                    tv_box_news.visibility = View.GONE
                                }
                                getShoppData(classifyBean?.result)
                            }
                            "2" -> {
                                if (tv_box_news.visibility == View.GONE) {
                                    tv_box_news.visibility = View.VISIBLE
                                }
                                if (tv_box_shop.visibility == View.VISIBLE) {
                                    tv_box_shop.visibility = View.GONE
                                }
                                if (classifyBean?.result?.categorys3 != null) {
                                    newsInformationAdapter?.refreshList(classifyBean?.result?.categorys2!![index].list2)
                                }
                                getNewsData(classifyBean?.result, 0)
                            }
                            else -> {
                                long_toast("没有你选择的类型", mContext)
                            }
                        }
                    } else {
//                        if (id?.equals(value.fatherId?.id.toString())!!) {
//                            newsClassAdapter?.initSelect(0)
//                            if (tv_box_news.visibility == View.GONE) {
//                                tv_box_news.visibility = View.VISIBLE
//                            }
//                            newsInformationAdapter?.refreshList(classifyBean?.result?.categorys2!![index].list2)
//                            getNewsData(classifyBean?.result, 0)
//                        }
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
        if (Util.isOnMainThread()) {
            Glide.with(BaseApplication.application).pauseRequests()
        }
    }
}

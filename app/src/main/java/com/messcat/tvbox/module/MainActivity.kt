package com.messcat.tvbox.module

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.messcat.kotlin.mchttp.RetrofitServiceManager
import com.messcat.mclibrary.BASE_URL
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.goToActivity
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.presenter.MainPresenter
import com.messcat.tvbox.widget.RatingBar
import kotlinx.android.synthetic.main.activity_main.*
import com.messcat.tvbox.module.home.contract.MainContract
import java.util.concurrent.TimeUnit
import com.messcat.kotlin.utils.*
import com.messcat.mclibrary.base.LANGUAGE_TYPE
import com.messcat.mclibrary.util.*
import com.messcat.tvbox.utils.*
import java.util.ArrayList
import com.messcat.tvbox.module.home.GlideRoundTransform
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.messcat.tvbox.module.home.BannerCycleViewPager
import com.messcat.tvbox.module.home.activity.*
import com.messcat.tvbox.module.home.bean.*
import kotlinx.android.synthetic.main.activity_main.tv_box_address
import kotlinx.android.synthetic.main.activity_main.tv_box_shopping
import kotlinx.android.synthetic.main.activity_main.tv_box_time
import kotlinx.android.synthetic.main.activity_main.tv_box_weather
import kotlinx.android.synthetic.main.activity_main.tv_box_wifi_name
import kotlinx.android.synthetic.main.activity_main.tv_box_year
import kotlinx.android.synthetic.main.activity_main.tv_wifi_name
import kotlinx.android.synthetic.main.activity_main.tv_wifi_password
import kotlinx.android.synthetic.main.activity_native_app.*

/**
 * Created by Administrator on 2017/8/23 0023.
 */

class MainActivity : MVPActivity<MainPresenter>(), MainContract.View, View.OnFocusChangeListener, View.OnClickListener {


    private var mHomeBean: HomeBean? = null
    private var travelId = 0//旅游
    private var cateId = 0//美食
    private var centrelId = 0//中间
    private var socialId = 0//社交
    private var shoppingId = 0//购物
    private var serviceId = 0//酒店服务
    private var city: String? = null
    private var temperature: String? = null
    private var weather: String? = null
    private var wifiManager: WifiManager? = null
    private var applicationBean: ApplicationBean? = null
    private var positionSkittles: Int = 0;//记录吃喝玩乐的位置
    private var positionApp: Int = 0;//记录本地应用的位置
    private var positionService: Int = 0;//记录酒店服务的位置
    private var positionChannel: Int = 0;//记录酒店服务的位置
    private var type1: Int = 0;//记录每个选项的type
    private var type2: Int = 0;//记录每个选项的type
    private var type3: Int = 0;//记录每个选项的type
    private var type4: Int = 0;//记录每个选项的type
    private var type5: Int = 0;//记录每个选项的type
    private var type6: Int = 0;//记录每个选项的type
    private var positionType: Int = -1//记录左上 左下 中间 右上 右下 是否有对应数据已是第几条
    private var positionType1: Int = -1
    private var positionType2: Int = -1
    private var positionType3: Int = -1
    private var positionType4: Int = -1

    override fun getLayout(): Int = R.layout.activity_main

    override fun onResume() {
        super.onResume()
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_wifi_name.text = "WiFi名称:"
            tv_wifi_password.text = "WiFi密码:"
        } else {
            tv_wifi_name.text = "WiFiSSID:"
            tv_wifi_password.text = "PASSWORD:"
        }
    }

    /**
     * 首页数据
     */
    override fun getIndexInfoView(homeBean: HomeBean?, type: String?) {
        if (type.equals("0")) {
            saveEnglishMain(Gson().toJson(homeBean))
        } else {
            saveMain(Gson().toJson(homeBean))
        }
        mHomeBean = homeBean
        tv_box_name.text = homeBean?.result?.roomInfo?.hotelId?.name
        tv_box_ename.text = homeBean?.result?.roomInfo?.hotelId?.enName
        tv_box_ratingbar.visibility = View.VISIBLE
        setRatingBar(false, homeBean?.result?.roomInfo?.hotelId?.level?.toFloat()!!)
        marquestextview.text = homeBean?.result?.title
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_wifi_name.text = "WiFi名称:"
            tv_wifi_password.text = "WiFi密码:"
        } else {
            tv_wifi_name.text = "WiFiSSID:"
            tv_wifi_password.text = "PASSWORD:"
        }
        tv_box_wifi_name.text = homeBean?.result?.roomInfo?.wifiName
        tv_box_password.text = mHomeBean?.result?.roomInfo?.wifiPassword
        for ((index, value) in homeBean?.result?.categorys2?.withIndex()!!) {
            if (value != null) {
                when (homeBean?.result?.categorys2!![index].position) {
                    "1" -> {//旅游
                        positionType = index
//                        loadImage(mContext, BASE_URL + homeBean?.result?.categorys2!![index].picture!!, tv_box_img_travel)
                        Glide.with(mActivity).load(BASE_URL + homeBean?.result?.categorys2!![index].picture!!)
                                .transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_travel)
                        tv_box_text_travel.text = homeBean?.result?.categorys2!![index].name
                        travelId = homeBean?.result?.categorys2!![index].id!!
                    }
                    "2" -> {//美食
                        positionType1 = index
                        loadImage(mContext, BASE_URL + homeBean?.result?.categorys2!![index].picture!!, tv_box_img_cate)
                        Glide.with(mActivity).load(BASE_URL + homeBean?.result?.categorys2!![index].picture!!)
                                .transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.ALL).into(tv_box_img_cate)
                        tv_box_text_cate.text = homeBean?.result?.categorys2!![index].name
                        cateId = homeBean?.result?.categorys2!![index].id!!
                    }
                    "3" -> {//中间
                        positionType2 = index
//                        var a = homeBean?.result?.categorys2!![index].picture!!.indexOf(",")
//                        var listString = ArrayList<String>()
                        var listImages = ArrayList<String>()
//                        listImages.add(BASE_URL + "/upload/enterprise/image/20180918185701771a432814f.jpeg")
//                        listImages.add(BASE_URL + "/upload/enterprise/image/20180918185701771a432814f.jpeg")
//                        listImages.add(BASE_URL + "/upload/enterprise/image/20180918185701771a432814f.jpeg")
                        listImages.add(BASE_URL + homeBean?.result?.categorys2!![index].picture)
                        listImages.add(BASE_URL + homeBean?.result?.categorys2!![index].picture1)
                        listImages.add(BASE_URL + homeBean?.result?.categorys2!![index].picture2)

//                        if (a != -1) {
//                            listString = homeBean?.result?.categorys2!![index].picture!!.split(",") as ArrayList<String>
//                            for (i in listString.indices) {
//                                listImages.add(BASE_URL + listString[i])
//                            }
//                            Toast.makeText(mActivity, "111" + listImages.get(0), Toast.LENGTH_SHORT).show()
//                        } else {
//                            listImages.add(BASE_URL + homeBean?.result?.categorys2!![index].picture!!)
//                            Toast.makeText(mActivity, "22" + listImages.get(0), Toast.LENGTH_SHORT).show()
//                        }
                        val mList = ArrayList<BannerInfoBean>()
                        for (i in listImages.indices) {
                            mList.add(BannerInfoBean(i, "", listImages.get(i)))
                        }
                        banner_vp.setDelay(5000)
                        banner_vp.setData(mList, mAdCycleViewListener)
//                        banner.setImages(listImages)
//                        banner.start()
//                        loadImage(mContext, BASE_URL + homeBean?.result?.categorys2!![index].picture!!, tv_box_centre_img)
                        centrelId = homeBean?.result?.categorys2!![index].id!!
                    }
                    "4" -> {//右上
                        positionType3 = index
                        positionType3 = index
//                        loadImage(mContext, BASE_URL + homeBean?.result?.categorys2!![index].picture!!, tv_box_img_social)
                        Glide.with(mActivity).load(BASE_URL + homeBean?.result?.categorys2!![index].picture!!).transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_social)
                        tv_box_text_social.text = homeBean?.result?.categorys2!![index].name
                        socialId = homeBean?.result?.categorys2!![index].id!!
                    }
                    "5" -> {//购物
                        positionType4 = index
//                        loadImage(mContext, BASE_URL + homeBean?.result?.categorys2!![index].picture!!, tv_box_img_shopping)
                        Glide.with(mActivity).load(BASE_URL + homeBean?.result?.categorys2!![index].picture!!).transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_shopping)
                        tv_box_text_shopping.text = homeBean?.result?.categorys2!![index].name
                        shoppingId = homeBean?.result?.categorys2!![index].id!!
                    }
                    "6" -> {
                        serviceId = homeBean?.result?.categorys2!![index].id!!
                    }
                    else -> {

                    }
                }
            }
        }
//        if (loadingDialog != null) {
//            loadingDialog.dismissDialog()
//        }
    }

    /**
     * 轮播图点击监听
     */
    private val mAdCycleViewListener = object : BannerCycleViewPager.ImageCycleViewListener {

        override fun onImageClick(info: BannerInfoBean, position: Int, imageView: View) {
        }
    }

    /**
     * 获取应用分类 0吃喝玩乐1本地应用2酒店服务3执行代码
     */
    override fun getApplicationView(bean: ApplicationBean?, type: String?) {
        if (type.equals("0")) {
            saveEnglishMainBottom(Gson().toJson(bean))
        } else {
            saveMainBottom(Gson().toJson(bean))
        }
        applicationBean = bean
        if (applicationBean!!.result!!.applica!!.size > 0) {
            tv_box_channel.visibility = View.GONE//电影轮播
            tv_box_movie_world.visibility = View.GONE//电影世界
            tv_box_hotel_service.visibility = View.GONE//酒店服务
            tv_box_live.visibility = View.GONE//直播频道
            tv_box_local_app.visibility = View.GONE//本地应用
            tv_box_skittles.visibility = View.GONE //吃喝玩乐
            when (applicationBean!!.result!!.applica!!.size) {
                0 -> {
                }
                1 -> {
                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                    tv_box_channel.visibility = View.VISIBLE
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                    if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                        type1 = 0
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                        type1 = 1
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                        type1 = 2
                    } else {
                        type1 = 3
                    }
                }
                2 -> {
                    tv_box_channel.visibility = View.VISIBLE//电影轮播
                    tv_box_movie_world.visibility = View.VISIBLE//电影世界
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                    tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                    if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                        type1 = 0
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                        type1 = 1
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                        type1 = 2
                    } else {
                        type1 = 3
                    }
                    if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                        type2 = 0
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                        type2 = 1
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                        type2 = 2
                    } else {
                        type2 = 3
                    }
                }
                3 -> {
                    tv_box_channel.visibility = View.VISIBLE//电影轮播
                    tv_box_movie_world.visibility = View.VISIBLE//电影世界
                    tv_box_hotel_service.visibility = View.VISIBLE//直播频道
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                    tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                    tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                    if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                        type1 = 0
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                        type1 = 1
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                        type1 = 2
                    } else {
                        type1 = 3
                    }
                    if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                        type2 = 0
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                        type2 = 1
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                        type2 = 2
                    } else {
                        type2 = 3
                    }
                    if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                        type3 = 0
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                        type3 = 1
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                        type3 = 2
                    } else {
                        type3 = 3
                    }
                }
                4 -> {
                    tv_box_channel.visibility = View.VISIBLE//电影轮播
                    tv_box_movie_world.visibility = View.VISIBLE//电影世界
                    tv_box_live.visibility = View.VISIBLE//直播频道
                    tv_box_hotel_service.visibility = View.VISIBLE//酒店服务
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![3].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_live_img)
                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                    tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                    tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                    tv_box_live_name.text = applicationBean!!.result!!.applica!![3].appname
                    if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                        type1 = 0
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                        type1 = 1
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                        type1 = 2
                    } else {
                        type1 = 3
                    }
                    if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                        type2 = 0
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                        type2 = 1
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                        type2 = 2
                    } else {
                        type2 = 3
                    }
                    if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                        type3 = 0
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                        type3 = 1
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                        type3 = 2
                    } else {
                        type3 = 3
                    }
                    if (applicationBean!!.result!!.applica!![3].type.equals("0")) {
                        type4 = 0
                    } else if (applicationBean!!.result!!.applica!![3].type.equals("1")) {
                        type4 = 1
                    } else if (applicationBean!!.result!!.applica!![3].type.equals("2")) {
                        type4 = 2
                    } else {
                        type4 = 3
                    }
                }
                5 -> {
                    tv_box_channel.visibility = View.VISIBLE//电影轮播
                    tv_box_movie_world.visibility = View.VISIBLE//电影世界
                    tv_box_live.visibility = View.VISIBLE//直播频道
                    tv_box_hotel_service.visibility = View.VISIBLE//酒店服务
                    tv_box_local_app.visibility = View.VISIBLE//本地应用
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![3].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_live_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![4].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_local_app_img)
                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                    tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                    tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                    tv_box_live_name.text = applicationBean!!.result!!.applica!![3].appname
                    tv_box_local_app_name.text = applicationBean!!.result!!.applica!![4].appname
                    if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                        type1 = 0
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                        type1 = 1
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                        type1 = 2
                    } else {
                        type1 = 3
                    }
                    if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                        type2 = 0
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                        type2 = 1
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                        type2 = 2
                    } else {
                        type2 = 3
                    }
                    if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                        type3 = 0
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                        type3 = 1
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                        type3 = 2
                    } else {
                        type3 = 3
                    }
                    if (applicationBean!!.result!!.applica!![3].type.equals("0")) {
                        type4 = 0
                    } else if (applicationBean!!.result!!.applica!![3].type.equals("1")) {
                        type4 = 1
                    } else if (applicationBean!!.result!!.applica!![3].type.equals("2")) {
                        type4 = 2
                    } else {
                        type4 = 3

                    }
                    if (applicationBean!!.result!!.applica!![4].type.equals("0")) {
                        type5 = 0
                    } else if (applicationBean!!.result!!.applica!![4].type.equals("1")) {
                        type5 = 1
                    } else if (applicationBean!!.result!!.applica!![4].type.equals("2")) {
                        type5 = 2
                    } else {
                        type5 = 3
                    }
                }
                6 -> {
                    tv_box_channel.visibility = View.VISIBLE//电影轮播
                    tv_box_movie_world.visibility = View.VISIBLE//电影世界
                    tv_box_live.visibility = View.VISIBLE//直播频道
                    tv_box_hotel_service.visibility = View.VISIBLE//酒店服务
                    tv_box_local_app.visibility = View.VISIBLE//本地应用
                    tv_box_skittles.visibility = View.VISIBLE //吃喝玩乐
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![3].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_live_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![4].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_local_app_img)
                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![5].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_skittles_img)
                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                    tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                    tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                    tv_box_live_name.text = applicationBean!!.result!!.applica!![3].appname
                    tv_box_local_app_name.text = applicationBean!!.result!!.applica!![4].appname
                    tv_box_skittles_name.text = applicationBean!!.result!!.applica!![5].appname
                    if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                        type1 = 0
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                        type1 = 1
                    } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                        type1 = 2
                    } else {
                        type1 = 3
                    }
                    if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                        type2 = 0
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                        type2 = 1
                    } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                        type2 = 2
                    } else {
                        type2 = 3
                    }
                    if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                        type3 = 0
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                        type3 = 1
                    } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                        type3 = 2
                    } else {
                        type3 = 3
                    }
                    if (applicationBean!!.result!!.applica!![3].type.equals("0")) {
                        type4 = 0
                    } else if (applicationBean!!.result!!.applica!![3].type.equals("1")) {
                        type4 = 1
                    } else if (applicationBean!!.result!!.applica!![3].type.equals("2")) {
                        type4 = 2
                    } else {
                        type4 = 3
                    }
                    if (applicationBean!!.result!!.applica!![4].type.equals("0")) {
                        type5 = 0
                    } else if (applicationBean!!.result!!.applica!![4].type.equals("1")) {
                        type5 = 1
                    } else if (applicationBean!!.result!!.applica!![4].type.equals("2")) {
                        type5 = 2
                    } else {
                        type5 = 3
                    }
                    if (applicationBean!!.result!!.applica!![5].type.equals("0")) {
                        type6 = 0
                    } else if (applicationBean!!.result!!.applica!![5].type.equals("1")) {
                        type6 = 1
                    } else if (applicationBean!!.result!!.applica!![5].type.equals("2")) {
                        type6 = 2
                    } else {
                        type6 = 3
                    }
                }
                else -> {
                    tv_box_channel.visibility = View.GONE//电影轮播
                    tv_box_movie_world.visibility = View.GONE//电影世界
                    tv_box_live.visibility = View.GONE//直播频道
                    tv_box_hotel_service.visibility = View.GONE//酒店服务
                    tv_box_local_app.visibility = View.GONE//本地应用
                    tv_box_skittles.visibility = View.GONE //吃喝玩乐
                }
            }
        } else {
            tv_box_channel.visibility = View.GONE
            tv_box_movie_world.visibility = View.GONE
            tv_box_hotel_service.visibility = View.GONE
            tv_box_live.visibility = View.GONE
            tv_box_local_app.visibility = View.GONE//本地应用
            tv_box_skittles.visibility = View.GONE //吃喝玩乐
        }
    }

    /**
     * 获取首页异常
     */
    override fun showError(msg: String?) {
//        if (loadingDialog != null) {
//            loadingDialog.dismissDialog()
//        }
        getSPData()
    }


    override fun initPresenter(): MainPresenter = MainPresenter(this,
            MainPresenter.MainLoader(RetrofitServiceManager.instance.create(MainPresenter.MainHttp::class.java)))

    @SuppressLint("WifiManagerLeak")
    override fun initView() {
        scheduledThreadPool.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        var bundle = intent.extras
        if (bundle != null) {
            city = bundle.getString("city")
            temperature = bundle.getString("temperature")
            weather = bundle.getString("weather")
        }
        if (city != null) {
            tv_box_address.text = city
        }
        if (temperature != null) {
            tv_box_address_weather.text = temperature
        }
        if (weather != null) {
            tv_box_address_dy.text = weather
            if (isContains(weather!!, "多云")) {
                tv_box_weather.setImageResource(R.mipmap.weather_cloudy)
            } else if (isContains(weather!!, "雨")) {//判断字符串中是否有那个中文
                tv_box_weather.setImageResource(R.mipmap.weather_rain)
            } else {
                tv_box_weather.setImageResource(R.mipmap.weather_sun)
            }
        }
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_box_help.text = "帮助"
        } else {
            tv_box_help.text = "help"
        }
    }


    fun getSPData() {
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_wifi_name.text = "WiFi名称:"
            tv_wifi_password.text = "WiFi密码:"
            if (getMain() != null) {
                mHomeBean = Gson().fromJson(getMain(), HomeBean::class.java)
            }
            if (getMainBottom() != null) {
                applicationBean = Gson().fromJson(getMainBottom(), ApplicationBean::class.java)
            }
        } else {
            tv_wifi_name.text = "WiFiSSID:"
            tv_wifi_password.text = "PASSWORD:"
            if (getEnglishMain() != null) {
                mHomeBean = Gson().fromJson(getEnglishMain(), HomeBean::class.java)
            }
            if (getEnglishMainBottom() != null) {
                applicationBean = Gson().fromJson(getEnglishMainBottom(), ApplicationBean::class.java)
            }
        }
        if (mHomeBean != null) {
            tv_box_name.text = mHomeBean?.result?.roomInfo?.hotelId?.name
            tv_box_ename.text = mHomeBean?.result?.roomInfo?.hotelId?.enName
            tv_box_ratingbar.visibility = View.VISIBLE
            setRatingBar(false, mHomeBean?.result?.roomInfo?.hotelId?.level?.toFloat()!!)
            marquestextview.text = mHomeBean?.result?.title

            tv_box_wifi_name.text = mHomeBean?.result?.roomInfo?.wifiName
            tv_box_password.text = mHomeBean?.result?.roomInfo?.wifiPassword
            for ((index, value) in mHomeBean?.result?.categorys2?.withIndex()!!) {
                if (value != null) {
                    when (mHomeBean?.result?.categorys2!![index].position) {
                        "1" -> {//左上
                            positionType = index
//                            loadImage(mContext, BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!, tv_box_img_travel)
                            Glide.with(mActivity).load(BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!).transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_travel)
                            tv_box_text_travel.text = mHomeBean?.result?.categorys2!![index].name
                            travelId = mHomeBean?.result?.categorys2!![index].id!!
                        }
                        "2" -> {//左下
                            positionType1 = index
//                            loadImage(mContext, BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!, tv_box_img_cate)
                            Glide.with(mActivity).load(BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!).transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_cate)
                            tv_box_text_cate.text = mHomeBean?.result?.categorys2!![index].name
                            cateId = mHomeBean?.result?.categorys2!![index].id!!
                        }
                        "3" -> {//中间
                            positionType2 = index
                            var listImages = ArrayList<String>()
                            listImages.add(BASE_URL + mHomeBean?.result?.categorys2!![index].picture)
                            listImages.add(BASE_URL + mHomeBean?.result?.categorys2!![index].picture1)
                            listImages.add(BASE_URL + mHomeBean?.result?.categorys2!![index].picture2)
                            val mList = ArrayList<BannerInfoBean>()
                            for (i in listImages.indices) {
                                mList.add(BannerInfoBean(i, "", listImages.get(i)))
                            }
                            banner_vp.setDelay(5000)
                            banner_vp.setData(mList, mAdCycleViewListener)
                            centrelId = mHomeBean?.result?.categorys2!![index].id!!
                        }
                        "4" -> {//右上
                            positionType3 = index
                            Glide.with(mActivity).load(BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!).transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_social)
//                            loadImage(mContext, BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!, tv_box_img_social)
                            tv_box_text_social.text = mHomeBean?.result?.categorys2!![index].name
                            socialId = mHomeBean?.result?.categorys2!![index].id!!
                        }
                        "5" -> {//右下
                            positionType4 = index
                            Glide.with(mActivity).load(BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!).transform(CenterCrop(mActivity), GlideRoundTransform(mActivity, 8)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(tv_box_img_shopping)
//                            loadImage(mContext, BASE_URL + mHomeBean?.result?.categorys2!![index].picture!!, tv_box_img_shopping)
                            tv_box_text_shopping.text = mHomeBean?.result?.categorys2!![index].name
                            shoppingId = mHomeBean?.result?.categorys2!![index].id!!
                        }
                        "6" -> {
                            serviceId = mHomeBean?.result?.categorys2!![index].id!!
                        }
                        else -> {

                        }
                    }
                }
            }
        } else if (getWelcome() != null) {
            var mWelcomeBean = Gson().fromJson(getWelcome(), WelcomeBean::class.java)
            if (mWelcomeBean != null) {
                tv_box_wifi_name.text = mWelcomeBean?.result?.wifi
                tv_box_password.text = mWelcomeBean?.result?.password
            }
        }
        if (applicationBean != null) {
            if (applicationBean!!.result!!.applica!!.size > 0) {
                tv_box_channel.visibility = View.GONE//电影轮播
                tv_box_movie_world.visibility = View.GONE//电影世界
                tv_box_hotel_service.visibility = View.GONE//酒店服务
                tv_box_live.visibility = View.GONE//直播频道
                tv_box_local_app.visibility = View.GONE//本地应用
                tv_box_skittles.visibility = View.GONE //吃喝玩乐
                when (applicationBean!!.result!!.applica!!.size) {
                    0 -> {
                    }
                    1 -> {
                        tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                        tv_box_channel.visibility = View.VISIBLE
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                        if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                            type1 = 0
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                            type1 = 1
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                            type1 = 2
                        } else {
                            type1 = 3
                        }
                    }
                    2 -> {
                        tv_box_channel.visibility = View.VISIBLE//电影轮播
                        tv_box_movie_world.visibility = View.VISIBLE//电影世界
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                        tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                        tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                        if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                            type1 = 0
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                            type1 = 1
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                            type1 = 2
                        } else {
                            type1 = 3
                        }
                        if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                            type2 = 0
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                            type2 = 1
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                            type2 = 2
                        } else {
                            type2 = 3
                        }
                    }
                    3 -> {
                        tv_box_channel.visibility = View.VISIBLE//电影轮播
                        tv_box_movie_world.visibility = View.VISIBLE//电影世界
                        tv_box_hotel_service.visibility = View.VISIBLE//直播频道
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                        tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                        tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                        tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                        if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                            type1 = 0
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                            type1 = 1
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                            type1 = 2
                        } else {
                            type1 = 3
                        }
                        if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                            type2 = 0
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                            type2 = 1
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                            type2 = 2
                        } else {
                            type2 = 3
                        }
                        if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                            type3 = 0
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                            type3 = 1
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                            type3 = 2
                        } else {
                            type3 = 3
                        }
                    }
                    4 -> {
                        tv_box_channel.visibility = View.VISIBLE//电影轮播
                        tv_box_movie_world.visibility = View.VISIBLE//电影世界
                        tv_box_live.visibility = View.VISIBLE//直播频道
                        tv_box_hotel_service.visibility = View.VISIBLE//酒店服务
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![3].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_live_img)

                        tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                        tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                        tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                        tv_box_live_name.text = applicationBean!!.result!!.applica!![3].appname
                        if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                            type1 = 0
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                            type1 = 1
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                            type1 = 2
                        } else {
                            type1 = 3
                        }
                        if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                            type2 = 0
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                            type2 = 1
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                            type2 = 2
                        } else {
                            type2 = 3
                        }
                        if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                            type3 = 0
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                            type3 = 1
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                            type3 = 2
                        } else {
                            type3 = 3
                        }
                        if (applicationBean!!.result!!.applica!![3].type.equals("0")) {
                            type4 = 0
                        } else if (applicationBean!!.result!!.applica!![3].type.equals("1")) {
                            type4 = 1
                        } else if (applicationBean!!.result!!.applica!![3].type.equals("2")) {
                            type4 = 2
                        } else {
                            type4 = 3
                        }
                    }
                    5 -> {
                        tv_box_channel.visibility = View.VISIBLE//电影轮播
                        tv_box_movie_world.visibility = View.VISIBLE//电影世界
                        tv_box_live.visibility = View.VISIBLE//直播频道
                        tv_box_hotel_service.visibility = View.VISIBLE//酒店服务
                        tv_box_local_app.visibility = View.VISIBLE//本地应用
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![3].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_live_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![4].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_local_app_img)
                        tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                        tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                        tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                        tv_box_live_name.text = applicationBean!!.result!!.applica!![3].appname
                        tv_box_local_app_name.text = applicationBean!!.result!!.applica!![4].appname
                        if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                            type1 = 0
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                            type1 = 1
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                            type1 = 2
                        } else {
                            type1 = 3
                        }
                        if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                            type2 = 0
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                            type2 = 1
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                            type2 = 2
                        } else {
                            type2 = 3
                        }
                        if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                            type3 = 0
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                            type3 = 1
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                            type3 = 2
                        } else {
                            type3 = 3
                        }
                        if (applicationBean!!.result!!.applica!![3].type.equals("0")) {
                            type4 = 0
                        } else if (applicationBean!!.result!!.applica!![3].type.equals("1")) {
                            type4 = 1
                        } else if (applicationBean!!.result!!.applica!![3].type.equals("2")) {
                            type4 = 2
                        } else {
                            type4 = 3

                        }
                        if (applicationBean!!.result!!.applica!![4].type.equals("0")) {
                            type5 = 0
                        } else if (applicationBean!!.result!!.applica!![4].type.equals("1")) {
                            type5 = 1
                        } else if (applicationBean!!.result!!.applica!![4].type.equals("2")) {
                            type5 = 2
                        } else {
                            type5 = 3
                        }
                    }
                    6 -> {
                        tv_box_channel.visibility = View.VISIBLE//电影轮播
                        tv_box_movie_world.visibility = View.VISIBLE//电影世界
                        tv_box_live.visibility = View.VISIBLE//直播频道
                        tv_box_hotel_service.visibility = View.VISIBLE//酒店服务
                        tv_box_local_app.visibility = View.VISIBLE//本地应用
                        tv_box_skittles.visibility = View.VISIBLE //吃喝玩乐
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![0].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_channel_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![1].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_movie_world_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![2].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_hotel_service_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![3].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_live_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![4].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_local_app_img)
                        Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![5].appurl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(tv_box_skittles_img)
                        tv_box_channel_name.text = applicationBean!!.result!!.applica!![0].appname
                        tv_box_movie_world_name.text = applicationBean!!.result!!.applica!![1].appname
                        tv_box_hotel_service_name.text = applicationBean!!.result!!.applica!![2].appname
                        tv_box_live_name.text = applicationBean!!.result!!.applica!![3].appname
                        tv_box_local_app_name.text = applicationBean!!.result!!.applica!![4].appname
                        tv_box_skittles_name.text = applicationBean!!.result!!.applica!![5].appname
                        if (applicationBean!!.result!!.applica!![0].type.equals("0")) {
                            type1 = 0
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("1")) {
                            type1 = 1
                        } else if (applicationBean!!.result!!.applica!![0].type.equals("2")) {
                            type1 = 2
                        } else {
                            type1 = 3
                        }
                        if (applicationBean!!.result!!.applica!![1].type.equals("0")) {
                            type2 = 0
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("1")) {
                            type2 = 1
                        } else if (applicationBean!!.result!!.applica!![1].type.equals("2")) {
                            type2 = 2
                        } else {
                            type2 = 3
                        }
                        if (applicationBean!!.result!!.applica!![2].type.equals("0")) {
                            type3 = 0
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("1")) {
                            type3 = 1
                        } else if (applicationBean!!.result!!.applica!![2].type.equals("2")) {
                            type3 = 2
                        } else {
                            type3 = 3
                        }
                        if (applicationBean!!.result!!.applica!![3].type.equals("0")) {
                            type4 = 0
                        } else if (applicationBean!!.result!!.applica!![3].type.equals("1")) {
                            type4 = 1
                        } else if (applicationBean!!.result!!.applica!![3].type.equals("2")) {
                            type4 = 2
                        } else {
                            type4 = 3
                        }
                        if (applicationBean!!.result!!.applica!![4].type.equals("0")) {
                            type5 = 0
                        } else if (applicationBean!!.result!!.applica!![4].type.equals("1")) {
                            type5 = 1
                        } else if (applicationBean!!.result!!.applica!![4].type.equals("2")) {
                            type5 = 2
                        } else {
                            type5 = 3
                        }
                        if (applicationBean!!.result!!.applica!![5].type.equals("0")) {
                            type6 = 0
                        } else if (applicationBean!!.result!!.applica!![5].type.equals("1")) {
                            type6 = 1
                        } else if (applicationBean!!.result!!.applica!![5].type.equals("2")) {
                            type6 = 2
                        } else {
                            type6 = 3
                        }
                    }
                    else -> {
                        tv_box_channel.visibility = View.GONE//电影轮播
                        tv_box_movie_world.visibility = View.GONE//电影世界
                        tv_box_live.visibility = View.GONE//直播频道
                        tv_box_hotel_service.visibility = View.GONE//酒店服务
                        tv_box_local_app.visibility = View.GONE//本地应用
                        tv_box_skittles.visibility = View.GONE //吃喝玩乐
                    }
                }

//            for (i in applicationBean!!.result!!.applica!!.indices) {//0吃喝玩乐1本地应用2酒店服务3执行代码
//                if (applicationBean!!.result!!.applica!![i].type!!.equals("0")) {
//                    positionSkittles = i
//                    tv_box_skittles.visibility = View.VISIBLE
//                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![i].appurl).into(tv_box_skittles_img)
//                } else if (applicationBean!!.result!!.applica!![i].type!!.equals("1")) {
//                    positionApp = i
//                    tv_box_local_app.visibility = View.VISIBLE
//                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![i].appurl).into(tv_box_local_app_img)
//                } else if (applicationBean!!.result!!.applica!![i].type!!.equals("2")) {
//                    positionService = i
//                    tv_box_hotel_service.visibility = View.VISIBLE
//                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![i].appurl).into(tv_box_hotel_service_img)
//                } else if (applicationBean!!.result!!.applica!![i].type!!.equals("3")) {
//                    positionChannel = i
//                    tv_box_channel.visibility = View.VISIBLE
//                    Glide.with(mActivity).load(BASE_URL + applicationBean!!.result!!.applica!![i].appurl).into(tv_box_channel_img)
//                    tv_box_channel_name.text = applicationBean!!.result!!.applica!![i].appname
//                }
//            }

            } else {
                tv_box_channel.visibility = View.GONE
                tv_box_movie_world.visibility = View.GONE
                tv_box_hotel_service.visibility = View.GONE
                tv_box_live.visibility = View.GONE
                tv_box_local_app.visibility = View.GONE//本地应用
                tv_box_skittles.visibility = View.GONE //吃喝玩乐
            }
        }

    }

    override fun initData() {
        if (checkNetEnable(mContext)) {
            var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
            if (messageBean != null) {
                var type = "0"
                if (LANGUAGE_TYPE.equals("chinese")) {
                    type = "1"
                } else {
                    type = "0"
                }
//                mPresenter?.getIndexInfo("339209", "8888", "1")
//                mPresenter?.getApplication("339209", "8888", "1")
                mPresenter?.getIndexInfo(messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
                mPresenter?.getApplication(messageBean?.result?.diCode, messageBean?.result?.roomNum, type)
            } else {
                goToActivity(mActivity, MessageActivity::class.java)
            }
//            loadingDialog.showDialog(mContext!!, "正在加载中", false, null)
        } else {
            long_toast("请检查网络连接是否正常", mActivity)
            getSPData()
        }
    }

    override fun initEvent() {
        setFocusListener()
    }

    private fun setBackgroud(position: Int) {
//        tv_box_channel.setBackgroundResource(if (position == 0) R.mipmap.index_selecttab_bg else 0)
//        tv_box_movie_world.setBackgroundResource(if (position == 1) R.mipmap.index_selecttab_bg else 0)
//        tv_box_hotel_service.setBackgroundResource(if (position == 2) R.mipmap.index_selecttab_bg else 0)
//        tv_box_live.setBackgroundResource(if (position == 3) R.mipmap.index_selecttab_bg else 0)
//        tv_box_local_app.setBackgroundResource(if (position == 4) R.mipmap.index_selecttab_bg else 0)
//        tv_box_skittles.setBackgroundResource(if (position == 8) R.mipmap.index_selecttab_bg else 0)
        if (position == 0) {
            tv_box_channel.scaleY = 1.4f
            tv_box_channel.scaleX = 1.2f
            tv_box_channel.setBackgroundResource(R.mipmap.gold)
        } else {
            tv_box_channel.scaleY = 1f
            tv_box_channel.scaleX = 1f
            tv_box_channel.setBackgroundResource(0)
        }
        if (position == 1) {
            tv_box_movie_world.scaleX = 1.2f
            tv_box_movie_world.scaleY = 1.4f
            tv_box_movie_world.setBackgroundResource(R.mipmap.gold)
        } else {
            tv_box_movie_world.scaleX = 1f
            tv_box_movie_world.scaleY = 1f
            tv_box_movie_world.setBackgroundResource(0)
        }
        if (position == 2) {
            tv_box_hotel_service.scaleX = 1.2f
            tv_box_hotel_service.scaleY = 1.4f
            tv_box_hotel_service.setBackgroundResource(R.mipmap.gold)
        } else {
            tv_box_hotel_service.scaleX = 1f
            tv_box_hotel_service.scaleY = 1f
            tv_box_hotel_service.setBackgroundResource(0)
        }
        if (position == 3) {
            tv_box_live.scaleX = 1.2f
            tv_box_live.scaleY = 1.4f
            tv_box_live.setBackgroundResource(R.mipmap.gold)
        } else {
            tv_box_live.scaleX = 1f
            tv_box_live.scaleY = 1f
            tv_box_live.setBackgroundResource(0)
        }
        if (position == 4) {
            tv_box_local_app.scaleY = 1.4f
            tv_box_local_app.scaleX = 1.2f
            tv_box_local_app.setBackgroundResource(R.mipmap.gold)
        } else {
            tv_box_local_app.scaleY = 1f
            tv_box_local_app.scaleX = 1f
            tv_box_local_app.setBackgroundResource(0)
        }
        if (position == 8) {
            tv_box_skittles.scaleY = 1.4f
            tv_box_skittles.scaleX = 1.2f
            tv_box_skittles.setBackgroundResource(R.mipmap.gold)
        } else {
            tv_box_skittles.scaleY = 1f
            tv_box_skittles.scaleX = 1f
            tv_box_skittles.setBackgroundResource(0)
        }
    }

    private fun setRatingBar(isClickable: Boolean, star: Float) {
        tv_box_ratingbar.setClickable(isClickable)
        tv_box_ratingbar.setStar(star)
        tv_box_ratingbar.setStepSize(RatingBar.StepSize.Half)
        tv_box_ratingbar.setOnRatingChangeListener(object : RatingBar.OnRatingChangeListener {
            override fun onRatingChange(ratingCount: Float) {//点击星星变化后选中的个数

            }
        })
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

    override fun showLoadingDialog() {//弹出Dialog
    }

    override fun dismissLoadingDialog() {//取消Dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown()
        }
    }

    fun setGoToActivity(mId: String, type: String) {
        if (!mId.equals("0") && mId != null) {
            var bundle = Bundle()
            if (city != null && temperature != null && weather != null) {
                bundle.putString("id", mId)
                bundle.putString("city", city)
                bundle.putString("temperature", temperature)
                bundle.putString("weather", weather)
                bundle.putString("type", type)
                goToActivity(mActivity!!, ClassifyActivity::class.java, bundle)
            } else {
                bundle.putString("id", mId)
                bundle.putString("type", type)
                goToActivity(mActivity!!, ClassifyActivity::class.java, bundle)
            }
        } else {
            toast("当前没有该服务", mContext)
        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        when (p0?.id) {
            R.id.tv_box_channel_img -> setBackgroud(0)
            R.id.tv_box_movie_world_img -> setBackgroud(1)
            R.id.tv_box_hotel_service_img -> setBackgroud(2)
            R.id.tv_box_live_img -> setBackgroud(3)
            R.id.tv_box_local_app_img -> setBackgroud(4)
            R.id.tv_box_skittles_img -> setBackgroud(8)
            R.id.tv_box_help -> {
                if (p1) {
                    setBackgroud(-1)
                    tv_box_help.scaleY = 1.3f
                    tv_box_help.scaleX = 1.3f
                    tv_box_help.setBackgroundResource(R.drawable.text_gold_radius)
//                    tv_box_help.setBackgroundResource(R.mipmap.welcome_help_bg)
                } else {
                    tv_box_help.scaleY = 1f
                    tv_box_help.scaleX = 1f
                    tv_box_help.setBackgroundResource(R.drawable.text_small_radius)
//                    tv_box_help.setBackgroundResource(R.mipmap.index_selecttab_bg)
                }
            }
            R.id.tv_box_img_travel -> {
                if (p1) {
                    setBackgroud(-1)
//                    tv_box_travel.setBackgroundResource(R.mipmap.new_gold)
                    tv_box_travel.setBackgroundResource(R.drawable.text_gold_radius)
//                    tv_box_travel.setBackgroundResource(R.drawable.empty_shape_gallery_select)
//                    tv_box_travel.setBackgroundResource(R.mipmap.hotel_service_tab_bg)
                } else {
                    tv_box_travel.setBackgroundResource(0)
                }
            }
            R.id.tv_box_img_cate -> {
                if (p1) {
                    setBackgroud(-1)
                    tv_box_cate.setBackgroundResource(R.drawable.text_gold_radius)
//                    tv_box_cate.setBackgroundResource(R.mipmap.hotel_service_tab_bg)
                } else {
                    tv_box_cate.setBackgroundResource(0)
                }
            }
//            R.id.tv_box_centre_img -> {
//                if (p1) {
//                    setBackgroud(-1)
//                    tv_box_centre.setBackgroundResource(R.mipmap.hotel_service_tab_bg)
//                } else {
//                    tv_box_centre.setBackgroundResource(0)
//                }
//            }
            R.id.tv_box_img_social -> {//右上
                if (p1) {
                    setBackgroud(-1)
                    tv_box_social.setBackgroundResource(R.drawable.text_gold_radius)
//                    tv_box_social.setBackgroundResource(R.mipmap.hotel_service_tab_bg)
                } else {
                    tv_box_social.setBackgroundResource(0)
                }
            }
            R.id.tv_box_img_shopping -> {
                if (p1) {
                    setBackgroud(-1)
                    tv_box_shopping.setBackgroundResource(R.drawable.text_gold_radius)
//                    tv_box_shopping.setBackgroundResource(R.mipmap.hotel_service_tab_bg)
                } else {
                    tv_box_shopping.setBackgroundResource(0)
                }
            }
            else -> {

            }

        }
    }

    /**
     * 焦点监听
     */
    fun setFocusListener() {
        tv_box_channel_img.setOnFocusChangeListener(this)
        tv_box_movie_world_img.setOnFocusChangeListener(this)
        tv_box_hotel_service_img.setOnFocusChangeListener(this)
        tv_box_live_img.setOnFocusChangeListener(this)
        tv_box_local_app_img.setOnFocusChangeListener(this)
        tv_box_skittles_img.setOnFocusChangeListener(this)
        tv_box_help.setOnFocusChangeListener(this)
        tv_box_img_travel.setOnFocusChangeListener(this)
        tv_box_img_cate.setOnFocusChangeListener(this)
//        tv_box_centre_img.setOnFocusChangeListener(this)
        tv_box_img_social.setOnFocusChangeListener(this)
        tv_box_img_shopping.setOnFocusChangeListener(this)
//        banner.setOnFocusChangeListener(this)

        tv_box_channel_img.setOnClickListener(this)
        tv_box_movie_world_img.setOnClickListener(this)
        tv_box_hotel_service_img.setOnClickListener(this)
        tv_box_live_img.setOnClickListener(this)
        tv_box_local_app_img.setOnClickListener(this)
        tv_box_skittles_img.setOnClickListener(this)
        tv_box_help.setOnClickListener(this)
        tv_box_img_travel.setOnClickListener(this)
        tv_box_img_cate.setOnClickListener(this)
//        tv_box_centre_img.setOnClickListener(this)
        tv_box_img_social.setOnClickListener(this)
        tv_box_img_shopping.setOnClickListener(this)
    }

    fun openOtherAppPage(packageName: String, pageName: String) {
        val intent = Intent()
        intent.component = ComponentName(packageName, pageName)
        mActivity!!.startActivity(intent)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_box_channel_img -> {
                positionChannel = 0
                setBackgroud(0)
                if (checkNetEnable(mActivity)) {
                    if (type1 == 3) {
                        if (isInstallApp(packageManager, applicationBean!!.result!!.applica!![positionChannel].codename!!)) {
                            if (!TextUtils.isEmpty(applicationBean!!.result!!.applica!![positionChannel].jumpPage)) {
                                openOtherAppPage(applicationBean!!.result!!.applica!![positionChannel].codename!!, applicationBean!!.result!!.applica!![positionChannel].jumpPage!!)
                            } else {
                                openOthemApp(mActivity, applicationBean!!.result!!.applica!![positionChannel].codename!!)
                            }
                        } else {
                            long_toast("没有安装该应用", mContext)
                        }
                    } else if (type1 == 1) {
                        var bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("temperature", temperature)
                        bundle.putString("weather", weather)
                        goToActivity(mActivity!!, NativeAppActivity::class.java, bundle)
                    } else {
                        if (applicationBean?.result?.applica != null) {
                            setGoToActivity(applicationBean!!.result!!.applica!![positionChannel].codename!!, "")
                        }
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
            R.id.tv_box_movie_world_img -> {
                positionChannel = 1
                if (checkNetEnable(mActivity)) {
                    if (type2 == 3) {
                        if (isInstallApp(packageManager, applicationBean!!.result!!.applica!![positionChannel].codename!!)) {
                            if (!TextUtils.isEmpty(applicationBean!!.result!!.applica!![positionChannel].jumpPage)) {
                                openOtherAppPage(applicationBean!!.result!!.applica!![positionChannel].codename!!, applicationBean!!.result!!.applica!![positionChannel].jumpPage!!)
                            } else {
                                openOthemApp(mActivity, applicationBean!!.result!!.applica!![positionChannel].codename!!)
                            }
                        } else {
                            long_toast("没有安装该应用", mContext)
                        }
                    } else if (type2 == 1) {
                        var bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("temperature", temperature)
                        bundle.putString("weather", weather)
                        goToActivity(mActivity!!, NativeAppActivity::class.java, bundle)
                    } else {
                        if (applicationBean?.result?.applica != null) {
                            setGoToActivity(applicationBean!!.result!!.applica!![positionChannel].codename!!, "")
                        }
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
//            R.id.tv_box_hotel_service_img -> {
//                if (mHomeBean?.result?.categorys1 != null) {
//                    for ((index, value) in mHomeBean?.result?.categorys1!!.withIndex()) {
//                        if ("酒店服务".equals(value.name)) {
//                            setGoToActivity(mHomeBean?.result?.categorys1?.get(index)?.id.toString(), "中间")
//                        }
//                    }
//                }
//            }
            R.id.tv_box_hotel_service_img -> {
                positionChannel = 2
                if (checkNetEnable(mActivity)) {
                    if (type3 == 3) {
                        if (isInstallApp(packageManager, applicationBean!!.result!!.applica!![positionChannel].codename!!)) {
                            if (!TextUtils.isEmpty(applicationBean!!.result!!.applica!![positionChannel].jumpPage)) {
                                openOtherAppPage(applicationBean!!.result!!.applica!![positionChannel].codename!!, applicationBean!!.result!!.applica!![positionChannel].jumpPage!!)
                            } else {
                                openOthemApp(mActivity, applicationBean!!.result!!.applica!![positionChannel].codename!!)
                            }
                        } else {
                            long_toast("没有安装该应用", mContext)
                        }
                    } else if (type3 == 1) {
                        var bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("temperature", temperature)
                        bundle.putString("weather", weather)
                        goToActivity(mActivity!!, NativeAppActivity::class.java, bundle)
                    } else {
                        if (applicationBean?.result?.applica != null) {
                            setGoToActivity(applicationBean!!.result!!.applica!![positionChannel].codename!!, "")
                        }
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }

            }
            R.id.tv_box_live_img -> {
                positionChannel = 3
                if (checkNetEnable(mActivity)) {
                    if (type4 == 3) {
                        if (isInstallApp(packageManager, applicationBean!!.result!!.applica!![positionChannel].codename!!)) {
                            if (!TextUtils.isEmpty(applicationBean!!.result!!.applica!![positionChannel].jumpPage)) {
                                openOtherAppPage(applicationBean!!.result!!.applica!![positionChannel].codename!!, applicationBean!!.result!!.applica!![positionChannel].jumpPage!!)
                            } else {
                                openOthemApp(mActivity, applicationBean!!.result!!.applica!![positionChannel].codename!!)
                            }
                        } else {
                            long_toast("没有安装该应用", mContext)
                        }
                    } else if (type4 == 1) {
                        var bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("temperature", temperature)
                        bundle.putString("weather", weather)
                        goToActivity(mActivity!!, NativeAppActivity::class.java, bundle)
                    } else {
                        if (applicationBean?.result?.applica != null) {
                            setGoToActivity(applicationBean!!.result!!.applica!![positionChannel].codename!!, "")
                        }
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
            R.id.tv_box_local_app_img -> {
                positionChannel = 4
                if (checkNetEnable(mActivity)) {
                    if (type5 == 3) {
                        if (isInstallApp(packageManager, applicationBean!!.result!!.applica!![positionChannel].codename!!)) {
                            if (!TextUtils.isEmpty(applicationBean!!.result!!.applica!![positionChannel].jumpPage)) {
                                openOtherAppPage(applicationBean!!.result!!.applica!![positionChannel].codename!!, applicationBean!!.result!!.applica!![positionChannel].jumpPage!!)
                            } else {
                                openOthemApp(mActivity, applicationBean!!.result!!.applica!![positionChannel].codename!!)
                            }
                        } else {
                            long_toast("没有安装该应用", mContext)
                        }
                    } else if (type5 == 1) {
                        var bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("temperature", temperature)
                        bundle.putString("weather", weather)
                        goToActivity(mActivity!!, NativeAppActivity::class.java, bundle)
                    } else {
                        if (applicationBean?.result?.applica != null) {
                            setGoToActivity(applicationBean!!.result!!.applica!![positionChannel].codename!!, "")
                        }
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
            R.id.tv_box_skittles_img -> {
                positionChannel = 5
                if (checkNetEnable(mActivity)) {
                    if (type6 == 3) {
                        if (isInstallApp(packageManager, applicationBean!!.result!!.applica!![positionChannel].codename!!)) {
                            if (!TextUtils.isEmpty(applicationBean!!.result!!.applica!![positionChannel].jumpPage)) {
                                openOtherAppPage(applicationBean!!.result!!.applica!![positionChannel].codename!!, applicationBean!!.result!!.applica!![positionChannel].jumpPage!!)
                            } else {
                                openOthemApp(mActivity, applicationBean!!.result!!.applica!![positionChannel].codename!!)
                            }
                        } else {
                            long_toast("没有安装该应用", mContext)
                        }
                    } else if (type6 == 1) {
                        var bundle = Bundle()
                        bundle.putString("city", city)
                        bundle.putString("temperature", temperature)
                        bundle.putString("weather", weather)
                        goToActivity(mActivity!!, NativeAppActivity::class.java, bundle)
                    } else {
                        if (applicationBean?.result?.applica != null) {
                            setGoToActivity(applicationBean!!.result!!.applica!![positionChannel].codename!!, "")
                        }
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
            R.id.tv_box_help -> {
                var intent: Intent = Intent(mActivity, HelpActivity::class.java)
                var bundle = Bundle()
                bundle.putString("city", city)
                bundle.putString("temperature", temperature)
                bundle.putString("weather", weather)
                bundle.putString("wifiName", tv_box_wifi_name.text.toString())
                bundle.putString("wifiPassword", tv_box_password.text.toString())
                intent.putExtras(bundle)
                mActivity?.startActivity(intent)
//                goToActivity(mActivity!!, HelpActivity::class.java)
            }
            R.id.tv_box_img_travel -> {
                if (checkNetEnable(mActivity)) {
                    if (positionType >= 0) {
                        if (mHomeBean!!.result!!.categorys2!![positionType].type!!.equals("3")) {
                            if (isInstallApp(packageManager, mHomeBean!!.result!!.categorys2!![positionType].cnamcode!!)) {
                                if (!TextUtils.isEmpty(mHomeBean!!.result!!.categorys2!![positionType4].jumpPage)) {
                                    openOtherAppPage(mHomeBean!!.result!!.categorys2!![positionType4].cnamcode!!, mHomeBean!!.result!!.categorys2!![positionType4].jumpPage!!)
                                } else {
                                    openOthemApp(mActivity, mHomeBean!!.result!!.categorys2!![positionType].cnamcode!!)
                                }
                            } else {
                                long_toast("没有安装该应用", mContext)
                            }
                        } else {
                            setGoToActivity(travelId.toString(), "左上")
                        }
                    } else {
                        Toast.makeText(mActivity, "没有该条数据", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
            R.id.tv_box_img_cate -> {
                if (checkNetEnable(mActivity)) {
                    if (positionType1 >= 0) {
                        if (mHomeBean!!.result!!.categorys2!![positionType1].type!!.equals("3")) {
                            if (isInstallApp(packageManager, mHomeBean!!.result!!.categorys2!![positionType].cnamcode!!)) {
                                if (!TextUtils.isEmpty(mHomeBean!!.result!!.categorys2!![positionType4].jumpPage)) {
                                    openOtherAppPage(mHomeBean!!.result!!.categorys2!![positionType4].cnamcode!!, mHomeBean!!.result!!.categorys2!![positionType4].jumpPage!!)
                                } else {
                                    openOthemApp(mActivity, mHomeBean!!.result!!.categorys2!![positionType1].cnamcode!!)
                                }
                            } else {
                                long_toast("没有安装该应用", mContext)
                            }
                        } else {
                            setGoToActivity(cateId.toString(), "左下")
                        }
                    } else {
                        Toast.makeText(mActivity, "没有该条数据", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
//            R.id.tv_box_centre_img -> {
//                setGoToActivity(centrelId.toString(), "中间")
//            }
            R.id.tv_box_img_social -> {//右上
                if (checkNetEnable(mActivity)) {
                    if (positionType3 >= 0) {
                        if (mHomeBean!!.result!!.categorys2!![positionType3].type!!.equals("3")) {
                            if (isInstallApp(packageManager, mHomeBean!!.result!!.categorys2!![positionType3].cnamcode!!)) {
                                if (!TextUtils.isEmpty(mHomeBean!!.result!!.categorys2!![positionType4].jumpPage)) {
                                    openOtherAppPage(mHomeBean!!.result!!.categorys2!![positionType4].cnamcode!!, mHomeBean!!.result!!.categorys2!![positionType4].jumpPage!!)
                                } else {
                                    openOthemApp(mActivity, mHomeBean!!.result!!.categorys2!![positionType3].cnamcode!!)
                                }
                            } else {
                                long_toast("没有安装该应用", mContext)
                            }
                        } else {
                            setGoToActivity(socialId.toString(), "右上")
                        }
                    } else {
                        Toast.makeText(mActivity, "没有该条数据", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }
            }
            R.id.tv_box_img_shopping -> {//右下
                if (checkNetEnable(mActivity)) {
                    if (positionType4 >= 0) {
                        if (mHomeBean!!.result!!.categorys2!![positionType4].type!!.equals("3")) {
                            if (isInstallApp(packageManager, mHomeBean!!.result!!.categorys2!![positionType4].cnamcode!!)) {
                                if (!TextUtils.isEmpty(mHomeBean!!.result!!.categorys2!![positionType4].jumpPage)) {
                                    openOtherAppPage(mHomeBean!!.result!!.categorys2!![positionType4].cnamcode!!, mHomeBean!!.result!!.categorys2!![positionType4].jumpPage!!)
                                } else {
                                    openOthemApp(mActivity, mHomeBean!!.result!!.categorys2!![positionType4].cnamcode!!)
                                }
                            } else {
                                long_toast("没有安装该应用", mContext)
                            }
                        } else {
                            setGoToActivity(shoppingId.toString(), "右下")
                        }
                    } else {
                        Toast.makeText(mActivity, "没有该条数据", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    goToActivity(mActivity, OpenLocalPlayerActivity::class.java)
                }

            }
            else -> {

            }
        }
    }
}


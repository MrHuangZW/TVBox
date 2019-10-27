package com.messcat.tvbox.module.home.activity

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.messcat.kotlin.mchttp.RetrofitServiceManager
import com.messcat.mclibrary.BASE_URL
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.mclibrary.goToActivity
import com.messcat.mclibrary.mchttp.uploadAPP.DownLoadService
import com.messcat.mclibrary.util.*
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.MessageBean
import com.messcat.tvbox.module.home.bean.PlayMovieBean
import com.messcat.tvbox.module.home.contract.PlayMovieContract
import com.messcat.tvbox.module.home.presenter.PlayMoviePresenter
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.os.Handler
import com.messcat.tvbox.module.home.utils.DownLoadUtils


/**
 * Created by Administrator on 2017/9/18 0018.
 */

class SplashActivity : MVPActivity<PlayMoviePresenter>(), PlayMovieContract.View, DownLoadUtils.ILoadVideoListener {
    override fun loadFail() {
        if (getFilePath("TVBox", "tvbox.mp4") != null) {
            var bundler = Bundle()
            bundler.putString("url", getFilePath("TVBox", "tvbox.mp4"))
            goToActivity(mActivity, PlayMovieActivity::class.java, bundler)
            finishActivitys(mActivity)
        } else {
            goToActivity(mActivity, NewWelcomeActivity::class.java)
            finishActivitys(mActivity)
        }
    }

    override fun loadSuccess( url:String) {
        putString("url",url)
        var bundler = Bundle()
        bundler.putString("url", getFilePath("TVBox", "tvbox.mp4"))
        goToActivity(mActivity, PlayMovieActivity::class.java, bundler)
        finishActivitys(mActivity)
    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf("android.permission.READ_EXTERNAL_STORAGE"
            , "android.permission.WRITE_EXTERNAL_STORAGE"
            , "android.permission.ACCESS_WIFI_STATE"
            , "android.permission.CHANGE_WIFI_STATE"
            , "android.permission.ACCESS_FINE_LOCATION", "android.permission.MANAGE_USERS", "android.permission.CREATE_USERS"
            , "android.permission.ACCESS_COARSE_LOCATION")


    override fun showError(msg: String?) {
        goToActivity(mActivity, NewWelcomeActivity::class.java)
        finishActivitys(mActivity)
    }

    override fun showLoadingDialog() {

    }

    override fun dismissLoadingDialog() {

    }

    override fun getVideo(playMovie: PlayMovieBean?) {
        if (playMovie?.result != null) {
            if (getStrings("url", null) != null) {
                if (!getStrings("url", "").equals(BASE_URL + playMovie?.result?.vedio)) {

                    DownLoadUtils.getInstance(this).loadVideo(BASE_URL + playMovie?.result?.vedio, this)
                } else if (getFilePath("TVBox", "tvbox.mp4") != null) {
                    var bundler = Bundle()
                    bundler.putString("url", getFilePath("TVBox", "tvbox.mp4"))
                    goToActivity(mActivity, PlayMovieActivity::class.java, bundler)
                    finishActivitys(mActivity)
                }
            } else {
                DownLoadUtils.getInstance(this).loadVideo(BASE_URL + playMovie?.result?.vedio, this)
            }

        } else {
            goToActivity(mActivity, NewWelcomeActivity::class.java)
            finishActivitys(mActivity)
        }
    }

    override fun initPresenter(): PlayMoviePresenter = PlayMoviePresenter(this,
            PlayMoviePresenter.PlayMovieLoader(RetrofitServiceManager.instance.create(PlayMoviePresenter.PlayMovieHttp::class.java)))

    override fun getLayout(): Int = R.layout.activity_splash

    override fun initData() {

    }


    override fun initEvent() {
    }

    override fun initView() {
        Thread.sleep(3000)
        //检测是否有写的权限
        val permission = ActivityCompat.checkSelfPermission(mActivity!!,
                "android.permission.WRITE_EXTERNAL_STORAGE")
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(mActivity!!, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
        } else {
            checkVideo()
        }
    }

    fun checkVideo() {
        if (checkNetEnable(mContext)) {
            if (getMember() != null) {
                var messageBean = Gson().fromJson(getMember(), MessageBean::class.java)
                if (messageBean != null) {
                    mPresenter?.getVideo(messageBean?.result?.diCode, messageBean?.result?.roomNum)
                } else {
                    goToActivity(mActivity, MessageActivity::class.java)
                    finishActivitys(mActivity)
                }
            } else {
                goToActivity(mActivity, MessageActivity::class.java)
                finishActivitys(mActivity)
            }
        } else {
            if (getMember() != null) {
                if (getFilePath("TVBox", "tvbox.mp4") != null) {
                    var bundler = Bundle()
                    bundler.putString("url", getFilePath("TVBox", "tvbox.mp4"))
                    goToActivity(mActivity, PlayMovieActivity::class.java, bundler)
                    finishActivitys(mActivity)
                } else if (getFilePath("TVBox", "tvbox.mp4") == null && checkNetEnable(mContext)) {
                    remove("url")
                } else {
                    goToActivity(mActivity, NewWelcomeActivity::class.java)
                    finishActivitys(mActivity)
                }
            } else {
                goToActivity(mActivity, MessageActivity::class.java)
                finishActivitys(mActivity)
            }

        }
    }

    /**
     * fragment回调处理权限的结果
     * @param requestCode 请求码 要等于申请时候的请求码
     * @param permissions 申请的权限
     * @param grantResults 对应权限的处理结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkVideo()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

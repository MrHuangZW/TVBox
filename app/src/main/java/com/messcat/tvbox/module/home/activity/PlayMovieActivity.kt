package com.messcat.tvbox.module.home.activity

import android.media.MediaPlayer
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.RelativeLayout
import com.messcat.mclibrary.base.NotMVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.mclibrary.goToActivity
import com.messcat.tvbox.R
import kotlinx.android.synthetic.main.activity_playmovie.*
import java.io.File
import java.io.FileInputStream
import java.net.URISyntaxException


/**
 * Created by Administrator on 2017/9/18 0018.
 */
class PlayMovieActivity : NotMVPActivity() {

    internal var player: MediaPlayer? = null
    internal var holder: SurfaceHolder? = null
    internal var uri: String? = null
    private var mSurfaceViewWidth: Int = 0
    private var mSurfaceViewHeight: Int = 0
    private var isChange = true


    override fun getLayout(): Int = R.layout.activity_playmovie

    override fun initData() {

    }

    override fun initEvent() {
        player?.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            changeVideoSize()
            id_video_play_progress.setVisibility(View.GONE)
            player?.start()
        })

        player?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            //播放结束
            goToActivity(mActivity, NewWelcomeActivity::class.java)
            finishActivitys(mActivity)
        })
    }

    override fun initView() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        mSurfaceViewWidth = dm.widthPixels
        mSurfaceViewHeight = dm.heightPixels
        val bundle = intent.extras
        if (bundle != null) {
            uri = bundle.getString("url")
            holder = surfaceView.holder
            holder?.addCallback(MyCallBack())
        }
        player = MediaPlayer()
        player?.reset()
        if (uri != null && uri?.length!! > 0) {
            var file: File? = null   //图片地址
            try {
                file = File(uri)
                if (!uri!!.startsWith("http:")&&!uri!!.startsWith("https:")&&getFileSize(file) > 0) {
                    try {
                        player?.setDataSource(uri)
                        // 设置异步加载视频，包括两种方式  prepare()同步，prepareAsync()异步
                        player?.prepareAsync()
                    } catch (e: Exception) {
                        goToActivity(mActivity, NewWelcomeActivity::class.java)
                        finishActivitys(mActivity)
                    }
                } else {
                    goToActivity(mActivity, NewWelcomeActivity::class.java)
                    finishActivitys(mActivity)
                }
            } catch (e: URISyntaxException) {
                goToActivity(mActivity, NewWelcomeActivity::class.java)
                finishActivitys(mActivity)
                e.printStackTrace()
            }
        } else {
            goToActivity(mActivity, NewWelcomeActivity::class.java)
            finishActivitys(mActivity)
        }


    }


    /**
     * 获取指定文件大小
     * @param f
     * @return
     * @throws Exception 　　
     */
    @Throws(Exception::class)
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            var fis: FileInputStream? = null
            fis = FileInputStream(file)
            size = fis!!.available().toLong()
        } else {
            file.createNewFile()
        }
        return size
    }


    inner class MyCallBack : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            player?.setDisplay(holder)
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            if (null != player) {
                player?.release()
                player = null
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        goToActivity(mActivity, NewWelcomeActivity::class.java)
        finishActivitys(mActivity)
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 改变视频的显示大小，全屏，窗口，内容
     */
    fun changeVideoSize() {

        if (!isChange) {
            return
        }

        isChange = false
        // 改变视频大小

        // 获取视频的宽度和高度
        var width = player?.getVideoWidth()
        var height = player?.getVideoHeight()
        // 如果按钮文字为窗口则设置为窗口模式

        /* mSurfaceViewWidth,mSurfaceViewHeight
            * 如果为全屏模式则改为适应内容的，前提是视频宽高小于屏幕宽高，如果大于宽高 我们要做缩放
            * 如果视频的宽高度有一方不满足我们就要进行缩放. 如果视频的大小都满足就直接设置并居中显示。
            */

        //            if (width > mSurfaceViewWidth || height > mSurfaceViewHeight) {
        // 计算出宽高的倍数
        //                float vWidth = (float) width / (float) mSurfaceViewWidth;
        //                float vHeight = (float) height / (float) mSurfaceViewHeight;
        //                // 获取最大的倍数值，按大数值进行缩放
        //                float max = Math.max(vWidth, vHeight);
        //                // 计算出缩放大小,取接近的正值
        //                width = (int) Math.ceil((float) width / max);
        //                height = (int) Math.ceil((float) height / max);

        var vWidth = 0f
        var vHeight = 0f

        if (width!! > height!!) {
            vWidth = mSurfaceViewWidth.toFloat()
            vHeight = (height * mSurfaceViewWidth / width).toFloat()
        } else if (width < height) {
            vHeight = mSurfaceViewHeight.toFloat()
            vWidth = (mSurfaceViewHeight * width / height).toFloat()
        } else {
            vWidth = mSurfaceViewWidth.toFloat()
            vHeight = mSurfaceViewWidth.toFloat()
        }

        width = vWidth.toInt()
        height = vHeight.toInt()
        val layoutParams = RelativeLayout.LayoutParams(width,
                height)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        surfaceView.layoutParams = layoutParams
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
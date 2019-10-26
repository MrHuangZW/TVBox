package com.messcat.mclibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.hengda.smart.jsyz.m.component.LoadingDialog
import com.messcat.kotlin.utils.LoadingDialogManager
import com.messcat.mclibrary.R
import com.messcat.mclibrary.util.GlideRoundTransform
import java.util.concurrent.Executors

/**
 * Created by Administrator on 2017/8/30 0030.
 */
abstract class BaseFragment : Fragment(), LoadingDialogManager {

    var mContext: Activity? = null//上下文
    var mView: View? = null//View
    var isLoad = false//是否显示
    var msgKey1: Int = 1//倒计时发送Handle
    var scheduledThreadPool = Executors.newScheduledThreadPool(1)//线程池 比较稳定

    override val loadingDialog by lazy { LoadingDialog(activity) }//赖初始化LoadingDialog

    override fun onAttach(context: Context?) {
        mContext = activity
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = getLayoutId()
        if (layoutId > 0)
            mView = inflater?.inflate(layoutId, null)
        return mView
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {  // 在onCreateView（）执行后立马执行
        super.onViewCreated(view, savedInstanceState)
//        initView()
//        initData()
//        initEvent()
    }

    /**
     * 处理预加载
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (!isLoad) {
                //当前fragment可见，执行onVisible
                onVisible()
            } else {
                onInVisible()
            }
        }
    }

    /**
     * 隐藏的时候调用
     */
    protected fun onInVisible() {
        setUserGone()
    }

    /**
     * 显示的时候调用
     */
    protected fun onVisible() {
        if (isLoad) {
            return
        } else {
            initData()
            isLoad = true
        }
    }

    /**
     * 得到轮播图的View
     * @param context
     * @param url
     * @return
     */
    companion object {
        fun getImageView(context: Context, url: String): View {
            val rl = RelativeLayout(context)
            //添加一个ImageView，并加载图片
            val imageView = ImageView(context)
            val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.layoutParams = layoutParams
            //使用Glide来加载图片
            Glide.with(context).load(url).transform(CenterCrop(context), GlideRoundTransform(context, 8)).diskCacheStrategy(DiskCacheStrategy.RESULT).priority(Priority.HIGH).into(imageView)
//            Glide.with(context).load(url).placeholder(R.mipmap.icon_moren).centerCrop().into(imageView)
            //在Imageview前添加一个半透明的黑色背景，防止文字和图片混在一起
            val backGround = ImageView(context)
            backGround.layoutParams = layoutParams
            backGround.setBackgroundResource(R.color.banner_background)
            rl.addView(imageView)
            // rl.addView(backGround);
            return rl
        }
    }

    /**
     * 加载XML布局
     */
    protected abstract fun getLayoutId(): Int

    //界面隐藏
    abstract fun setUserGone()

    //初始化
    protected abstract fun initView()

    //点击事件
    protected abstract fun initEvent()

    //加载数据
    protected abstract fun initData()
}
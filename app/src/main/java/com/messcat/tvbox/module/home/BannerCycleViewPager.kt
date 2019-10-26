package com.messcat.tvbox.module.home

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.messcat.mclibrary.base.BaseFragment
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.BannerInfoBean
import java.util.ArrayList

/**
 *author:Bandele
 *date:2018/9/20 0020$
 *describe:
 */

class BannerCycleViewPager @JvmOverloads constructor(private val mContext: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(mContext, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    private var mViewPager: ViewPager? = null//实现轮播图的ViewPager

    private var mTitle: TextView? = null//标题

    private var mIndicatorLayout: LinearLayout? = null // 指示器

    private var myHandler: Handler? = null//每几秒后执行下一张的切换

    private val WHEEL = 100 // 转动

    private val WHEEL_WAIT = 101 // 等待

    private val mViews = ArrayList<View>() //需要轮播的View，数量为轮播图数量+2

    private var mIndicators: Array<ImageView?>? = null    //指示器小圆点


    private var isScrolling = false // 滚动框是否滚动着

    /**
     * 是否处于循环状态
     *
     * @return
     */
    /**
     * 是否循环，默认开启。必须在setData前调用
     *
     * @param isCycle 是否循环
     */
    var isCycle = true // 是否循环，默认为true

    private var isWheel = true // 是否轮播，默认为true

    private var delay = 4000 // 默认轮播时间

    private var mCurrentPosition = 0 // 轮播当前位置

    private var releaseTime: Long = 0 // 手指松开、页面不滚动时间，防止手机松开后短时间进行切换

    private var mAdapter: ViewPagerAdapter? = null

    private var mImageCycleViewListener: ImageCycleViewListener? = null

    private var infos: List<BannerInfoBean>? = null//数据集合

    private var mIndicatorSelected: Int = 0//指示器图片，被选择状态

    private var mIndicatorUnselected: Int = 0//指示器图片，未被选择状态

    internal val runnable: Runnable = Runnable {
        if (mContext != null && isWheel) {
            val now = System.currentTimeMillis()
            // 检测上一次滑动时间与本次之间是否有触击(手滑动)操作，有的话等待下次轮播
            if (now - releaseTime > delay - 500) {
                myHandler!!.sendEmptyMessage(WHEEL)
            } else {
                myHandler!!.sendEmptyMessage(WHEEL_WAIT)
            }
        }
    }

    init {
        initView()
    }


    /**
     * 初始化View
     */
    private fun initView() {
        LayoutInflater.from(mContext).inflate(R.layout.banner_cycle_viewpager, this, true)
        mViewPager = findViewById<View>(R.id.cycle_view_pager) as ViewPager
        mTitle = findViewById<View>(R.id.cycle_title) as TextView
        mViewPager!!.isFocusable = false
        mIndicatorLayout = findViewById<View>(R.id.cycle_indicator) as LinearLayout
        myHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == WHEEL && mViews.size > 0) {
                    if (!isScrolling) {
                        //当前为非滚动状态，切换到下一页
                        val posttion = (mCurrentPosition + 1) % mViews.size
                        mViewPager!!.setCurrentItem(posttion, true)
                    }
                    releaseTime = System.currentTimeMillis()
                    myHandler!!.removeCallbacks(runnable)
                    myHandler!!.postDelayed(runnable, delay.toLong())
                    return

                }
                if (msg.what == WHEEL_WAIT && mViews.size > 0) {
                    myHandler!!.removeCallbacks(runnable)
                    myHandler!!.postDelayed(runnable, delay.toLong())
                }
            }
        }
    }

    /**
     * 设置指示器图片，在setData之前调用
     *
     * @param select   选中时的图片
     * @param unselect 未选中时的图片
     */
    fun setIndicators(select: Int, unselect: Int) {
        mIndicatorSelected = select
        mIndicatorUnselected = unselect
    }


    /**
     * 初始化viewpager
     *
     * @param list         要显示的数据
     * @param showPosition 默认显示位置
     */
    @JvmOverloads
    fun setData(list: List<BannerInfoBean>?, listener: ImageCycleViewListener, showPosition: Int = 0) {
        var showPosition = showPosition

        if (list == null || list.size == 0) {
            //没有数据时隐藏整个布局
            // this.setVisibility(View.GONE);
            this.visibility = View.INVISIBLE
            return
        }

        mViews.clear()
        infos = list

        if (isCycle) {
            //添加轮播图View，数量为集合数+2
            // 将最后一个View添加进来
            mViews.add(getImageView(mContext, infos!![infos!!.size - 1].url!!))
            for (a in infos!!.indices) {
                mViews.add(getImageView(mContext, infos!![a].url!!))
            }
            // 将第一个View添加进来
            mViews.add(getImageView(mContext, infos!![0].url!!))
        } else {
            //只添加对应数量的View
            for (i in infos!!.indices) {
                mViews.add(getImageView(mContext, infos!![i].url!!))
            }
        }


        if (mViews == null || mViews.size == 0) {
            //没有View时隐藏整个布局
            this.visibility = View.GONE
            return
        }

        mImageCycleViewListener = listener

        var ivSize = mViews.size

        // 设置指示器
        mIndicators = arrayOfNulls<ImageView>(ivSize!!)
        if (isCycle)
            mIndicators = arrayOfNulls(ivSize - 2)
        mIndicatorLayout!!.removeAllViews()
        for (i in mIndicators!!.indices) {
            mIndicators!![i] = ImageView(mContext)
            val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.setMargins(10, 0, 10, 0)
            mIndicators!![i]!!.layoutParams = lp
            mIndicatorLayout!!.addView(mIndicators!![i])
        }

        mAdapter = ViewPagerAdapter()

        // 默认指向第一项，下方viewPager.setCurrentItem将触发重新计算指示器指向
        setIndicator(0)

        mViewPager!!.offscreenPageLimit = 3
        mViewPager!!.setOnPageChangeListener(this)
        mViewPager!!.adapter = mAdapter
        if (showPosition < 0 || showPosition >= mViews.size)
            showPosition = 0
        if (isCycle) {
            showPosition = showPosition + 1
        }
        mViewPager!!.currentItem = showPosition

        setWheel(true)//设置轮播
    }

    /**
     * 获取轮播图View
     *
     * @param context
     * @param url
     */
    private fun getImageView(context: Context?, url: String): View {
        return BaseFragment.getImageView(context!!, url)
    }

//    fun getSecondImageView(context: Context, url: String): View {
//        val rl = RelativeLayout(context)
//        //添加一个ImageView，并加载图片
//        val imageView = ImageView(context)
//        val layoutParams = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        imageView.layoutParams = layoutParams
//        //使用Glide来加载图片
//        Glide.with(context).load(url).transform(CenterCrop(context), GlideRoundTransform(context, 20)).into(imageView)
////            Glide.with(context).load(url).placeholder(R.mipmap.icon_moren).centerCrop().into(imageView)
//        //在Imageview前添加一个半透明的黑色背景，防止文字和图片混在一起
//        val backGround = ImageView(context)
//        backGround.layoutParams = layoutParams
//        backGround.setBackgroundResource(com.messcat.mclibrary.R.color.banner_background)
//        rl.addView(imageView)
//        // rl.addView(backGround);
//        return rl
//    }

    /**
     * 设置指示器，和文字内容
     *
     * @param selectedPosition 默认指示器位置
     */
    private fun setIndicator(selectedPosition: Int) {
        setText(mTitle, infos!![selectedPosition].title)
        try {

            for (i in mIndicators!!.indices) {
                mIndicators!![i]!!
                        .setBackgroundResource(mIndicatorUnselected)
            }
            if (mIndicators!!.size > selectedPosition)
                mIndicators!![selectedPosition]!!.setBackgroundResource(mIndicatorSelected)
        } catch (e: Exception) {
            Log.i(TAG, "指示器路径不正确")
        }

    }


    /**
     * 页面适配器 返回对应的view
     *
     * @author Yuedong Li
     */
    private inner class ViewPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return mViews.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val v = mViews[position]
            if (mImageCycleViewListener != null) {
                v.setOnClickListener { v -> mImageCycleViewListener!!.onImageClick(infos!![mCurrentPosition - 1], mCurrentPosition, v) }
            }
            container.addView(v)
            return v
        }

        override fun getItemPosition(`object`: Any?): Int {
            return PagerAdapter.POSITION_NONE
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(arg0: Int) {
        val max = mViews.size - 1
        var position = arg0
        mCurrentPosition = arg0
        if (isCycle) {
            if (arg0 == 0) {

                //滚动到mView的1个（界面上的最后一个），将mCurrentPosition设置为max - 1
                mCurrentPosition = max - 1
            } else if (arg0 == max) {
                //滚动到mView的最后一个（界面上的第一个），将mCurrentPosition设置为1
                mCurrentPosition = 1
            }
            position = mCurrentPosition - 1
        }
        setIndicator(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == 1) { // viewPager在滚动
            isScrolling = true
            return
        } else if (state == 0) { // viewPager滚动结束

            releaseTime = System.currentTimeMillis()
            //跳转到第mCurrentPosition个页面（没有动画效果，实际效果页面上没变化）
            mViewPager!!.setCurrentItem(mCurrentPosition, false)

        }
        isScrolling = false
    }

    /**
     * 设置是否轮播，默认轮播,轮播一定是循环的
     *
     * @param isWheel
     */
    fun setWheel(isWheel: Boolean) {
        this.isWheel = isWheel
        isCycle = true
        if (isWheel) {
            myHandler!!.postDelayed(runnable, delay.toLong())
        }
    }


    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    fun refreshData() {
        if (mAdapter != null)
            mAdapter!!.notifyDataSetChanged()
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    fun isWheel(): Boolean {
        return isWheel
    }

    /**
     * 设置轮播暂停时间,单位毫秒（默认4000毫秒）
     * @param delay
     */
    fun setDelay(delay: Int) {
        this.delay = delay
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param info
         * @param position
         * @param imageView
         */
        fun onImageClick(info: BannerInfoBean, position: Int, imageView: View)
    }


    /**
     * 关闭轮播
     *
     * @param
     */
    fun stopWheel() {

        if (isWheel) {
            isWheel = false//关闭循环
            myHandler!!.removeCallbacksAndMessages(null)//关闭所有消息和任务
        }
    }

    companion object {

        private val TAG = "CycleViewPager"

        /**
         * 为textview设置文字
         *
         * @param textView
         * @param text
         */
        fun setText(textView: TextView?, text: String?) {
            if (text != null && textView != null) textView.text = text
        }

        /**
         * 为textview设置文字
         *
         * @param textView
         * @param text
         */
        fun setText(textView: TextView?, text: Int) {
            if (textView != null) setText(textView, text.toString() + "")
        }
    }

}

package com.messcat.tvbox.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.messcat.mclibrary.R
import java.math.BigDecimal

/**
 * 星级评分
 * Created by Administrator on 2017/8/22 0022.
 */

class RatingBar
/**
 * 构造函数
 * 获取xml中设置的资源文件
 *
 * @param context
 * @param attrs
 */
(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    /**
     * 是否可点击
     */
    private var mClickable: Boolean = false
    /**
     * 星星总数
     */
    private val starCount: Int
    /**
     * 星星的点击事件
     */
    private var onRatingChangeListener: OnRatingChangeListener? = null
    /**
     * 每个星星的大小
     */
    private var starImageSize: Float = 0.toFloat()
    /**
     * 每个星星的间距
     */
    private val starPadding: Float
    /**
     * 星星的显示数量，支持小数点
     */
    private var starStep: Float = 0.toFloat()
    /**
     * 空白的默认星星图片
     */
    private var starEmptyDrawable: Drawable? = null
    /**
     * 选中后的星星填充图片
     */
    private var starFillDrawable: Drawable? = null
    /**
     * 半颗星的图片
     */
    private var starHalfDrawable: Drawable? = null
    /**
     * 每次点击星星所增加的量是整个还是半个
     */
    private var stepSize: StepSize? = null

    /**
     * 设置半星的图片资源文件
     *
     * @param starHalfDrawable
     */
    fun setStarHalfDrawable(starHalfDrawable: Drawable) {
        this.starHalfDrawable = starHalfDrawable
    }

    /**
     * 设置满星的图片资源文件
     *
     * @param starFillDrawable
     */
    fun setStarFillDrawable(starFillDrawable: Drawable) {
        this.starFillDrawable = starFillDrawable
    }

    /**
     * 设置空白和默认的图片资源文件
     *
     * @param starEmptyDrawable
     */
    fun setStarEmptyDrawable(starEmptyDrawable: Drawable) {
        this.starEmptyDrawable = starEmptyDrawable
    }

    /**
     * 设置星星是否可以点击操作
     *
     * @param clickable
     */
    override fun setClickable(clickable: Boolean) {
        this.mClickable = clickable
    }

    /**
     * 设置星星点击事件
     *
     * @param onRatingChangeListener
     */
    fun setOnRatingChangeListener(onRatingChangeListener: OnRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener
    }

    /**
     * 设置星星的大小
     *
     * @param starImageSize
     */
    fun setStarImageSize(starImageSize: Float) {
        this.starImageSize = starImageSize
    }

    fun setStepSize(stepSize: StepSize) {
        this.stepSize = stepSize
    }

    init {
        orientation = LinearLayout.HORIZONTAL
        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar)
        starImageSize = mTypedArray.getDimension(R.styleable.RatingBar_starImageSize, 20f)
        starPadding = mTypedArray.getDimension(R.styleable.RatingBar_starPadding, 10f)
        starStep = mTypedArray.getFloat(R.styleable.RatingBar_starStep, 1.0f)
        stepSize = StepSize.fromStep(mTypedArray.getInt(R.styleable.RatingBar_stepSize, 1))
        starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5)
        starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty)
        starFillDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starFill)
        starHalfDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starHalf)
        mClickable = mTypedArray.getBoolean(R.styleable.RatingBar_clickable, true)
        mTypedArray.recycle()
        for (i in 0..starCount - 1) {
            val imageView = starImageView
            imageView.setImageDrawable(starEmptyDrawable)
            imageView.setOnClickListener(
                    OnClickListener { v ->
                        if (mClickable) {
                            //浮点数的整数部分
                            var fint = starStep.toInt()
                            val b1 = BigDecimal(java.lang.Float.toString(starStep))
                            val b2 = BigDecimal(Integer.toString(fint))
                            //浮点数的小数部分
                            val fPoint = b1.subtract(b2).toFloat()
                            if (fPoint == 0f) {
                                fint -= 1
                            }

                            if (indexOfChild(v) > fint) {
                                setStar((indexOfChild(v) + 1).toFloat())
                            } else if (indexOfChild(v) == fint) {
                                if (stepSize == StepSize.Full) {//如果是满星 就不考虑半颗星了
                                    return@OnClickListener
                                }
                                //点击之后默认每次先增加一颗星，再次点击变为半颗星
                                if (imageView.drawable.current.constantState == starHalfDrawable!!.constantState) {
                                    setStar((indexOfChild(v) + 1).toFloat())
                                } else {
                                    setStar(indexOfChild(v) + 0.5f)
                                }
                            } else {
                                setStar(indexOfChild(v) + 1f)
                            }

                        }
                    }
            )
            addView(imageView)
        }
        setStar(starStep)
    }

    /**
     * 设置每颗星星的参数
     *
     * @return
     */
    private //设置每颗星星在线性布局的大小
            //设置每颗星星在线性布局的间距
    val starImageView: ImageView
        get() {
            val imageView = ImageView(context)

            val layout = LinearLayout.LayoutParams(
                    Math.round(starImageSize), Math.round(starImageSize))
            layout.setMargins(0, 0, Math.round(starPadding), 0)
            imageView.layoutParams = layout
            imageView.adjustViewBounds = true
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageDrawable(starEmptyDrawable)
            imageView.minimumWidth = 10
            imageView.maxHeight = 10
            return imageView

        }


    /**
     * 设置星星的个数
     *
     * @param rating
     */

    fun setStar(rating: Float) {

        if (onRatingChangeListener != null) {
            onRatingChangeListener!!.onRatingChange(rating)
        }
        this.starStep = rating
        //浮点数的整数部分
        val fint = rating.toInt()
        val b1 = BigDecimal(java.lang.Float.toString(rating))
        val b2 = BigDecimal(Integer.toString(fint))
        //浮点数的小数部分
        val fPoint = b1.subtract(b2).toFloat()

        //设置选中的星星
        for (i in 0..fint - 1) {
            (getChildAt(i) as ImageView).setImageDrawable(starFillDrawable)
        }
        //设置没有选中的星星
        for (i in fint..starCount - 1) {
            (getChildAt(i) as ImageView).setImageDrawable(starEmptyDrawable)
        }
        //小数点默认增加半颗星
        if (fPoint > 0) {
            (getChildAt(fint) as ImageView).setImageDrawable(starHalfDrawable)
        }
    }

    /**
     * 操作星星的点击事件
     */
    interface OnRatingChangeListener {
        /**
         * 选中的星星的个数
         */
        fun onRatingChange(ratingCount: Float)

    }

    /**
     * 星星每次增加的方式整星还是半星，枚举类型
     * 类似于View.GONE
     */
    enum class StepSize private constructor(internal var step: Int) {
        Half(0), Full(1);


        companion object {

            fun fromStep(step: Int): StepSize {
                for (f in values()) {
                    if (f.step == step) {
                        return f
                    }
                }
                throw IllegalArgumentException()
            }
        }
    }
}

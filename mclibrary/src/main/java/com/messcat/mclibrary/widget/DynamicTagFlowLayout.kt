package com.messcat.mclibrary.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.messcat.mclibrary.R
import java.util.ArrayList


/**
 * 流式标签(动态的，根据传入的数据动态添加标签)
 */
class DynamicTagFlowLayout : ViewGroup {

    private val mTags = ArrayList<String>()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        //��ǰViewGroup���ܸ߶�
        var totalHeight = 0
        //�������е������
        var maxLineWidth = 0

        //��ǰ�е����߶�
        var lineMaxHeight = 0
        //��ǰ�е��ܿ��
        var currentLineWidth = 0

        //ÿ��childView��ռ�õĿ��
        var childViewWidthSpace = 0
        //ÿ��childView��ռ�õĸ߶�
        var childViewHeightSpace = 0

        val count = childCount
        var layoutParams: ViewGroup.MarginLayoutParams

        for (i in 0..count - 1) {
            val child = getChildAt(i)

            if (child.visibility != View.GONE) {//ֻ�е����View�ܹ���ʾ��ʱ���ȥ����
                //����ÿ����View���Ի�ȡ��View�Ŀ�͸�
                measureChild(child, widthMeasureSpec, heightMeasureSpec)

                layoutParams = child.layoutParams as ViewGroup.MarginLayoutParams

                childViewWidthSpace = child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                childViewHeightSpace = child.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

                if (currentLineWidth + childViewWidthSpace > widthSize) {//��ʾ�����ǰ���ټ������������View���ͻᳬ���ܵĹ涨��ȣ���Ҫ����һ��
                    totalHeight += lineMaxHeight
                    if (maxLineWidth < currentLineWidth) {//����е����ȷ����˱仯�����±��������
                        maxLineWidth = currentLineWidth
                    }
                    currentLineWidth = childViewWidthSpace//����һ�к���Ҫ���õ�ǰ�п�
                    lineMaxHeight = childViewHeightSpace
                } else {//��ʾ��ǰ�п��Լ��������Ԫ��
                    currentLineWidth += childViewWidthSpace
                    if (lineMaxHeight < childViewHeightSpace) {
                        lineMaxHeight = childViewHeightSpace
                    }
                }
            }
        }

        setMeasuredDimension(if (widthMode == View.MeasureSpec.EXACTLY) widthSize else maxLineWidth, if (heightMode == View.MeasureSpec.EXACTLY) heightSize else totalHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //��ǰ�ǵڼ���
        var currentLine = 1
        //���ÿһ�е����߶�
        val lineMaxHeightList = ArrayList<Int>()

        //ÿ��childView��ռ�õĿ��
        var childViewWidthSpace = 0
        //ÿ��childView��ռ�õĸ߶�
        var childViewHeightSpace = 0

        //��ǰ�е����߶�
        var lineMaxHeight = 0
        //��ǰ�е��ܿ��
        var currentLineWidth = 0

        val count = childCount
        var layoutParams: ViewGroup.MarginLayoutParams

        for (i in 0..count - 1) {
            var cl = 0
            var ct = 0
            var cr = 0
            var cb = 0
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {//ֻ�е����View�ܹ���ʾ��ʱ���ȥ����

                layoutParams = child.layoutParams as ViewGroup.MarginLayoutParams
                childViewWidthSpace = child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                childViewHeightSpace = child.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

                println("getWidth()---->" + width)

                if (currentLineWidth + childViewWidthSpace > width) {//��ʾ�����ǰ���ټ������������View���ͻᳬ���ܵĹ涨��ȣ���Ҫ����һ��
                    lineMaxHeightList.add(lineMaxHeight)//��ʱ�Ƚ���һ�е����߶ȼ��뵽������
                    //�µ�һ�У�����һЩ����
                    currentLine++
                    currentLineWidth = childViewWidthSpace
                    lineMaxHeight = childViewHeightSpace

                    cl = layoutParams.leftMargin
                    if (currentLine > 1) {
                        for (j in 0..currentLine - 1 - 1) {
                            ct += lineMaxHeightList[j]
                        }
                        ct += layoutParams.topMargin
                    } else {
                        ct = layoutParams.topMargin
                    }
                } else {//��ʾ��ǰ�п��Լ��������Ԫ��
                    cl = currentLineWidth + layoutParams.leftMargin
                    if (currentLine > 1) {
                        for (j in 0..currentLine - 1 - 1) {
                            ct += lineMaxHeightList[j]
                        }
                        ct += layoutParams.topMargin
                    } else {
                        ct = layoutParams.topMargin
                    }
                    currentLineWidth += childViewWidthSpace
                    if (lineMaxHeight < childViewHeightSpace) {
                        lineMaxHeight = childViewHeightSpace
                    }
                }

                cr = cl + child.measuredWidth
                cb = ct + child.measuredHeight

                child.layout(cl, ct, cr, cb)

            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

    fun setTags(tags: List<String>?) {
        if (tags != null) {
            mTags.clear()
            mTags.addAll(tags)
            for (i in mTags.indices) {
                val tv = TextView(context)
                val lp = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
                lp.setMargins(15, 15, 15, 15)
                tv.layoutParams = lp
                tv.setBackgroundResource(R.mipmap.single_categoery_search)
                tv.setPadding(15, 15, 15, 15)
                tv.setTextColor(Color.WHITE)

                tv.text = mTags[i]

                tv.setOnClickListener { v ->
                    if (listener != null) {
                        listener!!.onClick(v)
                    }
                }

                addView(tv)
            }
            requestLayout()
        }
    }

    private var listener: OnTagItemClickListener? = null

    interface OnTagItemClickListener {
        fun onClick(v: View)
    }

    fun setOnTagItemClickListener(l: OnTagItemClickListener) {
        listener = l
    }

}

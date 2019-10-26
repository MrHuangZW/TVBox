package com.messcat.mclibrary

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * GridLayoutManager的边距
 * Created by Administrator on 2017/9/6 0006.
 */
class SpacesItemDecoration(left: Int, right: Int, bottom: Int, top: Int) : RecyclerView.ItemDecoration() {

    private var mLeft: Int = left
    private var mRight: Int = right
    private var mBottom: Int = bottom
    private var mTop: Int = top

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.left = mLeft
        outRect?.right = mRight
        outRect?.bottom = mBottom
        outRect?.top = mTop
    }
}
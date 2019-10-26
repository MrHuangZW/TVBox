package com.messcat.kotlin.base

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Administrator on 2017/8/16 0016.
 */
abstract class BaseRecyclerViewHolder<T>(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var mView: View? = itemView
}
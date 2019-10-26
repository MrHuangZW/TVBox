package com.messcat.tvbox.module.home.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.messcat.kotlin.base.BaseRecyclerViewAdapter
import com.messcat.kotlin.base.BaseRecyclerViewHolder
import com.messcat.kotlin.utils.long_toast
import com.messcat.kotlin.utils.toast
import com.messcat.mclibrary.BASE_URL
import com.messcat.tvbox.R
import com.messcat.tvbox.R.id.tv_box_image
import com.messcat.tvbox.R.id.tv_box_img_travel
import com.messcat.tvbox.module.home.GlideRoundTransform
import com.messcat.tvbox.module.home.bean.ClassifyBean
import com.messcat.tvbox.utils.loadImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_wgallery.view.*


/**
 * Created by Administrator on 2017/8/25 0025.
 */
class NewsClassAdapter(context: Context?) : BaseRecyclerViewAdapter<ClassifyBean.ResultBean.Categorys2Bean>() {


    private var mContext = context
    var mListener: onSelectListener? = null
    var mPosition: Int? = -1


    fun setOnItemStateListener(listener: onSelectListener) {
        mListener = listener
    }

    fun setFocus(position: Int) {
        mPosition = position
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBaseBindViewHolder(objectData: ClassifyBean.ResultBean.Categorys2Bean, position: Int, itemView: View?, isSelect: Boolean?) {
//        loadImage(mContext, BASE_URL + objectData.picture, itemView?.tv_box_image)
        Glide.with(mContext).load(BASE_URL + objectData.picture)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).transform(CenterCrop(mContext), GlideRoundTransform(mContext, 8)).into(itemView?.tv_box_image)
        itemView?.tv_box_text?.text = objectData.name
        val para1: ViewGroup.LayoutParams
        val para2: ViewGroup.LayoutParams
        para1 = itemView?.tv_box_image?.getLayoutParams()!!
//        para2 = itemView?.focusrelativelayout?.getLayoutParams()!!
        itemView?.tv_box_image?.setOnFocusChangeListener { view, b ->
            if (b) {
                itemView?.focusrelativelayout?.scaleX = 1.2f
                itemView?.focusrelativelayout?.scaleY = 1.2f
                mListener?.onSelectItemListener(objectData, itemView?.focusrelativelayout, position)
                itemView?.tv_box_image?.layoutParams = para1
            } else {
                itemView?.focusrelativelayout?.scaleY = 1f
                itemView?.focusrelativelayout?.scaleX = 1f
                itemView?.tv_box_image?.layoutParams = para1
            }
        }
        if (position == mPosition) {
            itemView?.tv_box_image?.requestFocus()
        } else {
//            itemView?.tv_box_image?.focusable=View.NOT_FOCUSABLE
        }
        itemView?.tv_box_image?.setOnClickListener {
            mListener?.onSelectItemListener(objectData, itemView?.focusrelativelayout, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ClassifyBean.ResultBean.Categorys2Bean>? = NewsClassHolder(LayoutInflater.from(mContext).inflate(R.layout.item_wgallery, parent, false))

    class NewsClassHolder(itemView: View?) : BaseRecyclerViewHolder<ClassifyBean.ResultBean.Categorys2Bean>(itemView)

    interface onSelectListener {
        fun onSelectItemListener(category: ClassifyBean.ResultBean.Categorys2Bean, relativeLayout: RelativeLayout, position: Int)
    }

}
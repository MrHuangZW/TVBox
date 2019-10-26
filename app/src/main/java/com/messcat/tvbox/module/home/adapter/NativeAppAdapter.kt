package com.messcat.tvbox.module.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.messcat.kotlin.base.BaseRecyclerViewAdapter
import com.messcat.kotlin.base.BaseRecyclerViewHolder
import com.messcat.kotlin.utils.e
import com.messcat.mclibrary.BASE_URL
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.NativeAppBean
import com.messcat.tvbox.utils.loadImage
import kotlinx.android.synthetic.main.item_wgallery.view.*
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.messcat.tvbox.module.home.GlideRoundTransform
import com.messcat.tvbox.module.home.bean.ApplicationNewBean


/**
 * Created by Administrator on 2017/9/6 0006.
 */
class NativeAppAdapter(context: Context?) : BaseRecyclerViewAdapter<ApplicationNewBean.ResultBean.CategoryBean>() {

    private var mContext = context
    var mListener: onSelectListener? = null

    fun setOnItemStateListener(listener: onSelectListener) {
        mListener = listener
    }

    override fun onBaseBindViewHolder(objectData: ApplicationNewBean.ResultBean.CategoryBean, position: Int, itemView: View?, isSelect: Boolean?) {
//        loadImage(mContext, BASE_URL + objectData.picture, itemView?.tv_box_image)
        Glide.with(mContext).load(BASE_URL + objectData.picture)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).transform(CenterCrop(mContext), GlideRoundTransform(mContext, 8)).into(itemView?.tv_box_image)
        itemView?.tv_box_text?.text = objectData.name
        val para1: ViewGroup.LayoutParams
//        val para2: ViewGroup.LayoutParams
        para1 = itemView?.tv_box_image?.getLayoutParams()!!
//        para2 = itemView?.focusrelativelayout?.getLayoutParams()!!
        itemView?.tv_box_image?.setOnFocusChangeListener { view, b ->
            if (b) {
                e("Kotlin")
                itemView?.focusrelativelayout?.scaleY = 1.2f
                itemView?.focusrelativelayout?.scaleX = 1.2f
//                itemView?.tv_box_layout?.setBackgroundResource(R.drawable.shape_gallery_select)
                itemView?.tv_box_image?.isEnabled = false
                mListener?.onSelectItemListener(objectData, itemView?.focusrelativelayout, position)
//                para1.height = 78
//                para1.width = 132
//                para2.height = 78
//                para2.width = 132
//                itemView?.focusrelativelayout?.layoutParams = para2
                itemView?.tv_box_image?.layoutParams = para1
            } else {
                itemView?.focusrelativelayout?.scaleY = 1f
                itemView?.focusrelativelayout?.scaleX = 1f
//                itemView?.tv_box_layout?.setBackgroundResource(0)
                itemView?.tv_box_image?.isEnabled = true
//                para1.height = 69
//                para1.width = 110
//                para2.height = 69
//                para2.width = 110
//                itemView?.focusrelativelayout?.layoutParams = para2
                itemView?.tv_box_image?.layoutParams = para1
            }
        }
        itemView?.tv_box_image?.setOnClickListener {
            mListener?.onSelectItemListener(objectData, itemView?.focusrelativelayout, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ApplicationNewBean.ResultBean.CategoryBean> = NativeAppHolder(LayoutInflater.from(mContext).inflate(R.layout.item_wgallery, parent, false))

    class NativeAppHolder(itemView: View?) : BaseRecyclerViewHolder<ApplicationNewBean.ResultBean.CategoryBean>(itemView)

    interface onSelectListener {
        fun onSelectItemListener(category: ApplicationNewBean.ResultBean.CategoryBean, relativeLayout: RelativeLayout, position: Int)
    }
}
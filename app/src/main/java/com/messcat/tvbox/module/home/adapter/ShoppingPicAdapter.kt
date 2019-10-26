package com.messcat.tvbox.module.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.messcat.kotlin.base.BaseRecyclerViewAdapter
import com.messcat.kotlin.base.BaseRecyclerViewHolder
import com.messcat.mclibrary.BASE_URL
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.ClassifyBean
import kotlinx.android.synthetic.main.item_shopping.view.*
import com.messcat.tvbox.utils.loadImage

/**
 * Created by Administrator on 2017/9/6 0006.
 */
class ShoppingPicAdapter(context: Context?) : BaseRecyclerViewAdapter<ClassifyBean.ResultBean.GoodDetails>() {
    private var mContext = context
    private var mListener: OnSelectListener? = null

    fun setListener(listener: OnSelectListener?) {
        mListener = listener
    }

    override fun onBaseBindViewHolder(objectData: ClassifyBean.ResultBean.GoodDetails, position: Int, itemView: View?, isSelect: Boolean?) {
        loadImage(mContext, BASE_URL + objectData.picture, itemView?.tv_box_shopping_img)
        itemView?.tv_box_shopping_name?.text = objectData.title
        itemView?.tv_box_shopping_price?.text = "￥ " + objectData.price
        itemView?.tv_box_shopping_img?.setOnFocusChangeListener { view, b ->
            if (b) {
                itemView?.tv_box_shopp?.setBackgroundResource(R.mipmap.index_select_bg)
            } else {
                itemView?.tv_box_shopp?.setBackgroundResource(0)
            }
        }
        itemView?.tv_box_shopping_img?.setOnClickListener {
            mListener?.setOnSelectListener()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ClassifyBean.ResultBean.GoodDetails>
            = ShoppingPicHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shopping, parent, false))

    class ShoppingPicHolder(itemView: View?) : BaseRecyclerViewHolder<ClassifyBean.ResultBean.GoodDetails>(itemView)

    interface OnSelectListener {
        fun setOnSelectListener()
    }
}
package com.messcat.tvbox.module.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.messcat.kotlin.base.BaseRecyclerViewAdapter
import com.messcat.kotlin.base.BaseRecyclerViewHolder
import com.messcat.mclibrary.BASE_URL
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.ApplicationNewBean
import com.messcat.tvbox.utils.loadImage
import kotlinx.android.synthetic.main.item_naticeapps.view.*


/**
 * Created by Administrator on 2017/9/6 0006.
 */
class NativeAppsAdapter(context: Context?) : BaseRecyclerViewAdapter<ApplicationNewBean.ResultBean.DetailsBean>() {

    private var mContext = context
    private var mListener: OnSelectItemListener? = null
    private var arrayInt: List<String>? = null

    fun setArray(array: List<String>?) {
        arrayInt = array
        notifyDataSetChanged()
    }

    fun setListener(listener: OnSelectItemListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ApplicationNewBean.ResultBean.DetailsBean> = NativeAppsHolder(LayoutInflater.from(mContext).inflate(R.layout.item_naticeapps, parent, false))

    override fun onBaseBindViewHolder(objectData: ApplicationNewBean.ResultBean.DetailsBean, position: Int, itemView: View?, isSelect: Boolean?) {

        itemView?.visibility = View.VISIBLE
        itemView?.tv_box_app_layout!!.visibility = View.VISIBLE
        itemView?.tv_box_image_app!!.visibility = View.VISIBLE
        itemView?.tv_box_app_name!!.visibility = View.VISIBLE
        loadImage(mContext, BASE_URL + objectData.picture, itemView?.tv_box_image_app)
        itemView?.tv_box_app_name?.text = objectData.title
        itemView?.tv_box_image_app?.setOnFocusChangeListener { view, b ->
            if (b) {
                itemView?.tv_box_app_layout?.setBackgroundResource(R.mipmap.index_select_bg)
            } else {
                itemView?.tv_box_app_layout?.setBackgroundResource(0)
            }
        }
        itemView?.tv_box_image_app?.setOnClickListener {
            mListener?.setOnSelectItemListener(objectData, position)
        }
//        }

    }

    class NativeAppsHolder(itemView: View?) : BaseRecyclerViewHolder<ApplicationNewBean.ResultBean.DetailsBean>(itemView)

    interface OnSelectItemListener {
        fun setOnSelectItemListener(objectData: ApplicationNewBean.ResultBean.DetailsBean, position: Int)
    }
}
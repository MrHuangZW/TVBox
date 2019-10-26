package com.messcat.tvbox.module.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.messcat.kotlin.base.BaseRecyclerViewAdapter
import com.messcat.kotlin.base.BaseRecyclerViewHolder
import com.messcat.kotlin.utils.e
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.ClassifyBean
import kotlinx.android.synthetic.main.item_newsinfor.view.*

/**
 * Created by Administrator on 2017/9/5 0005.
 */
class NewsInformationAdapter(context: Context?) : BaseRecyclerViewAdapter<ClassifyBean.ResultBean.Categorys2Bean.List2Bean>() {

    private var mContext = context
    private var mListener: OnSelectListener? = null

    fun setListener(listener: OnSelectListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseRecyclerViewHolder<ClassifyBean.ResultBean.Categorys2Bean.List2Bean>
            = NewsInformationHolder(LayoutInflater.from(mContext).inflate(R.layout.item_newsinfor, parent, false))

    override fun onBaseBindViewHolder(objectData: ClassifyBean.ResultBean.Categorys2Bean.List2Bean, position: Int, itemView: View?, isSelect: Boolean?) {
        itemView?.tv_box_news?.text = objectData.name
        itemView?.tv_box_news?.setOnFocusChangeListener { view, b ->
            if (b) {
                itemView?.tv_box_news?.setBackgroundResource(R.mipmap.hotel_service_tab_bg)
                mListener?.setOnSelectListener(objectData.id.toString(), itemView?.tv_box_news, position)
            } else {
                itemView?.tv_box_news?.setBackgroundResource(0)
            }
        }

        itemView?.tv_box_news?.setOnClickListener {
            mListener?.setOnSelectListener(objectData.id.toString(), itemView?.tv_box_news, position)
        }
    }

    class NewsInformationHolder(itemView: View?) : BaseRecyclerViewHolder<ClassifyBean.ResultBean.Categorys2Bean.List2Bean>(itemView)

    interface OnSelectListener {
        fun setOnSelectListener(id: String, textView: TextView, position: Int)
    }
}
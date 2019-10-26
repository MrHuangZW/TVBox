package com.messcat.kotlin.base

import android.support.v7.widget.RecyclerView
import android.view.View
import com.messcat.mclibrary.model.SelectBean

/**
 * Created by Administrator on 2017/8/16 0016.
 */
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseRecyclerViewHolder<T>>() {

    private var dataList: MutableList<T> = ArrayList<T>()
    internal var headerView: View? = null
    private var selectList = ArrayList<SelectBean>()
    val TYPE_HEAD = 1
    val TYPE_NOMAL = 0

    /**
     * 添加头部View

     * @param headerView
     */
    fun setHeaderView(headerView: View?) {
        this.headerView = headerView
        headerView?.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
        notifyItemInserted(0)
    }

    /**
     * 判断当前Item的类型
     * @param position
     * *
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        if (headerView == null)
            return TYPE_NOMAL
        if (position == 0)
            return TYPE_HEAD
        return TYPE_NOMAL
    }

    /**
     * 从第三个Item获取数据，为什么减2是因为XRecyclerView的原因，正常的RecyclerView只需要减1
     */
    fun getRealPosition(holder: RecyclerView.ViewHolder?): Int {
        val position = holder?.layoutPosition
        return if (headerView == null) position!! else position!! - 2
    }

    /**
     * 如果headerView不为空，也就是第一个Item有内容，所以要从第二个Item开始获取数据
     */
    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>?, position: Int) {
        if (getItemViewType(position) == TYPE_HEAD) {
            return
        }
        selectList.add(SelectBean(false))
        onBaseBindViewHolder(dataList.get(getRealPosition(holder)), getRealPosition(holder), holder?.mView, selectList[position].isSelect)
    }

    abstract fun onBaseBindViewHolder(objectData: T, position: Int, itemView: View?, isSelect: Boolean?)
    /**
     * 如果headerView不为空就加一
     */
    override fun getItemCount(): Int {
        var count = if (dataList == null) 0 else dataList.size
        if (headerView != null) {
            count++
        }
        return count
    }

    /**
     * 是否选中
     */
    fun initSelect(position: Int) {
        if (selectList.size > 0) {
            selectList.clear()
        }
        for ((index, value) in dataList.withIndex()) {
            if (index == position) {
                selectList.add(SelectBean(true))
            } else {
                selectList.add(SelectBean(false))
            }
        }
    }

    /**
     * 刷新
     */
    fun refreshList(list: MutableList<T>?) {
        if (dataList.size > 0) {
            dataList.clear()
        }
        loadList(list)
    }

    /**
     * 加载
     */
    fun loadList(list: MutableList<T>?) {
        dataList.addAll(list!!)
        notifyDataSetChanged()
    }

    /**
     * 删除Item
     */
    fun removeItem(position: Int) {
        if (dataList.size > 0 && dataList.size - 1 >= position) {
            if (dataList.get(position) != null) {
                dataList.remove(dataList.get(position))
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 获取集合
     */
    fun getList(): MutableList<T> = dataList

    /**
     * 清除所有数据
     */
    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }

    /**
     * 获取集合的数量
     */
    fun getListSize(): Int = dataList.size
}
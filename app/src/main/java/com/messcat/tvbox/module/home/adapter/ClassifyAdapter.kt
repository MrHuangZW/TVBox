package com.messcat.tvbox.module.home.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Administrator on 2017/8/24 0024.
 */
class ClassifyAdapter(fm: FragmentManager?, list: MutableList<Fragment>) : FragmentPagerAdapter(fm) {

    private var fragmentList = list
    override fun getItem(position: Int): Fragment = fragmentList.get(position)

    override fun getCount(): Int = fragmentList.size
}
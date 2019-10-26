package com.messcat.tvbox.module.home.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.messcat.kotlin.utils.long_toast
import com.messcat.mclibrary.base.LANGUAGE_TYPE
import com.messcat.tvbox.R
import com.messcat.tvbox.utils.isInstallApp
import com.messcat.tvbox.utils.openOthemApp
import kotlinx.android.synthetic.main.activity_open_local_player.*

/**
 *author:Bandele
 *date:2018/10/15 0015$
 *describe:
 */

class OpenLocalPlayerActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_local_player)
        bt_sure.setOnClickListener {
            if (isInstallApp(packageManager, "com.xiaobaifile.xbplayer")) {
                openOthemApp(this, "com.xiaobaifile.xbplayer")
            } else {
                long_toast("没有安装小白播放器该应用", this)
            }
        }
        if (LANGUAGE_TYPE.equals("chinese")) {
            tv_open.text = "打开本地影音"
            bt_sure.text="确定"
        } else {
            tv_open.text = "Open local Player"
            bt_sure.text="sure"
        }
    }
}
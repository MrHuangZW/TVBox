package com.messcat.tvbox.module.home.activity

import android.content.Context
import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.gson.Gson
import com.messcat.kotlin.mchttp.RetrofitServiceManager
import com.messcat.kotlin.utils.long_toast
import com.messcat.kotlin.utils.toast
import com.messcat.mclibrary.base.MVPActivity
import com.messcat.mclibrary.finishActivitys
import com.messcat.mclibrary.goToActivity
import com.messcat.tvbox.R
import com.messcat.tvbox.module.home.bean.MessageBean
import com.messcat.tvbox.module.home.contract.MessageContract
import com.messcat.tvbox.module.home.presenter.MessagePresenter
import com.messcat.tvbox.utils.isInstallApp
import com.messcat.tvbox.utils.openOthemApp
import kotlinx.android.synthetic.main.activity_message.*
import android.view.inputmethod.InputMethodManager
import com.messcat.mclibrary.base.MEMBER
import android.provider.Settings.ACTION_SETTINGS
import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.drawable.Animatable
import android.media.MediaPlayer
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.VideoView
import com.messcat.mclibrary.util.*
import com.messcat.tvbox.utils.getUUID
import kotlinx.android.synthetic.main.activity_playmovie.*
import java.util.*
import android.content.ComponentName
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.View.GONE


/**
 * 设置数据页面
 * Created by Administrator on 2017/9/21 0021.
 */
class MessageActivity : MVPActivity<MessagePresenter>(), MessageContract.View {


    private var code: String? = null
    private var num: String? = null
    private var baseUrl:String?=null

    override fun showError(msg: String?) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show()
    }

    override fun showLoadingDialog() {
    }

    override fun dismissLoadingDialog() {
    }

    override fun checkHotelCodeResult(messageBean: MessageBean?) {
        putString("code", messageBean?.result?.diCode!!)
        putString("num", messageBean?.result?.roomNum!!)
        saveMember(Gson().toJson(messageBean))
        goToActivity(mActivity, SplashActivity::class.java)
        finishActivitys(mActivity)
    }

    override fun initPresenter(): MessagePresenter = MessagePresenter(this, MessagePresenter.CheckHotelCodeLoader(
            RetrofitServiceManager.instance.create(MessagePresenter.CheckHotelCodeHttp::class.java)))

    override fun getLayout(): Int = R.layout.activity_message
    override fun initData() {
    }

    override fun initEvent() {

        et_tv_box_ip.setOnClickListener{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_tv_box_ip, InputMethodManager.SHOW_FORCED)
        }
        et_tv_box_ip.setOnFocusChangeListener { view, b ->
            if (b) {
                et_tv_box_ip.setBackgroundResource(R.drawable.shape_message_select)
            } else {
                et_tv_box_ip.setBackgroundResource(R.drawable.shape_message)
            }
        }

        tv_commit.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_commit.setBackgroundResource(R.mipmap.welcome_help_bg)
            } else {
                tv_commit.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
            }
        }

        tv_commit.setOnClickListener{
            baseUrl = et_tv_box_ip.text.toString()
            if (!TextUtils.isEmpty(baseUrl)) {
               llt_container.setVisibility(GONE);
            } else {
                toast("请填写ip地址", mContext)
            }
        }

        tv_box_diCode.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(tv_box_diCode, InputMethodManager.SHOW_FORCED)
        }
        tv_box_roomNum.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(tv_box_roomNum, InputMethodManager.SHOW_FORCED)
        }
        tv_box_diCode.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_box_diCode.setBackgroundResource(R.drawable.shape_message_select)
            } else {
                tv_box_diCode.setBackgroundResource(R.drawable.shape_message)
            }
        }
        tv_box_roomNum.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_box_roomNum.setBackgroundResource(R.drawable.shape_message_select)
            } else {
                tv_box_roomNum.setBackgroundResource(R.drawable.shape_message)
            }
        }
        tv_box_yes.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_box_yes.setBackgroundResource(R.mipmap.welcome_help_bg)
            } else {
                tv_box_yes.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
            }
        }
        tv_box_set.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_box_set.setBackgroundResource(R.mipmap.welcome_help_bg)
            } else {
                tv_box_set.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
            }
        }
        tv_box_manage.setOnFocusChangeListener { view, b ->
            if (b) {
                tv_box_manage.setBackgroundResource(R.mipmap.welcome_help_bg)
            } else {
                tv_box_manage.setBackgroundResource(R.mipmap.welcome_help_bg_hover)
            }
        }
        tv_box_yes.setOnClickListener {
            code = tv_box_diCode.text.toString()
            num = tv_box_roomNum.text.toString()
            if (code != null && !"".equals(code) && num != null && !"".equals(num)) {
                Log.d("lyhlog", MacUtils.getMac())
                mPresenter?.checkHotelCode(code, num, MacUtils.getMac())
            } else {
                toast("请填写酒店码和房间号", mContext)
            }
        }
        tv_box_set.setOnClickListener {
            //            //系统设置
//            val intent = Intent(Settings.ACTION_SETTINGS)
////            val intent = Intent(Settings.ACTION)
//            startActivity(intent)
//            if (isInstallApp(packageManager,"com.android.tv.settings")) {
//                openOthemApp(mActivity, "com.android.tv.settings")
//            } else {
//                long_toast("没有安装该应用", mContext)
//            }
            if (isInstallApp(packageManager, "com.android.tv.settings")) {
                val intent = Intent()
                intent.component = ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings")
                mContext!!.startActivity(intent)
            } else {
                long_toast("没有安装该应用", mContext)
            }

//            val intent = Intent()
//            intent.component = ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings")
//            mContext!!.startActivity(intent)
//            if (isInstallApp(packageManager, "com.coolux.setting")) {
//                openOthemApp(mActivity, "com.coolux.setting")
//            } else {
//                long_toast("没有安装应用管理软件", mContext)
//            }
            // intent.setComponent(new
            // ComponentName("com.droidlogic.mboxlauncher",
            // "com.droidlogic.mboxlauncher.AppDrawerActivity"));
//            val packageURI = Uri.parse("package:" + "com.android.tv.settings")
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
//            startActivity(intent)
        }
        tv_box_manage.setOnClickListener {
            val currentapiVersion = android.os.Build.VERSION.SDK_INT
            if (currentapiVersion <= 24) {
                //应用管理
                if (isInstallApp(packageManager, "com.droidlogic.appinstall")) {
                    openOthemApp(mActivity, "com.droidlogic.appinstall")
                } else {
                    long_toast("没有安装应用管理软件", mContext)
                }
//                if (isInstallApp(packageManager, "com.xiaobaifile.tv.kls")) {
//                    openOthemApp(mActivity, "com.xiaobaifile.tv.kls")
//                } else {
//                    long_toast("没有安装应用管理软件", mContext)
//                }
            } else {
                //应用管理
//                val intent = Intent()
//                intent.component = ComponentName("com.droidlogic.appinstall", "com.droidlogic.appinstall.MainSettings")

                // intent.setComponent(new
                // ComponentName("com.droidlogic.mboxlauncher",
                // "com.droidlogic.mboxlauncher.AppDrawerActivity"));
//                mContext!!.startActivity(intent)
                if (isInstallApp(packageManager, "com.droidlogic.tv.settings")) {
                    openOthemApp(mActivity, "com.droidlogic.tv.settings")
                } else {
                    long_toast("没有安装应用管理软件", mContext)
                }
            }

        }
    }

    override fun initView() {
        if (getSPString("code", null) != null) {
            tv_box_diCode.setText(getSPString("code", null))
        }
        if (getSPString("num", null) != null) {
            tv_box_roomNum.setText(getSPString("num", null))
        }
    }
}
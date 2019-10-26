package com.messcat.mclibrary.util

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.loader.ImageLoader
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.messcat.mclibrary.R
import java.io.File

/**
 * 初始化图片选择器
 * Created by Administrator on 2017/8/30 0030.
 */

fun Any.initImagePicker(activity: Activity) {
    val imagePicker = ImagePicker.getInstance()
    imagePicker.setImageLoader(GlideImageLoader())   //设置图片加载器
    imagePicker.setShowCamera(true)  //显示拍照按钮
    imagePicker.setCrop(true)        //允许裁剪（单选才有效）
    imagePicker.setSaveRectangle(false) //是否按矩形区域保存
    imagePicker.setSelectLimit(1)    //选中数量限制
    imagePicker.setStyle(CropImageView.Style.RECTANGLE)  //裁剪框的形状
    imagePicker.setFocusWidth(800)   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
    imagePicker.setFocusHeight(800)  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
    imagePicker.setOutPutX(1000)//保存文件的宽度。单位像素
    imagePicker.setOutPutY(1000)//保存文件的高度。单位像素
}

internal class GlideImageLoader : ImageLoader {

    override fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
        Glide.with(activity).load(File(path))
                .placeholder(R.mipmap.default_image)
                .error(R.mipmap.default_image)
                .centerCrop()
                .into(imageView)
    }

    override fun clearMemoryCache() {
        //清除缓存
    }
}

/**
 * 打开图片选择器
 */
fun Any.openImagePicker(activity: Activity, IMAGE_PICKER: Int) {
    val intent = Intent(activity, ImageGridActivity::class.java)
    activity.startActivityForResult(intent, IMAGE_PICKER)
}
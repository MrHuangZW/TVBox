package com.messcat.mclibrary.widget

import android.graphics.PorterDuffXfermode
import android.graphics.Shader.TileMode
import android.graphics.Bitmap
import android.view.View
import android.view.View.MeasureSpec
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff.Mode



/**
 * 倒影实现类
 * Created by Administrator on 2017/9/7 0007.
 */
class ImageReflect {
    /**
     * 图片倒影效果实现
     */
    private val reflectImageHeight = 80

    /**
     * view界面转换成bitmap
     *
     * @param view
     * @return
     */
    fun convertViewToBitmap(view: View): Bitmap {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight())
        view.buildDrawingCache()
        return view.getDrawingCache()
    }

    /**
     * 将bitmap设置倒影
     *
     * @param bitmap
     * @return
     */
    fun createReflectedImage(bitmap: Bitmap, reflectHeight: Int): Bitmap? {

        val width = bitmap.width

        val height = bitmap.height
        if (height <= reflectHeight) {
            return null

        }

        val matrix = Matrix()

        matrix.preScale(1f, -1f)

        val reflectionImage = Bitmap.createBitmap(bitmap, 0, height - reflectHeight, width, reflectHeight, matrix, true)

        val bitmapWithReflection = Bitmap.createBitmap(width, reflectHeight,
                Config.ARGB_8888)
        val canvas = Canvas(bitmapWithReflection)
        canvas.drawBitmap(reflectionImage, 0f, 0f, null)
        val shader = LinearGradient(0f, 0f, 0f,
                bitmapWithReflection.height.toFloat(), 0x80ffffff.toInt(), 0x00ffffff, TileMode.CLAMP)

        val paint = Paint()
        paint.setShader(shader)

        paint.setXfermode(PorterDuffXfermode(Mode.DST_IN))
        canvas.drawRect(0f, 0f, width.toFloat(), bitmapWithReflection.height.toFloat(), paint)
        return bitmapWithReflection

    }

    fun createCutReflectedImage(bitmap: Bitmap, cutHeight: Int): Bitmap? {

        val width = bitmap.width

        val height = bitmap.height
        val totleHeight = reflectImageHeight + cutHeight

        if (height <= totleHeight) {
            return null
        }

        val matrix = Matrix()

        matrix.preScale(1f, -1f)

        println(height - reflectImageHeight - cutHeight)
        val reflectionImage = Bitmap.createBitmap(bitmap, 0, height
                - reflectImageHeight - cutHeight, width, reflectImageHeight,
                matrix, true)

        val bitmapWithReflection = Bitmap.createBitmap(width,
                reflectImageHeight, Config.ARGB_8888)
        val canvas = Canvas(bitmapWithReflection)
        canvas.drawBitmap(reflectionImage, 0f, 0f, null)
        val shader = LinearGradient(0f, 0f, 0f,
                bitmapWithReflection.height.toFloat(), 0x80ffffff.toInt(), 0x00ffffff, TileMode.CLAMP)

        val paint = Paint()
        paint.setShader(shader)

        paint.setXfermode(PorterDuffXfermode(Mode.DST_IN))
        canvas.drawRect(0f, 0f, width.toFloat(), bitmapWithReflection.height.toFloat(), paint)
        if (!reflectionImage.isRecycled) {
            reflectionImage.recycle()
        }
        // if (!bitmap.isRecycled()) {
        // bitmap.recycle();
        // }
        System.gc()
        return bitmapWithReflection

    }
}
package com.messcat.tvbox.module.home

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation



/**
 *author:Bandele
 *date:2018/9/19 0019$
 *describe:
 */
    class GlideRoundTransform
    /** * 构造函数 * * @param context Context * @param dp 圆角半径  */ @JvmOverloads constructor(context: Context?, dp: Int = 4) : BitmapTransformation(context) {
        init {
            radius = Resources.getSystem().getDisplayMetrics().density * dp
        }

        override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
            return roundCrop(pool, toTransform)
        }

        override fun getId(): String {
            return javaClass.name + Math.round(radius)
        }

        companion object {
            private var radius = 0f
            private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
                if (source == null) return null
                var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
                if (result == null) {
                    result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
                }
                val canvas = Canvas(result)
                val paint = Paint()

                var shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                paint.shader = shader
                paint.isAntiAlias = true
                val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
                canvas.drawRoundRect(rectF, radius, radius, paint)
                return result
            }
        }
    }
/** * 构造函数 默认圆角半径 4dp * * @param context Context  */
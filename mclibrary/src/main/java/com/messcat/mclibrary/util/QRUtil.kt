package com.messcat.mclibrary.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.util.*

/**
 * Created by Administrator on 2017/9/4 0004.
 */

private val BLACK = 0xff000000.toInt()
//生成二维码
fun Create2QR(uri: String?, mScreenWidth: Int, iv_qr_image: ImageView) {
    val bitmap: Bitmap?
    try {
        bitmap = createQRCode(uri, mScreenWidth)

        if (bitmap != null) {
            iv_qr_image.setImageBitmap(bitmap)
        }

    } catch (e: WriterException) {
        e.printStackTrace()
    }

}

/**
 * 生成一个二维码图像
 *
 *
 * 传入的字符串，通常是一个URL
 *
 * @param widthAndHeight 图像的宽高
 * @return
 */
fun createQRCode(str: String?, widthAndHeight: Int): Bitmap {
    val hints = Hashtable<EncodeHintType, String>()
    hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
    val matrix = MultiFormatWriter().encode(str,
            BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight)
    val width = matrix.width
    val height = matrix.height
    val pixels = IntArray(width * height)

    for (y in 0..height - 1) {
        for (x in 0..width - 1) {
            if (matrix.get(x, y)) {
                pixels[y * width + x] = BLACK
            }
        }
    }
    val bitmap = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}
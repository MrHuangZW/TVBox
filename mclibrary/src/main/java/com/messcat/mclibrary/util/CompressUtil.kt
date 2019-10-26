package com.messcat.kotlin.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import java.io.*

/**
 * 图片按一定的大小压缩
 * Created by Administrator on 2017/8/18 0018.
 */
/**
 * 循环压缩
 */
fun Any.compressImage(path: String, bitmapSize: Int, fileName: String, filePath: String): File? {
    val image = getBitmapFromFile(path)
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    var options = 100
    if (options > 0) {
        while (baos.toByteArray().size / 1024 > bitmapSize) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            options -= 10//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
        }
    }
    val isBm = ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
    val bitmap = BitmapFactory.decodeStream(isBm, null, null)//把ByteArrayInputStream数据生成图片
    return saveBitmap(bitmap, fileName, filePath)
}

/**
 * 将Bitmap保存到本地
 */
fun saveBitmap(bitmap: Bitmap?, fileName: String, filePath: String): File? {
    //在SD卡上创建目录
    if (bitmap != null) {
        val tmpDir = createFile(fileName, filePath)
        try {
            val fos = FileOutputStream(tmpDir)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            return tmpDir
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    } else {
        return null
    }
}

/**
 * 创建SD卡下的文件
 */
fun createFile(fileName: String, fielPath: String): File {
    val filePath = getSDPath() + File.separator + fileName
    val file = File(filePath)
    if (!file.exists()) {
        file.mkdir()
    }
    val file1 = File(filePath + File.separator + fielPath)
    if (!file1.exists()) {
        try {
            file1.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    return file1
}

/**
 * 获取SD卡目录
 */
fun getSDPath(): String {
    var sdDis: String? = null
    val sdCardExit = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    if (sdCardExit) {
        sdDis = Environment.getExternalStorageDirectory().path
    }
    return sdDis!!
}

/**
 * 计算缩放比
 */
fun getRatioSize(bitWidth: Int, bitHeight: Int): Int {
    // 图片最大分辨率
    val imageHeight = 1280
    val imageWidth = 960
    // 缩放比
    var ratio = 1
    // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    if (bitWidth > bitHeight && bitWidth > imageWidth) {
        // 如果图片宽度比高度大,以宽度为基准
        ratio = bitWidth / imageWidth
    } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
        // 如果图片高度比宽度大，以高度为基准
        ratio = bitHeight / imageHeight
    }
    // 最小比率为1
    if (ratio <= 0)
        ratio = 1
    return ratio
}

/**
 * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
 */
fun getBitmapFromFile(filePath: String): Bitmap {
    val newOpts = BitmapFactory.Options()
    newOpts.inJustDecodeBounds = true//只读边,不读内容
    BitmapFactory.decodeFile(filePath, newOpts)
    val w = newOpts.outWidth
    val h = newOpts.outHeight
    // 获取尺寸压缩倍数
    newOpts.inSampleSize = getRatioSize(w, h)
    newOpts.inJustDecodeBounds = false//读取所有内容
    newOpts.inDither = false
    newOpts.inPurgeable = true
    newOpts.inInputShareable = true
    newOpts.inTempStorage = ByteArray(32 * 1024)
    var bitmap: Bitmap? = null
    val file = File(filePath)
    var fs: FileInputStream? = null
    try {
        fs = FileInputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }

    try {
        if (fs != null) {
            bitmap = BitmapFactory.decodeFileDescriptor(fs.fd, null, newOpts)
            //旋转图片
            val photoDegree = readPictureDegree(filePath)
            if (photoDegree != 0) {
                val matrix = Matrix()
                matrix.postRotate(photoDegree.toFloat())
                // 创建新的图片
                bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap!!.width, bitmap.height, matrix, true)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        if (fs != null) {
            try {
                fs.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    return bitmap!!
}

/**
 * 读取图片属性：旋转的角度
 */

fun readPictureDegree(path: String): Int {
    var degree = 0
    try {
        val exifInterface = ExifInterface(path)
        val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return degree
}
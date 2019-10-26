@file:JvmName("FileUtil")
@file:JvmMultifileClass

package com.messcat.mclibrary.util

import android.os.Environment
import com.messcat.kotlin.utils.getSDPath
import com.messcat.kotlin.utils.isSpace
import java.io.File
import java.io.IOException

/**
 * Created by Administrator on 2017/8/30 0030.
 */
/**
 * 获取SD卡目录
 */
fun Any.getSDPath(): String? {
    var sdDis: String? = null
    val sdCardExit = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    if (sdCardExit) {
        sdDis = Environment.getExternalStorageDirectory().path
    }
    return sdDis
}

/**
 * 创建SD卡下的文件
 *
 * @param path
 */
fun createFile(fileName: String, path: String): File {
    val filePath = getSDPath() + File.separator + fileName
    val file = File(filePath)
    if (!file.exists()) {
        file.mkdir()
    }
    val file1 = File(filePath + File.separator + path)
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
 * 获取SD卡数据
 */
fun Any.getFilePath(fileName: String?, filePaths: String?): String? {
    val filePath = getSDPath() + File.separator + fileName
    val file = File(filePath)
    if (file.exists()) {
        val file1 = File(filePath + File.separator + filePaths)
        if (file1.exists()) {
            return file1.path
        } else {
            return null
        }
    } else {
        return null
    }


}

/**
 * 根据文件路径获取文件
 *
 * @param filePath 文件路径
 */
fun Any.getFileByPath(filePath: String): File? {
    return if (isSpace(filePath)) null else File(filePath)
}

/**
 * 判断文件是否存在
 *
 * @param filePath 文件路径
 */
fun Any.isFileExists(filePath: String): Boolean {
    return isFileExists(getFileByPath(filePath))
}

/**
 * 判断文件是否存在
 *
 * @param file 文件
 */
fun Any.isFileExists(file: File?): Boolean {
    return file != null && file.exists()
}

/**
 * 重命名文件
 *
 * @param filePath 文件路径
 * @param newName  新名称
 */
fun Any.rename(filePath: String, newName: String): Boolean {
    return rename(getFileByPath(filePath), newName)
}

/**
 * 重命名文件
 *
 * @param file    文件
 * @param newName 新名称
 */
fun Any.rename(file: File?, newName: String): Boolean {
    // 文件为空返回false
    if (file == null) return false
    // 文件不存在返回false
    if (!file.exists()) return false
    // 新的文件名为空返回false
    if (isSpace(newName)) return false
    // 如果文件名没有改变返回true
    if (newName == file.name) return true
    val newFile = File(file.parent + File.separator + newName)
    // 如果重命名的文件已存在返回false
    return !newFile.exists() && file.renameTo(newFile)
}

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 * @param filePath 文件路径
 */
fun Any.createOrExistsFile(filePath: String): Boolean {
    return createOrExistsFile(getFileByPath(filePath))
}

/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 */
fun Any.createOrExistsDir(file: File?): Boolean {
    // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 */
fun Any.createOrExistsFile(file: File?): Boolean {
    if (file == null) return false
    // 如果存在，是文件则返回true，是目录则返回false
    if (file.exists()) return file.isFile
    if (!createOrExistsDir(file.parentFile)) return false
    try {
        return file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }

}


/**
 * 删除文件
 *
 * @param srcFilePath 文件路径
 */
fun Any.deleteFile(srcFilePath: String): Boolean {
    return deleteFile(getFileByPath(srcFilePath))
}

/**
 * 删除文件
 *
 * @param file 文件
 */
fun Any.deleteFile(file: File?): Boolean {
    return file != null && (!file.exists() || file.isFile && file.delete())
}

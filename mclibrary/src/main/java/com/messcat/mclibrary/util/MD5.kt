package com.messcat.mclibrary.util

import android.util.Log
import java.security.MessageDigest

/**
 *   //操作符  shl 下面对Int和Long
 * var a: Int = 4
 * var shl: Int = a shl (1)  //Java中的左移运算符 <<
 * var shr: Int = a shr (1) //Java中的右移运算符 >>
 * var ushr: Int = a ushr (3) //无符号右移，高位补0 >>>
 * var and: Int = 2 and (4)   //按位与操作 &
 * var or: Int = 2 or (4) //按位或操作 |
 * var xor: Int = 2 xor (6)  //按位异或操作 ^
 * print("\nshl:" + shl + "\nshr:" + shr + " \nushr:" + ushr )
 * print( "\nand：" + and + "\nor:" + or + "\nxor:" + xor)
 * Created by Administrator on 2017/8/30 0030.
 */

private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

fun getMessageDigest(buffer: ByteArray): String? {
    val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    try {
        val mdTemp = MessageDigest.getInstance("MD5")
        mdTemp.update(buffer)
        val md = mdTemp.digest()
        val j = md.size
        val str = CharArray(j * 2)
        var k = 0
        for (i in 0..j - 1) {
            val byte0 = md[i]
            str[k++] = hexDigits[byte0.toInt() ushr (4) and 0xf]
            str[k++] = hexDigits[byte0.toInt() and 0xf]
        }
        return String(str)
    } catch (e: Exception) {
        return null
    }

}

fun byteArrayToHexString(b: ByteArray): String {
    val resultSb = StringBuilder()
    for (aB in b) {
        resultSb.append(byteToHexString(aB))
    }
    return resultSb.toString()
}

/**
 * 转换byte到16进制
 * @param b 要转换的byte
 * @return 16进制格式
 */
private fun byteToHexString(b: Byte): String {
    var n = b.toInt()
    if (n < 0) {
        n = 256 + n
    }
    val d1 = n / 16
    val d2 = n % 16
    return hexDigits[d1] + hexDigits[d2]
}

/**
 * MD5编码
 * @param origin 原始字符串
 * @return 经过MD5加密之后的结果
 */
fun MD5Encode(timestamp: String, origin: String): String? {
    var resultString: String? = null
    try {
        resultString = "加密的内容" + timestamp + origin
        val md = MessageDigest.getInstance("MD5")
        md.update(resultString!!.toByteArray(charset("UTF-8")))
        resultString = byteArrayToHexString(md.digest())
        Log.e("resultString", resultString)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return resultString
}

fun MD5Encode(origin: String): String? {
    var resultString: String? = null
    try {
        resultString = origin
        val md = MessageDigest.getInstance("MD5")
        md.update(resultString.toByteArray(charset("UTF-8")))
        resultString = byteArrayToHexString(md.digest())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return resultString
}
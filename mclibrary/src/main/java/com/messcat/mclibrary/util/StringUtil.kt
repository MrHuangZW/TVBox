package com.messcat.kotlin.utils

/**
 * Created by Administrator on 2017/8/23 0023.
 */
/**
 * 判断字符串是否为null或长度为0
 * @param s 待校验字符串
 * @return `true`: 空<br></br> `false`: 不为空
 */
fun Any.isEmpty(s: CharSequence?): Boolean = s == null || s.length == 0

/**
 * 判断字符串是否为null或全为空格
 *
 * @param s 待校验字符串
 * @return `true`: null或全空格<br></br> `false`: 不为null且不全空格
 */
fun Any.isSpace(s: String?): Boolean = s == null || s.trim { it <= ' ' }.length == 0


/**
 * null转为长度为0的字符串
 *
 * @param s 待转字符串
 * @return s为null转为长度为0字符串，否则不改变
 */
fun Any.null2Length0(s: String?): String = s ?: ""

/**
 * 返回字符串长度
 *
 * @param s 字符串
 * @return null返回0，其他返回自身长度
 */
fun Any.length(s: CharSequence?): Int = s?.length ?: 0

/**
 * 首字母大写
 *
 * @param s 待转字符串
 * @return 首字母大写字符串
 */
fun Any.upperFirstLetter(s: String): String? = if (isEmpty(s) || !Character.isLowerCase(s[0])) s else (s[0].toInt() - 32).toChar().toString() + s.substring(1)

/**
 * 首字母小写
 *
 * @param s 待转字符串
 * @return 首字母小写字符串
 */
fun Any.lowerFirstLetter(s: String): String? = if (isEmpty(s) || !Character.isUpperCase(s[0])) s else (s[0].toInt() + 32).toChar().toString() + s.substring(1)

/**
 * 反转字符串
 *
 * @param s 待反转字符串
 * @return 反转字符串
 */
fun Any.reverse(s: String): String {
    val len = length(s)
    if (len <= 1) return s
    val mid = len shr 1
    val chars = s.toCharArray()
    var c: Char
    for (i in 0..mid - 1) {
        c = chars[i]
        chars[i] = chars[len - i - 1]
        chars[len - i - 1] = c
    }
    return String(chars)
}

/**
 * 转化为半角字符
 *
 * @param s 待转字符串
 * @return 半角字符串
 */
fun Any.toDBC(s: String): String? {
    if (isEmpty(s)) return s
    val chars = s.toCharArray()
    var i = 0
    val len = chars.size
    while (i < len) {
        if (chars[i].toInt() == 12288) {
            chars[i] = ' '
        } else if (65281 <= chars[i].toInt() && chars[i].toInt() <= 65374) {
            chars[i] = (chars[i].toInt() - 65248).toChar()
        } else {
            chars[i] = chars[i]
        }
        i++
    }
    return String(chars)
}

/**
 * 转化为全角字符
 *
 * @param s 待转字符串
 * @return 全角字符串
 */
fun Any.toSBC(s: String): String? {
    if (isEmpty(s)) return s
    val chars = s.toCharArray()
    var i = 0
    val len = chars.size
    while (i < len) {
        if (chars[i] == ' ') {
            chars[i] = 12288.toChar()
        } else if (33 <= chars[i].toInt() && chars[i].toInt() <= 126) {
            chars[i] = (chars[i].toInt() + 65248).toChar()
        } else {
            chars[i] = chars[i]
        }
        i++
    }
    return String(chars)
}

/**
 * 判断字符串中是否有那个字符
 */
fun Any.isContains(content: String, contain: String): Boolean {
    if (content.contains(contain)) {
        return true
    } else {
        return false
    }
}
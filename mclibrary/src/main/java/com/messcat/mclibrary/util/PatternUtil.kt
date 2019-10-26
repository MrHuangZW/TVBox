package com.messcat.mclibrary.util

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Administrator on 2017/9/20 0020.
 */

fun Any.getEN(mobiles: String): String {
    val p = Pattern
            .compile("[^a-z^A-Z^0-9]")
    val m = p.matcher(mobiles)
    return m.replaceAll("")
}

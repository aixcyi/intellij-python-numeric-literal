package cn.aixcyi.plugin.pnl.utils

import java.awt.Color

/** 将颜色转换为 HTML 样式中的值，类似于 `rgb(255, 255, 255)` 。 */
fun Color.toHtmlRGB() = "rgb($red, $green, $blue)"

/** 将颜色转换为 HTML 样式中 `color` 属性的值，类似于 `color: rgb(255, 255, 255);` 。 */
fun Color?.toHtmlStyleCodeRGB() = if (this == null) "" else "color: ${toHtmlRGB()};"

/**
 * 计算比特中的下一个最高位，可以理解为二进制中的向上取整。
 *
 * 例如计算 `34`、`63`、`64` 都得到 `64`，而计算 `65`、`127`、`128` 都得到 `128` 。
 */
fun Int.nextHighestOneBit(): Int {
    val mid = takeHighestOneBit()
    if (mid == this)
        return this
    return mid shl 1
}

operator fun List<UInt>.compareTo(another: List<UInt>): Int {
    for ((a, b) in this.zip(another)) {
        val cmp = a.compareTo(b)
        if (cmp != 0)
            return cmp
    }
    return this.size.compareTo(another.size)
}
package cn.aixcyi.plugin.pnl.utils

import java.awt.Color

/** 将颜色转换为 HTML 样式中的值，类似于 `rgb(255, 255, 255)` 。 */
fun Color.toHtmlRGB() = "rgb($red, $green, $blue)"

/** 将颜色转换为 HTML 样式中 `color` 属性的值，类似于 `color: rgb(255, 255, 255);` 。 */
fun Color?.toHtmlStyleCodeRGB() = if (this == null) "" else "color: ${toHtmlRGB()};"

package cn.aixcyi.plugin.pnl.utils

import java.awt.Color

/** 将颜色转换为 HTML 样式中的值，类似于 `rgb(255, 255, 255)` 。 */
fun Color.toHtmlRgb() = "rgb($red, $green, $blue)"

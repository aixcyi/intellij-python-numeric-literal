package cn.aixcyi.plugin.pnl.utils

import cn.aixcyi.plugin.pnl.Zoo.message
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.jetbrains.python.highlighting.PyHighlighter
import com.jetbrains.python.psi.PyNumericLiteralExpression
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Color

/**
 * Python 整数字面值文档构建器。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class IntegerLiteralDocBuilder private constructor(element: PyNumericLiteralExpression) {

    companion object {
        @JvmStatic
        fun getInstance(element: PyNumericLiteralExpression): IntegerLiteralDocBuilder? {
            if (element.bigIntegerValue == null)
                return null
            return IntegerLiteralDocBuilder(element)
        }
    }

    /** Python 数字字面值配色。 */
    private val numberColor: Color = EditorColorsManager.getInstance()
        .schemeForCurrentUITheme
        .getAttributes(PyHighlighter.PY_NUMBER)
        .foregroundColor

    private val codeStyle = "text-align: right; color: ${numberColor.toHtmlRgb()};"
    private val textStyle = "text-align: right;"
    private val integer = element.bigIntegerValue!!
    private val hexLow = integer.toString(16).lowercase()
    private val hex = integer.toString(16).uppercase()
    private val oct = integer.toString(8)
    private val bin = integer.toString(2)

    fun buildMultiRadixTable() = buildString {
        appendHTML().html {
            body {
                multiRadixTable()
            }
        }
    }

    private fun BODY.multiRadixTable() = table {
        tr {
            td { +message("text.Decimal") }
            td { style = codeStyle; code { +"$integer" } }
        }
        tr {
            td { +message("text.HexadecimalValue") }
            td { style = textStyle; code { +hex } }
        }
        tr {
            td { +message("text.HexadecimalCode") }
            td { style = codeStyle; code { +"0x$hex" } }
        }
        if (hexLow != hex) {
            tr {
                td { +message("text.HexadecimalValue") }
                td { style = textStyle; code { +hexLow } }
            }
            tr {
                td { +message("text.HexadecimalCode") }
                td { style = codeStyle; code { +"0x$hexLow" } }
            }
        }
        tr {
            td { +message("text.OctalValue") }
            td { style = textStyle; code { +oct } }
        }
        tr {
            td { +message("text.OctalCode") }
            td { style = codeStyle; code { +"0o$oct" } }
        }
        tr {
            td { +message("text.BinaryValue") }
            td { style = textStyle; code { +bin } }
        }
        tr {
            td { +message("text.BinaryCode") }
            td { style = codeStyle; code { +"0b$bin" } }
        }
    }
}
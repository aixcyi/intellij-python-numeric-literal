package cn.aixcyi.plugin.pnl.utils

import cn.aixcyi.plugin.pnl.Zoo.message
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.highlighting.PyHighlighter
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.PyNumericLiteralExpression
import com.jetbrains.python.psi.PyPrefixExpression
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.awt.Color
import java.math.BigInteger

/**
 * Python 整数字面值文档构建器。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class PyIntDocumentationBuilder private constructor(integer: BigInteger) {

    companion object {
        @JvmStatic
        fun getInstance(expression: PyNumericLiteralExpression): PyIntDocumentationBuilder? {
            if (expression.bigIntegerValue == null)
                return null
            val (_, int) = evaluate(Pair(expression, expression.bigIntegerValue!!))
            return PyIntDocumentationBuilder(int)
        }

        private fun evaluate(pair: Pair<PyExpression, BigInteger>): Pair<PyExpression, BigInteger> {
            val expression = pair.first.parent
            if (expression !is PyPrefixExpression)
                return pair
            return evaluate(
                Pair(
                    expression,
                    when (expression.operator) {
                        PyTokenTypes.PLUS -> pair.second
                        PyTokenTypes.MINUS -> pair.second.negate()
                        PyTokenTypes.TILDE -> pair.second.not()
                        else -> pair.second
                    }
                )
            )
        }
    }

    private val wrapper = IntegerWrapper(integer)
    private val numberColor: Color = EditorColorsManager.getInstance()
        .schemeForCurrentUITheme
        .getAttributes(PyHighlighter.PY_NUMBER)
        .foregroundColor

    fun buildMultiRadixTable() = buildString {
        appendHTML().html {
            body {
                multiRadixTable()
            }
        }
    }

    private fun BODY.multiRadixTable() = table("sections") {
        val codeStyle = "text-align: right; color: ${numberColor.toHtmlRGB()};"
        val textStyle = "text-align: right;"
        tr {
            td { +message("text.Decimal") }
            td { style = codeStyle; code { +wrapper.toRadix(Radix.DEC) } }
        }
        tr {
            td { +message("text.HexadecimalValue") }
            td {
                val hexUpper = wrapper.toRadix(Radix.HEX).uppercase()
                val hexLower = wrapper.toRadix(Radix.HEX).lowercase()
                style = textStyle
                code { +hexUpper }
                if (hexUpper != hexLower) {
                    br()
                    code { +hexLower }
                }
            }
        }
        tr {
            td { +message("text.HexadecimalCode") }
            td {
                val hexUpper = wrapper.toLiteral(Radix.HEX, String::uppercase)
                val hexLower = wrapper.toLiteral(Radix.HEX, String::lowercase)
                style = codeStyle
                code { +hexUpper }
                if (hexUpper != hexLower) {
                    br()
                    code { +hexLower }
                }
            }
        }
        tr {
            td { +message("text.OctalValue") }
            td { style = textStyle; code { +wrapper.toRadix(Radix.OCT) } }
        }
        tr {
            td { +message("text.OctalCode") }
            td { style = codeStyle; code { +wrapper.toLiteral(Radix.OCT) } }
        }
        tr {
            td { +message("text.BinaryValue") }
            td { style = textStyle; code { +wrapper.toRadix(Radix.BIN) } }
        }
        tr {
            td { +message("text.BinaryCode") }
            td { style = codeStyle; code { +wrapper.toLiteral(Radix.BIN) } }
        }
    }
}
package cn.aixcyi.plugin.pnl.utils

import cn.aixcyi.plugin.pnl.Zoo.message
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.highlighting.PyHighlighter
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.PyNumericLiteralExpression
import com.jetbrains.python.psi.PyPrefixExpression
import kotlinx.html.*
import java.awt.Color
import java.math.BigInteger

/**
 * Python 整数字面值文档构建器。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class PyIntDocumentationBuilder
private constructor(private val expression: PyExpression, integer: BigInteger) {

    companion object {
        @JvmStatic
        fun getInstance(expression: PyNumericLiteralExpression): PyIntDocumentationBuilder? {
            if (expression.bigIntegerValue == null)
                return null
            val (exp, int) = evaluate(Pair(expression, expression.bigIntegerValue!!))
            return PyIntDocumentationBuilder(exp, int)
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
    private val scheme = EditorColorsManager.getInstance().schemeForCurrentUITheme
    private val numberColor = getColor(PyHighlighter.PY_NUMBER)
    private val operationColor = getColor(
        PyHighlighter::class.java.getDeclaredField("PY_OPERATION_SIGN")
            .apply { isAccessible = true }  // COMPATIBLE: 222.* 时是非 public 的
            .get(null) as TextAttributesKey
    )

    private fun getColor(key: TextAttributesKey): Color? = scheme.getAttributes(key).foregroundColor

    fun buildMultiRadixTable() = MeowDocumentationBuilder.getInstance()
        .definition {
            val regex = "^([-+~]*)([0-9A-Za-z]+)$".toRegex()
            val result = regex.find(expression.text)!!
            span {
                style = operationColor.toHtmlStyleCodeRGB()
                +result.groupValues[1]
            }
            span {
                style = numberColor.toHtmlStyleCodeRGB()
                +result.groupValues[2]
            }
        }
        .contentTable {
            val codeStyle = "text-align: right; ${numberColor.toHtmlStyleCodeRGB()}"
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
        .build()
}
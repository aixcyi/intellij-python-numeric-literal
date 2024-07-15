package cn.aixcyi.plugin.pnl.utils

import cn.aixcyi.plugin.pnl.Zoo.message
import cn.aixcyi.plugin.pnl.storage.PNLSettings
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
    private val state = PNLSettings.getInstance().state
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
            val decCode = wrapper.toLiteral(Radix.DEC, state.decCodeGroupWidth)
            val decSize = wrapper.toRadix(Radix.DEC, state.decSizeGroupWidth, state.decSizeDelimiter)
            val hexCodeUpper = wrapper.toLiteral(Radix.HEX, state.hexCodeGroupWidth, String::uppercase)
            val hexCodeLower = wrapper.toLiteral(Radix.HEX, state.hexCodeGroupWidth, String::lowercase)
            val hexSizeUpper =
                wrapper.toRadix(Radix.HEX, state.hexSizeGroupWidth, state.hexSizeDelimiter).uppercase()
            val hexSizeLower =
                wrapper.toRadix(Radix.HEX, state.hexSizeGroupWidth, state.hexSizeDelimiter).lowercase()
            tr {
                td { +message("text.DecimalSize") }
                td {
                    style = if (decCode != decSize) textStyle else codeStyle
                    code { +decCode }
                }
            }
            if (decCode != decSize)
                tr {
                    td { +message("text.DecimalCode") }
                    td {
                        style = codeStyle
                        code { +decCode }
                    }
                }
            tr {
                td { +message("text.HexadecimalSize") }
                td {
                    style = textStyle
                    code { +hexSizeUpper }
                    if (hexSizeUpper != hexSizeLower) {
                        br()
                        code { +hexSizeLower }
                    }
                }
            }
            tr {
                td { +message("text.HexadecimalCode") }
                td {
                    style = codeStyle
                    code { +hexCodeUpper }
                    if (hexCodeUpper != hexCodeLower) {
                        br()
                        code { +hexCodeLower }
                    }
                }
            }
            tr {
                td { +message("text.OctalSize") }
                td {
                    style = textStyle
                    code { +wrapper.toRadix(Radix.OCT, state.octSizeGroupWidth, state.octSizeDelimiter) }
                }
            }
            tr {
                td { +message("text.OctalCode") }
                td {
                    style = codeStyle
                    code { +wrapper.toLiteral(Radix.OCT, state.octCodeGroupWidth) }
                }
            }
            tr {
                td { +message("text.BinarySize") }
                td {
                    style = textStyle
                    code { +wrapper.toRadix(Radix.BIN, state.binSizeGroupWidth, state.binSizeDelimiter) }
                }
            }
            tr {
                td { +message("text.BinaryCode") }
                td {
                    style = codeStyle
                    code { +wrapper.toLiteral(Radix.BIN, state.binCodeGroupWidth) }
                }
            }
        }
        .build()
}
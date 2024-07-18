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
private constructor(expression: PyExpression, integer: BigInteger) {

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
    private val literal = run {  // "-2077" -> Pair("-", "2077")
        val regex = "^([-+~]*)([0-9A-Za-z]+)$".toRegex()
        val result = regex.find(expression.text)!!
        object {
            val symbols = result.groupValues[1]
            val digits = result.groupValues[2]
        }
    }

    private fun getColor(key: TextAttributesKey): Color? = scheme.getAttributes(key).foregroundColor

    fun buildMultiRadixTable() = MeowDocumentationBuilder.getInstance()
        .definition {
            span {
                style = operationColor.toHtmlStyleCodeRGB()
                +literal.symbols
            }
            span {
                style = numberColor.toHtmlStyleCodeRGB()
                +literal.digits
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
                    snippet {
                        style = if (decCode != decSize) textStyle else codeStyle
                        +decSize
                    }
                }
            }
            if (decCode != decSize)
                tr {
                    td { +message("text.DecimalCode") }
                    td {
                        snippet {
                            style = codeStyle
                            +decCode
                        }
                    }
                }
            tr {
                td { +message("text.HexadecimalSize") }
                td {
                    snippet {
                        style = textStyle
                        +hexSizeUpper
                        if (hexSizeUpper != hexSizeLower) {
                            br()
                            +hexSizeLower
                        }
                    }
                }
            }
            tr {
                td { +message("text.HexadecimalCode") }
                td {
                    snippet {
                        style = codeStyle
                        +hexCodeUpper
                        if (hexCodeUpper != hexCodeLower) {
                            br()
                            +hexCodeLower
                        }
                    }
                }
            }
            tr {
                td { +message("text.OctalSize") }
                td {
                    snippet {
                        style = textStyle
                        +wrapper.toRadix(Radix.OCT, state.octSizeGroupWidth, state.octSizeDelimiter)
                    }
                }
            }
            tr {
                td { +message("text.OctalCode") }
                td {
                    snippet {
                        style = codeStyle
                        +wrapper.toLiteral(Radix.OCT, state.octCodeGroupWidth)
                    }
                }
            }
            tr {
                td { +message("text.BinarySize") }
                td {
                    snippet {
                        style = textStyle
                        +wrapper.toRadix(Radix.BIN, state.binSizeGroupWidth, state.binSizeDelimiter)
                    }
                }
            }
            tr {
                td { +message("text.BinaryCode") }
                td {
                    snippet {
                        style = codeStyle
                        +wrapper.toLiteral(Radix.BIN, state.binCodeGroupWidth)
                    }
                }
            }
        }
        .build()

    fun buildBitView() = MeowDocumentationBuilder.getInstance()
        .content {
            val bits = wrapper.toBits(state.viewBitDepth, state.viewUnsigned)
            val snippet = bits
                .chunked(4)
                .chunked(if (bits.length > 64) 8 else 4)
                .joinToString(separator = "\n") { it.joinToString(separator = " ") }
                .replace("1", "<span style=\"${numberColor.toHtmlStyleCodeRGB()}\">1</span>")
            pre {
                unsafe {
                    raw(snippet)
                }
            }
        }
        .build()
}
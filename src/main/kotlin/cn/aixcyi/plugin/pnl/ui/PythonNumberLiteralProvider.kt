package cn.aixcyi.plugin.pnl.ui

import cn.aixcyi.plugin.pnl.Zoo.message
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.python.highlighting.PyHighlighter
import com.jetbrains.python.psi.PyNumericLiteralExpression
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.math.BigInteger

/**
 * 在悬浮文档中展示一个整数的其它进制。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see <a href="https://plugins.jetbrains.com/docs/intellij/code-documentation.html">Documentation | IntelliJ Platform Plugin SDK</a>
 */
class PythonNumberLiteralProvider : AbstractDocumentationProvider() {

    override fun getCustomDocumentationElement(
        editor: Editor,
        file: PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
        val element = contextElement?.parent
        if (element !is PyNumericLiteralExpression)
            return null
        return element
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element !is PyNumericLiteralExpression)
            return null
        if (element.isIntegerLiteral) {
            return explainInteger(element.bigIntegerValue ?: return null)
        } else {
            // 未来再决定是否处理小数。
            return null
        }
    }

    private fun explainInteger(integer: BigInteger): String {
        val color = EditorColorsManager.getInstance()
            .schemeForCurrentUITheme
            .getAttributes(PyHighlighter.PY_NUMBER)
            .foregroundColor

        val codeStyle = "text-align: right; color: rgb(${color.red}, ${color.green}, ${color.blue});"
        val textStyle = "text-align: right;"
        val hexLow = integer.toString(16).lowercase()
        val hex = integer.toString(16).uppercase()
        val oct = integer.toString(8)
        val bin = integer.toString(2)
        return buildString {
            appendHTML().html {
                body {
                    table {
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
            }
        }
    }
}
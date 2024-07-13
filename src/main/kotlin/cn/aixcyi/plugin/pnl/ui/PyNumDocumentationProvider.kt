package cn.aixcyi.plugin.pnl.ui

import cn.aixcyi.plugin.pnl.utils.IntegerLiteralDocBuilder
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyNumericLiteralExpression

/**
 * Python 数字字面值文档。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see <a href="https://plugins.jetbrains.com/docs/intellij/code-documentation.html">Documentation | IntelliJ Platform Plugin SDK</a>
 */
class PyNumDocumentationProvider : AbstractDocumentationProvider() {

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
            return IntegerLiteralDocBuilder.getInstance(element)?.buildMultiRadixTable()
        } else {
            // 未来再决定是否处理小数。
            return null
        }
    }
}
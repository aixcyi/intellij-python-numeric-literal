package cn.aixcyi.plugin.pnl.documentation

import com.intellij.model.Symbol
import com.intellij.model.psi.ImplicitReferenceProvider
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyNumericLiteralExpression

/**
 * Python 数字字面值参考。
 *
 * @author <a href="https://github.com/aixcyi/">砹小翼</a>
 */
class PyNumericLiteralReferenceProvider : ImplicitReferenceProvider {

    override fun resolveAsReference(element: PsiElement): MutableCollection<out Symbol> {
        if (element !is PyNumericLiteralExpression)
            return mutableListOf()
        return mutableListOf(
            // PsiSymbolService.getInstance().asSymbol(element)
        )
    }
}
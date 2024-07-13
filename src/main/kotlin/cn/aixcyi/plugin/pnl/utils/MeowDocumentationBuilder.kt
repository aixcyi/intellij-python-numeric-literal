package cn.aixcyi.plugin.pnl.utils

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.DocumentationProvider
import kotlinx.html.*
import kotlinx.html.stream.appendHTML

/**
 * 提供对 [DocumentationProvider] 硬编码的简易封装。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see [DocumentationMarkup]
 */
@Suppress("unused")
class MeowDocumentationBuilder private constructor() {

    companion object {
        const val DEFINITION_CLASS = "definition"
        const val CONTENT_CLASS = "content"
        const val SECTION_TABLE_CLASS = "sections"
        const val SECTION_TD_CLASS = "section"
        const val GRAYED_SPAN_CLASS = "grayed"

        fun getInstance(): MeowDocumentationBuilder {
            return MeowDocumentationBuilder()
        }
    }

    private val blocks = mutableListOf<BODY.() -> Unit>()

    fun definition(block: PRE.() -> Unit): MeowDocumentationBuilder {
        val typedBlock: BODY.() -> Unit = {
            div(DEFINITION_CLASS) {
                pre {
                    block()
                }
            }
        }
        blocks.add(typedBlock)
        return this
    }

    fun content(block: DIV.() -> Unit): MeowDocumentationBuilder {
        val typedBlock: BODY.() -> Unit = {
            div(CONTENT_CLASS) {
                block()
            }
        }
        blocks.add(typedBlock)
        return this
    }

    fun contentTable(block: TABLE.() -> Unit): MeowDocumentationBuilder {
        val typedBlock: BODY.() -> Unit = {
            div(CONTENT_CLASS) {
                table {
                    block()
                }
            }
        }
        blocks.add(typedBlock)
        return this
    }

    fun build() = buildString {
        appendHTML().html {
            body {
                for (block in blocks)
                    block()
            }
        }
    }
}
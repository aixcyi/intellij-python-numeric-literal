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
        blocks.add {
            div(DEFINITION_CLASS) {
                pre {
                    block()
                }
            }
        }
        return this
    }

    fun content(block: DIV.() -> Unit): MeowDocumentationBuilder {
        blocks.add {
            div(CONTENT_CLASS) {
                block()
            }
        }
        return this
    }

    fun contentTable(block: TABLE.() -> Unit): MeowDocumentationBuilder {
        blocks.add {
            div(CONTENT_CLASS) {
                table {
                    block()
                }
            }
        }
        return this
    }

    fun build() = buildString {
        appendHTML(prettyPrint = false, xhtmlCompatible = false).html {
            body {
                for (block in blocks)
                    block()
            }
        }
    }
}

/**
 * 根据 IDE 版本选择构建 `<code></code>` 或者 `<pre></pre>` 。
 *
 * @see [AppInfo.isSupportPreformattedTag]
 */
fun FlowOrPhrasingContent.snippet(classes: String? = null, block: HtmlBlockInlineTag.() -> Unit) {
    val appInfo = AppInfo.getInstance()
    if (appInfo.isSupportPreformattedTag())
        (this as FlowContent).pre(classes, block)
    else
        code(classes, block)
}
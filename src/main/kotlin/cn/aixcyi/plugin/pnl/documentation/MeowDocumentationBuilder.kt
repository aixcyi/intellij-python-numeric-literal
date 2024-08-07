package cn.aixcyi.plugin.pnl.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.application.ApplicationInfo
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import net.aixcyi.utils.Version

/**
 * 提供对 [DocumentationProvider] 硬编码的简易封装。
 *
 * @author <a href="https://github.com/aixcyi/">砹小翼</a>
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

        /**
         * @see [ApplicationInfo.isSupportPreformattedTag]
         */
        val VERSION_JETBRAINS_241 = Version("JetBrains", "2024", "1")

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
                // TODO: 2024+ 这里会空出一行
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
 * Documentation 是否支持嵌套 `<pre></pre>` 。
 *
 * 1. 旧版本 IDE 对 `<code>` 的渲染效果与 HTML 的 `<pre>` 一致，而 `<pre>` 嵌套时无法渲染任何内容。
 * 2. 2024.1+ 的 IDE 对 `<pre>` 和 `<code>` 与 HTML 一致，二者都可以将文本按照等宽字体渲染，但前者没有 “方框”，更为美观。
 */
fun ApplicationInfo.isSupportPreformattedTag(): Boolean {
    val version = Version(shortCompanyName ?: "", *strictVersion.split('.').toTypedArray())
    return version >= MeowDocumentationBuilder.VERSION_JETBRAINS_241
}

/**
 * 根据 IDE 版本选择构建 `<code></code>` 或者 `<pre></pre>` 。
 *
 * @see [ApplicationInfo.isSupportPreformattedTag]
 */
fun FlowOrPhrasingContent.snippet(classes: String? = null, block: HtmlBlockInlineTag.() -> Unit) {
    val appInfo = ApplicationInfo.getInstance()
    if (appInfo.isSupportPreformattedTag())
        (this as FlowContent).pre(classes, block)
    else
        code(classes, block)
}
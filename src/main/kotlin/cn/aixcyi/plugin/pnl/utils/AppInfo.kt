package cn.aixcyi.plugin.pnl.utils

import com.intellij.openapi.application.ApplicationInfo

/**
 * IDE 信息。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class AppInfo private constructor(info: ApplicationInfo) {

    companion object {
        const val COMPANY_JETBRAINS = "JetBrains"

        @JvmField
        val VERSION_JETBRAINS_241 = listOf(2024u, 1u)

        @JvmStatic
        fun getInstance(): AppInfo {
            val info = ApplicationInfo.getInstance()
            return AppInfo(info)
        }
    }

    // 不同公司有不同的版本号递增规则，结合二者才能加以区分

    val version = eval { info.strictVersion.split('.').map(String::toUInt) } ?: listOf(0u, 0u)
    val company = info.shortCompanyName

    /**
     * Documentation 是否支持嵌套 `<pre></pre>` 。
     *
     * 1. 旧版本 IDE 对 `<code>` 的渲染效果与 HTML 的 `<pre>` 一致，而 `<pre>` 嵌套时无法渲染任何内容。
     * 2. 2024.1+ 的 IDE 对 `<pre>` 和 `<code>` 与 HTML 一致，二者都可以将文本按照等宽字体渲染，但前者没有 “方框”，更为美观。
     */
    fun isSupportPreformattedTag(): Boolean {
        if (company == COMPANY_JETBRAINS)
            return version >= VERSION_JETBRAINS_241
        return false
    }
}
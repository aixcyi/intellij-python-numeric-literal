package net.aixcyi.utils

import java.math.BigInteger
import kotlin.math.min

/**
 * 版本号。（用于通用比较）
 *
 * ```kotlin
 * import com.intellij.openapi.application.ApplicationInfo
 *
 * val appInfo = ApplicationInfo.getInstance()
 * val MILESTONE = Version("2024", "1")
 * val currVersion = Version(*strictVersion.split('.').toTypedArray())
 * val isNewVersion = currVersion > MILESTONE
 * ```
 *
 * @author <a href="https://github.com/aixcyi/">砹小翼</a>
 */
class Version(vararg val parts: String) {

    /**
     * 整数比较器。
     *
     * - 用于将某个部分转换为整数，以实现 `"2.8" < "2.13“` 这种比较。
     * - 默认转换为 [UShort] 比较，可以重写改用 [ULong] 乃至 [BigInteger] 。
     */
    var intComparator = { a: String, b: String -> (a.toUShort()).compareTo(b.toUShort()) }

    operator fun compareTo(another: Version): Int {
        for (i in 0..min(parts.size, another.parts.size)) {
            val result = eval {
                intComparator(parts[i], another.parts[i])
            } ?: run {
                parts[i].compareTo(another.parts[i])
            }
            if (result != 0)
                return result
        }
        return parts.size.compareTo(another.parts.size)
    }
}
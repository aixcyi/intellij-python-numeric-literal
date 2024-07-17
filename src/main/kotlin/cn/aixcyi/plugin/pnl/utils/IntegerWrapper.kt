package cn.aixcyi.plugin.pnl.utils

import java.math.BigInteger
import kotlin.math.max

/**
 * 常见的进位制。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
enum class Radix(val radix: Int, val prefix: String) {
    BIN(2, "0b"),
    OCT(8, "0o"),
    HEX(16, "0x"),
    DEC(10, "");
}

/**
 * 整数包装器，用于控制前缀、进位、分组的输出。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 * @see [Radix]
 */
class IntegerWrapper(private val integer: BigInteger) {

    private val isNegative = integer.signum() < 0

    /**
     * 转换为不同进位制的整数。
     *
     * @param r 目标进位制。
     * @param width 分组宽度。`3` 表示每三位分一组、`4` 表示每四位分一组，`null` 或小于 `1` 则不分组。
     * @param delimiter 分组之间的分隔符。不提供，或提供空字符串，则不会触发分组操作。
     * @return 不包含类似 `0x` 的前缀；会自动转换为大写字符。
     */
    fun toRadix(r: Radix = Radix.DEC, width: Int? = null, delimiter: String = ""): String {
        if (width == null || width < 1 || delimiter.isEmpty())
            return integer.toString(r.radix).uppercase()

        val body = integer.abs().toString(r.radix)
            .reversed()
            .chunked(width)
            .reversed()
            .joinToString(delimiter) { it.reversed() }
            .uppercase()
        return if (isNegative) "-$body" else body
    }

    /**
     * 转换为不同进位制的字面值。
     *
     * @param r 目标进位制。
     * @param width 分组宽度。`3` 表示每三位分一组、`4` 表示每四位分一组，`null` 或小于 `1` 则不分组。分组之间的分隔符固定为 `_` 。
     * @param transform 对整数（不包含前缀）进行转换，比如大小写转换。
     * @return 根据不同的 [r] 自动添加类似 `0x` 的前缀。
     */
    fun toLiteral(r: Radix = Radix.DEC, width: Int? = null, transform: (String.() -> String)? = null): String {
        val prefix = if (isNegative) "-${r.prefix}" else r.prefix
        var body = integer.abs().toString(r.radix)
        if (transform != null)
            body = body.transform()
        if (width != null && width > 0)
            body = body.reversed().chunked(width).reversed().joinToString("_") { it.reversed() }
        return "$prefix$body"
    }

    /**
     * 转换为二进制码。
     *
     * @param depth 最低位深度。不足时优先填充到这个长度，然后按照 `8`、`16`、`32`、`64`…… 的规律递增。
     * @param unsigned `false` 会将 **负整数** 转换为二进制补码，而 `true` 会将 **绝对值** 转换为二进制码。
     */
    fun toBits(depth: Int? = null, unsigned: Boolean = false) = integer
        .let {
            if (unsigned) it.abs()
            else it
        }
        .toByteArray()
        .joinToString("") { it.toUByte().toString(2).padStart(8, '0') }
        .run {
            padStart(
                length = max(this.length, depth ?: 0).nextHighestOneBit(),
                padChar = if (isNegative) '1' else '0',
            )
        }
}
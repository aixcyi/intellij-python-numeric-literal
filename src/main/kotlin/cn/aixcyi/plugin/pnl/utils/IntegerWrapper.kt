package cn.aixcyi.plugin.pnl.utils

import java.math.BigInteger

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

    /** 转换为不同进位制的整数。 */
    fun toRadix(r: Radix = Radix.DEC): String {
        return integer.toString(r.radix).uppercase()
    }

    /** 转换为不同进位制的字面值，能够附加 `0x` 等前缀，并对原来的整数（不包括前缀）进行 [transform] 转换。 */
    fun toLiteral(r: Radix = Radix.DEC, transform: (String.() -> String)? = null): String {
        val prefix = if (isNegative) "-${r.prefix}" else r.prefix
        val body = integer.abs().toString(r.radix).let {
            if (transform != null) it.transform() else it
        }
        return "$prefix$body"
    }
}
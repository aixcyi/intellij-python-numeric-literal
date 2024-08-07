package cn.aixcyi.plugin.pnl.settings

import cn.aixcyi.plugin.pnl.Zoo
import com.intellij.openapi.components.*

/**
 * 整数字面值分组设置。
 *
 * @author <a href="https://github.com/aixcyi/">砹小翼</a>
 */
@Service(Service.Level.APP)
@State(name = Zoo.XmlComponent.PNL, storages = [Storage(Zoo.PLUGIN_STORAGE)])
class PyNumericLiteralSettings : SimplePersistentStateComponent<PyNumericLiteralSettings.State>(State()) {

    companion object {
        fun getInstance() = service<PyNumericLiteralSettings>()
    }

    /**
     * @see [PyNumericLiteralSettingsComponent.Companion]
     */
    class State : BaseState() {
        // Ctrl-Hover 设置
        var viewBitDepth by property(32)
        var viewUnsigned by property(false)

        // 字面值
        var hexCodeGroupWidth by property<Int?>(null) { it == null }
        var decCodeGroupWidth by property<Int?>(null) { it == null }
        var octCodeGroupWidth by property<Int?>(null) { it == null }
        var binCodeGroupWidth by property<Int?>(null) { it == null }

        // 数值
        var hexSizeGroupWidth by property<Int?>(null) { it == null }
        var decSizeGroupWidth by property<Int?>(null) { it == null }
        var octSizeGroupWidth by property<Int?>(null) { it == null }
        var binSizeGroupWidth by property<Int?>(null) { it == null }
        var hexSizeDelimiter by property("") { it == "" }
        var decSizeDelimiter by property("") { it == "" }
        var octSizeDelimiter by property("") { it == "" }
        var binSizeDelimiter by property("") { it == "" }
    }
}
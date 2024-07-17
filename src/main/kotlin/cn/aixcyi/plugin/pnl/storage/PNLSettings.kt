package cn.aixcyi.plugin.pnl.storage

import cn.aixcyi.plugin.pnl.Zoo
import cn.aixcyi.plugin.pnl.ui.PNLSettingsComponent
import com.intellij.openapi.components.*

/**
 * 整数字面值分组设置。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
@Service(Service.Level.APP)
@State(name = Zoo.XmlComponent.PNL, storages = [Storage(Zoo.PLUGIN_STORAGE)])
class PNLSettings : SimplePersistentStateComponent<PNLSettings.State>(State()) {

    companion object {
        fun getInstance() = service<PNLSettings>()
    }

    /**
     * @see [PNLSettingsComponent.Companion]
     */
    class State : BaseState() {
        // Ctrl-Hover 设置
        var bitDepth: Int = 32

        // 字面值
        var hexCodeGroupWidth: Int? = null
        var decCodeGroupWidth: Int? = null
        var octCodeGroupWidth: Int? = null
        var binCodeGroupWidth: Int? = null

        // 数值
        var hexSizeGroupWidth: Int? = null
        var decSizeGroupWidth: Int? = null
        var octSizeGroupWidth: Int? = null
        var binSizeGroupWidth: Int? = null
        var hexSizeDelimiter: String = ""
        var decSizeDelimiter: String = ""
        var octSizeDelimiter: String = ""
        var binSizeDelimiter: String = ""
    }
}
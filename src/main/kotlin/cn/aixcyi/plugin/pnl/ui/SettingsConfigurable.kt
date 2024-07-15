package cn.aixcyi.plugin.pnl.ui

import cn.aixcyi.plugin.pnl.Zoo.message
import cn.aixcyi.plugin.pnl.storage.PNLSettings
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.dsl.builder.bindItemNullable
import com.intellij.ui.dsl.builder.panel
import javax.swing.JList

/**
 * 插件设置首页。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class SettingsConfigurable : SearchableConfigurable {

    companion object {
        const val ID = "HooTool.PNL.SettingsConfigurable"
    }

    private val state = PNLSettings.getInstance().copyState()
    private val renderer = object : ColoredListCellRenderer<Int?>() {
        override fun customizeCellRenderer(
            list: JList<out Int?>, value: Int?, index: Int, selected: Boolean, hasFocus: Boolean
        ) {
            this.append(value?.toString() ?: "（不分组）")
        }
    }

    override fun getId() = ID

    override fun getDisplayName() = message("configurable.Settings.display_name")

    override fun createComponent() = panel {
        group("整数字面值 - 分组宽度") {
            row("十六进制") {
                comboBox(listOf(null, 2, 4), renderer)
                    .bindItemNullable(state::hexCodeGroupWidth)
            }
            row("十进制") {
                comboBox(listOf(null, 3, 4), renderer)
                    .bindItemNullable(state::decCodeGroupWidth)
            }
            row("八进制") {
                comboBox(listOf(null, 2, 3, 4), renderer)
                    .bindItemNullable(state::octCodeGroupWidth)
            }
            row("二进制") {
                comboBox(listOf(null, 4, 8, 16), renderer)
                    .bindItemNullable(state::binCodeGroupWidth)
            }
        }
    }

    override fun isModified(): Boolean {
        println(state.hexCodeGroupWidth)
        println(state.decCodeGroupWidth)
        println(state.octCodeGroupWidth)
        println(state.binCodeGroupWidth)
        println(PNLSettings.getInstance().state.hexCodeGroupWidth == state.hexCodeGroupWidth)
        println(PNLSettings.getInstance().state.decCodeGroupWidth == state.decCodeGroupWidth)
        println(PNLSettings.getInstance().state.octCodeGroupWidth == state.octCodeGroupWidth)
        println(PNLSettings.getInstance().state.binCodeGroupWidth == state.binCodeGroupWidth)
        println("--------------------------------")
        return PNLSettings.getInstance().state != this.state
    }

    override fun apply() {
        PNLSettings.getInstance().loadState(state)
    }
}
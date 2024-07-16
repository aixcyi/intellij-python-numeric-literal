package cn.aixcyi.plugin.pnl.ui

import cn.aixcyi.plugin.pnl.Zoo.message
import cn.aixcyi.plugin.pnl.storage.PNLSettings
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.panel
import javax.swing.JList

/**
 * 插件设置首页组件。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class PNLSettingsComponent {

    private lateinit var myHexCodeGroupWidth: ComboBox<Int?>
    private lateinit var myDecCodeGroupWidth: ComboBox<Int?>
    private lateinit var myOctCodeGroupWidth: ComboBox<Int?>
    private lateinit var myBinCodeGroupWidth: ComboBox<Int?>
    private lateinit var myHexSizeGroupWidth: ComboBox<Int?>
    private lateinit var myDecSizeGroupWidth: ComboBox<Int?>
    private lateinit var myOctSizeGroupWidth: ComboBox<Int?>
    private lateinit var myBinSizeGroupWidth: ComboBox<Int?>
    private lateinit var myHexSizeDelimiter: JBTextField
    private lateinit var myDecSizeDelimiter: JBTextField
    private lateinit var myOctSizeDelimiter: JBTextField
    private lateinit var myBinSizeDelimiter: JBTextField

    var state
        get() = PNLSettings.State().apply {
            hexCodeGroupWidth = myHexCodeGroupWidth.item
            decCodeGroupWidth = myDecCodeGroupWidth.item
            octCodeGroupWidth = myOctCodeGroupWidth.item
            binCodeGroupWidth = myBinCodeGroupWidth.item
            hexSizeGroupWidth = myHexSizeGroupWidth.item
            decSizeGroupWidth = myDecSizeGroupWidth.item
            octSizeGroupWidth = myOctSizeGroupWidth.item
            binSizeGroupWidth = myBinSizeGroupWidth.item
            hexSizeDelimiter = myHexSizeDelimiter.text
            decSizeDelimiter = myDecSizeDelimiter.text
            octSizeDelimiter = myOctSizeDelimiter.text
            binSizeDelimiter = myBinSizeDelimiter.text
        }
        set(state) {
            myHexCodeGroupWidth.item = state.hexCodeGroupWidth
            myDecCodeGroupWidth.item = state.decCodeGroupWidth
            myOctCodeGroupWidth.item = state.octCodeGroupWidth
            myBinCodeGroupWidth.item = state.binCodeGroupWidth
            myHexSizeGroupWidth.item = state.hexSizeGroupWidth
            myDecSizeGroupWidth.item = state.decSizeGroupWidth
            myOctSizeGroupWidth.item = state.octSizeGroupWidth
            myBinSizeGroupWidth.item = state.binSizeGroupWidth
            myHexSizeDelimiter.text = state.hexSizeDelimiter
            myDecSizeDelimiter.text = state.decSizeDelimiter
            myOctSizeDelimiter.text = state.octSizeDelimiter
            myBinSizeDelimiter.text = state.binSizeDelimiter
        }

    private val myRenderer = object : ColoredListCellRenderer<Int?>() {
        override fun customizeCellRenderer(
            list: JList<out Int?>, value: Int?, index: Int, selected: Boolean, hasFocus: Boolean
        ) {
            this.append(value?.toString() ?: message("label.DisableDigitGrouping.text"))
        }
    }

    private val myMainPanel = panel {
        group(message("label.LiteralGroupingSetting.text")) {
            row(message("label.Hexadecimal.text")) {
                comboBox(listOf(null, 2, 4), myRenderer).apply { myHexCodeGroupWidth = component }
            }
            row(message("label.Decimal.text")) {
                comboBox(listOf(null, 3, 4), myRenderer).apply { myDecCodeGroupWidth = component }
            }
            row(message("label.Octal.text")) {
                comboBox(listOf(null, 2, 3, 4), myRenderer).apply { myOctCodeGroupWidth = component }
            }
            row(message("label.Binary.text")) {
                comboBox(listOf(null, 4, 8, 16), myRenderer).apply { myBinCodeGroupWidth = component }
            }
        }
        group(message("label.SizeGroupingSetting.text")) {
            row(message("label.Hexadecimal.text")) {
                comboBox(listOf(null, 2, 4), myRenderer).apply { myHexSizeGroupWidth = component }
                textField().apply { myHexSizeDelimiter = component }
            }
            row(message("label.Decimal.text")) {
                comboBox(listOf(null, 3, 4), myRenderer).apply { myDecSizeGroupWidth = component }
                textField().apply { myDecSizeDelimiter = component }
            }
            row(message("label.Octal.text")) {
                comboBox(listOf(null, 2, 3, 4), myRenderer).apply { myOctSizeGroupWidth = component }
                textField().apply { myOctSizeDelimiter = component }
            }
            row(message("label.Binary.text")) {
                comboBox(listOf(null, 4, 8, 16), myRenderer).apply { myBinSizeGroupWidth = component }
                textField().apply { myBinSizeDelimiter = component }
            }
        }
    }

    fun getPanel() = myMainPanel

    fun getPreferredFocusedComponent() = myDecCodeGroupWidth
}
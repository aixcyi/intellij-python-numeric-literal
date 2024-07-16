package cn.aixcyi.plugin.pnl.ui

import cn.aixcyi.plugin.pnl.Zoo.message
import cn.aixcyi.plugin.pnl.storage.PNLSettings
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.layout.ComboBoxPredicate
import java.awt.Font
import javax.swing.JList

/**
 * 插件设置首页组件。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class PNLSettingsComponent {

    companion object {
        private val GW_OPTIONS_HEX = listOf(null, 2, 4)
        private val GW_OPTIONS_DEC = listOf(null, 3, 4)
        private val GW_OPTIONS_OCT = listOf(null, 2, 3, 4)
        private val GW_OPTIONS_BIN = listOf(null, 4, 8, 16)
    }

    private lateinit var myDecSizeGroupWidth: ComboBox<Int?>

    private val myRenderer = object : ColoredListCellRenderer<Int?>() {
        override fun customizeCellRenderer(
            list: JList<out Int?>, value: Int?, index: Int, selected: Boolean, hasFocus: Boolean
        ) {
            this.append(value?.toString() ?: message("label.DisableDigitGrouping.text"))
        }
    }

    private val editorFont: Font = run {
        val preferences = EditorColorsManager.getInstance().schemeForCurrentUITheme.fontPreferences
        val fontFamily = preferences.fontFamily
        Font(fontFamily, Font.PLAIN, preferences.getSize(fontFamily))
    }

    val state = PNLSettings.getInstance().state

    val panel = panel {
        group(message("label.LiteralGroupingSetting.text")) {
            row(message("label.Hexadecimal.text")) {
                comboBox(GW_OPTIONS_HEX, myRenderer)
                    .bindItem(state::hexCodeGroupWidth)
            }
            row(message("label.Decimal.text")) {
                comboBox(GW_OPTIONS_DEC, myRenderer)
                    .bindItem(state::decCodeGroupWidth)
            }
            row(message("label.Octal.text")) {
                comboBox(GW_OPTIONS_OCT, myRenderer)
                    .bindItem(state::octCodeGroupWidth)
            }
            row(message("label.Binary.text")) {
                comboBox(GW_OPTIONS_BIN, myRenderer)
                    .bindItem(state::binCodeGroupWidth)
            }
        }
        group(message("label.SizeGroupingSetting.text")) {
            row(message("label.Hexadecimal.text")) {
                var box: ComboBox<Int?>
                comboBox(GW_OPTIONS_HEX, myRenderer)
                    .bindItem(state::hexSizeGroupWidth)
                    .apply { box = component }
                textField()
                    .bindText(state::hexSizeDelimiter)
                    .applyToComponent { font = editorFont }
                    .enabledIf(ComboBoxPredicate(box) { it != null })
            }
            row(message("label.Decimal.text")) {
                comboBox(GW_OPTIONS_DEC, myRenderer)
                    .bindItem(state::decSizeGroupWidth)
                    .apply { myDecSizeGroupWidth = component }
                textField()
                    .bindText(state::decSizeDelimiter)
                    .applyToComponent { font = editorFont }
                    .enabledIf(ComboBoxPredicate(myDecSizeGroupWidth) { it != null })
            }
            row(message("label.Octal.text")) {
                var box: ComboBox<Int?>
                comboBox(GW_OPTIONS_OCT, myRenderer)
                    .bindItem(state::octSizeGroupWidth)
                    .apply { box = component }
                textField()
                    .bindText(state::octSizeDelimiter)
                    .applyToComponent { font = editorFont }
                    .enabledIf(ComboBoxPredicate(box) { it != null })
            }
            row(message("label.Binary.text")) {
                var box: ComboBox<Int?>
                comboBox(GW_OPTIONS_BIN, myRenderer)
                    .bindItem(state::binSizeGroupWidth)
                    .apply { box = component }
                textField()
                    .bindText(state::binSizeDelimiter)
                    .applyToComponent { font = editorFont }
                    .enabledIf(ComboBoxPredicate(box) { it != null })
            }
        }
    }

    val preferredFocusedComponent = myDecSizeGroupWidth
}
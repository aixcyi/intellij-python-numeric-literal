package cn.aixcyi.plugin.pnl.ui

import cn.aixcyi.plugin.pnl.Zoo.message
import cn.aixcyi.plugin.pnl.storage.PNLSettings
import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

/**
 * 插件设置首页。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
class PNLSettingsConfigurable : SearchableConfigurable {

    companion object {
        const val ID = "HooTool.PNL.SettingsConfigurable"
    }

    private var myComponent: PNLSettingsComponent? = null

    override fun getId() = ID

    override fun getDisplayName() = message("configurable.Settings.display_name")

    override fun getPreferredFocusedComponent(): JComponent? = myComponent?.preferredFocusedComponent

    override fun createComponent(): JComponent {
        myComponent = PNLSettingsComponent()
        return myComponent!!.panel
    }

    override fun isModified(): Boolean {
        return myComponent?.panel?.isModified() ?: false
    }

    override fun apply() {
        if (myComponent == null)
            return
        myComponent!!.panel.apply()
        PNLSettings.getInstance().loadState(myComponent!!.state)
    }

    override fun reset() {
        myComponent?.panel?.reset()
    }

    override fun disposeUIResources() {
        myComponent = null
    }
}
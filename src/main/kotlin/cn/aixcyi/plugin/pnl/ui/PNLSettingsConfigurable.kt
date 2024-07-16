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

    override fun getPreferredFocusedComponent(): JComponent? = myComponent?.getPreferredFocusedComponent()

    override fun createComponent(): JComponent {
        myComponent = PNLSettingsComponent()
        return myComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        return PNLSettings.getInstance().state != myComponent?.state
    }

    override fun apply() {
        if (myComponent == null)
            return
        PNLSettings.getInstance().loadState(myComponent!!.state)
    }

    override fun reset() {
        if (myComponent == null)
            return
        myComponent!!.state = PNLSettings.getInstance().state
    }

    override fun disposeUIResources() {
        myComponent = null
    }
}
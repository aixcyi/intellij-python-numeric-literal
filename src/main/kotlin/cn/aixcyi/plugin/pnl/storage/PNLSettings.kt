package cn.aixcyi.plugin.pnl.storage

import cn.aixcyi.plugin.pnl.Zoo
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * 整数字面值分组设置。
 *
 * @author <a href="https://github.com/aixcyi">砹小翼</a>
 */
@Service(Service.Level.APP)
@State(name = Zoo.XmlComponent.PNL, storages = [Storage(Zoo.PLUGIN_STORAGE)])
class PNLSettings : PersistentStateComponent<PNLSettings.State> {

    companion object {
        fun getInstance() = service<PNLSettings>()
    }

    class State {
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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (hexCodeGroupWidth != other.hexCodeGroupWidth) return false
            if (decCodeGroupWidth != other.decCodeGroupWidth) return false
            if (octCodeGroupWidth != other.octCodeGroupWidth) return false
            if (binCodeGroupWidth != other.binCodeGroupWidth) return false
            if (hexSizeGroupWidth != other.hexSizeGroupWidth) return false
            if (decSizeGroupWidth != other.decSizeGroupWidth) return false
            if (octSizeGroupWidth != other.octSizeGroupWidth) return false
            if (binSizeGroupWidth != other.binSizeGroupWidth) return false
            if (hexSizeDelimiter != other.hexSizeDelimiter) return false
            if (decSizeDelimiter != other.decSizeDelimiter) return false
            if (octSizeDelimiter != other.octSizeDelimiter) return false
            if (binSizeDelimiter != other.binSizeDelimiter) return false

            return true
        }

        override fun hashCode(): Int {
            var result = hexCodeGroupWidth ?: 0
            result = 31 * result + (decCodeGroupWidth ?: 0)
            result = 31 * result + (octCodeGroupWidth ?: 0)
            result = 31 * result + (binCodeGroupWidth ?: 0)
            result = 31 * result + (hexSizeGroupWidth ?: 0)
            result = 31 * result + (decSizeGroupWidth ?: 0)
            result = 31 * result + (octSizeGroupWidth ?: 0)
            result = 31 * result + (binSizeGroupWidth ?: 0)
            result = 31 * result + hexSizeDelimiter.hashCode()
            result = 31 * result + decSizeDelimiter.hashCode()
            result = 31 * result + octSizeDelimiter.hashCode()
            result = 31 * result + binSizeDelimiter.hashCode()
            return result
        }
    }

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, myState)
    }

    fun copyState(): State {
        val newState = State()
        XmlSerializerUtil.copyBean(myState, newState)
        return newState
    }
}
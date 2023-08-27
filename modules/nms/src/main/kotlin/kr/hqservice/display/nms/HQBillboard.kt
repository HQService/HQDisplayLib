package kr.hqservice.display.nms

import kr.hqservice.framework.nms.Version
import kr.hqservice.framework.nms.handler.FunctionType
import kr.hqservice.framework.nms.wrapper.NmsReflectionWrapper
import kr.hqservice.framework.nms.wrapper.NmsWrapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class HQBillboard(
    private val nmsInstance: Any
) : NmsWrapper {
    companion object : KoinComponent {
        private val reflectionWrapper: NmsReflectionWrapper by inject()
        internal val BillboardClass = reflectionWrapper.getNmsClass(
            "Display\$BillboardConstraints",
            Version.V_19.handle("world.entity")
        )
        private val valueOfFunc = reflectionWrapper.getFunction(BillboardClass, FunctionType("valueOf", null, listOf(String::class), true))
        private fun nmsValueOf(nmsName: String): Any {
            return valueOfFunc.call(nmsName)!!
        }
    }

    object FIXED : HQBillboard(nmsValueOf("FIXED"))
    object VERTICAL: HQBillboard(nmsValueOf("VERTICAL"))
    object HORIZONTAL : HQBillboard(nmsValueOf("HORIZONTAL"))
    object CENTER : HQBillboard(nmsValueOf("CENTER"))

    override fun getUnwrappedInstance(): Any {
        return nmsInstance
    }
}
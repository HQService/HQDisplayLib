package kr.hqservice.display.nms

import kr.hqservice.display.nms.wrapper.NmsDisplayWrapper
import kr.hqservice.display.nms.wrapper.NmsTextDisplayWrapper
import kr.hqservice.framework.nms.Version
import kr.hqservice.framework.nms.handler.FunctionType
import kr.hqservice.framework.nms.wrapper.NmsReflectionWrapper
import kr.hqservice.framework.nms.wrapper.NmsWrapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

sealed class HQDisplayType<T : NmsDisplayWrapper>(
    private val nmsInstance: Any,
    private val wrapperClass: KClass<T>
) : NmsWrapper {
    companion object : KoinComponent {
        private val reflectionWrapper: NmsReflectionWrapper by inject()
        internal val EntityTypeClass = reflectionWrapper.getNmsClass("EntityTypes",
            Version.V_19.handle("world.entity")
        )
    }

    object TextDisplay : HQDisplayType<NmsTextDisplayWrapper>(reflectionWrapper.getStaticField(EntityTypeClass, "TEXT_DISPLAY",
        Version.V_19.handleFunction("aX"),
        Version.V_20_4.handleFunction("aY"),
        Version.V_19_FORGE.handleFunction("f_268607_")
    ).call()!!, NmsTextDisplayWrapper::class)
    //object ItemDisplay: HQDisplayType<NmsTextDisplayWrapper>(nmsValueOf("ITEM_DISPLAY"), NmsTextDisplayWrapper::class)
    //object BlockDisplay : HQDisplayType<NmsTextDisplayWrapper>(nmsValueOf("BLOCK_DISPLAY"), NmsTextDisplayWrapper::class)

    fun getWrapperClass(): KClass<T> {
        return wrapperClass
    }

    override fun getUnwrappedInstance(): Any {
        return nmsInstance
    }
}
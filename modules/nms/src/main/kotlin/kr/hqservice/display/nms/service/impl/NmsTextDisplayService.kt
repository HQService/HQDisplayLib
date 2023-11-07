package kr.hqservice.display.nms.service.impl

import kr.hqservice.display.nms.HQDisplayType
import kr.hqservice.display.nms.service.AbstractDisplayService
import kr.hqservice.display.nms.service.NmsDisplayService
import kr.hqservice.display.nms.wrapper.NmsTextDisplayWrapper
import kr.hqservice.framework.global.core.component.Qualifier
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.nms.Version
import kr.hqservice.framework.nms.service.chat.BaseComponentService
import kr.hqservice.framework.nms.wrapper.NmsReflectionWrapper
import kr.hqservice.framework.nms.wrapper.getFunction
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.Color
import kotlin.reflect.jvm.isAccessible

@Service
class NmsTextDisplayService(
    nmsDisplayService: NmsDisplayService,
    reflectionWrapper: NmsReflectionWrapper,
    @Qualifier("base-component") private val baseComponentService: BaseComponentService
) : AbstractDisplayService<NmsTextDisplayWrapper>(nmsDisplayService, HQDisplayType.TextDisplay) {

    private val setTextFunction = reflectionWrapper.getFunction(getTargetClass(), "setText", listOf(baseComponentService.getTargetClass()),
        Version.V_19.handleFunction("c") { setParameterClasses(baseComponentService.getTargetClass()) },
        Version.V_19_FORGE.handleFunction("m_269037_") { setParameterClasses(baseComponentService.getTargetClass()) }
    )
    private val setBackgroundColorFunction = reflectionWrapper.getFunction(getTargetClass(), "setBackgroundColor", listOf(Int::class),
        Version.V_19.handleFunction("c") { setParameterClasses(Int::class) },
        Version.V_19_FORGE.handleFunction("m_269001_") { setParameterClasses(Int::class) }
    )
    private val DATA_LINE_WIDTH_ID = reflectionWrapper.getStaticField(getTargetClass(), "DATA_LINE_WIDTH_ID",
        Version.V_19.handle("aL"),
        Version.V_20.handle("aM"),
        Version.V_20_2.handle("aN"),
        Version.V_19_FORGE.handle("f_268476_")
    ).run {
        isAccessible = true
        call()!!
    }
    private val DATA_TEXT_OPACITY_ID = reflectionWrapper.getStaticField(getTargetClass(), "DATA_TEXT_OPACITY_ID",
        Version.V_19.handle("aN"),
        Version.V_20.handle("aO"),
        Version.V_20_2.handle("aP"),
        Version.V_19_FORGE.handle("f_268481_")
    ).run {
        isAccessible = true
        call()!!
    }


    fun setText(displayWrapper: NmsTextDisplayWrapper, text: BaseComponent) {
        val baseCompWrapper = baseComponentService.wrapFromJson(ComponentSerializer.toString(text))
        if (!setBackgroundColorFunction.isAccessible)
            setBackgroundColorFunction.isAccessible = true
        setTextFunction.call(displayWrapper.getUnwrappedInstance(), baseCompWrapper.getUnwrappedInstance())
    }

    fun setBackgroundColor(displayWrapper: NmsTextDisplayWrapper, color: Color) {
        if (!setBackgroundColorFunction.isAccessible)
            setBackgroundColorFunction.isAccessible = true
        setBackgroundColorFunction.call(displayWrapper.getUnwrappedInstance(), color.asARGB())
    }

    fun setLineWith(displayWrapper: NmsTextDisplayWrapper, lineWidth: Int) {
        nmsDisplayService.setEntityData(displayWrapper, DATA_LINE_WIDTH_ID, lineWidth)
    }

    fun setOpacity(displayWrapper: NmsTextDisplayWrapper, opacity: Byte) {
        nmsDisplayService.setEntityData(displayWrapper, DATA_TEXT_OPACITY_ID, opacity)
    }
}
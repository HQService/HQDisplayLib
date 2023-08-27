package kr.hqservice.display.nms.service.impl

import kr.hqservice.display.nms.HQDisplayType
import kr.hqservice.display.nms.service.AbstractDisplayService
import kr.hqservice.display.nms.service.NmsDisplayService
import kr.hqservice.display.nms.wrapper.NmsTextDisplayWrapper
import kr.hqservice.framework.global.core.component.Qualifier
import kr.hqservice.framework.global.core.component.Service
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
    private val setTextFunction = reflectionWrapper.getFunction(getTargetClass(), "c", listOf(baseComponentService.getTargetClass()))
    private val setTextOpacityFunction = reflectionWrapper.getFunction(getTargetClass(), "g", listOf(Float::class))
    private val setBackgroundColorFunction = reflectionWrapper.getFunction(getTargetClass(), "c", listOf(Int::class))
    private val DATA_LINE_WIDTH_ID = reflectionWrapper.getStaticField(getTargetClass(), "aM").run {
        isAccessible = true
        call()!!
    }

    fun setText(displayWrapper: NmsTextDisplayWrapper, text: BaseComponent) {
        val baseCompWrapper = baseComponentService.wrapFromJson(ComponentSerializer.toString(text))
        setTextFunction.call(displayWrapper.getUnwrappedInstance(), baseCompWrapper.getUnwrappedInstance())
    }

    fun setTextOpacity(displayWrapper: NmsTextDisplayWrapper, opacity: Float) {
        setTextOpacityFunction.call(displayWrapper.getUnwrappedInstance(), opacity)
    }

    fun setBackgroundColor(displayWrapper: NmsTextDisplayWrapper, color: Color) {
        setBackgroundColorFunction.call(displayWrapper.getUnwrappedInstance(), color.asARGB())
    }

    fun setLineWith(displayWrapper: NmsTextDisplayWrapper, lineWidth: Int) {
        nmsDisplayService.setEntityData(displayWrapper, DATA_LINE_WIDTH_ID, lineWidth)
    }
}
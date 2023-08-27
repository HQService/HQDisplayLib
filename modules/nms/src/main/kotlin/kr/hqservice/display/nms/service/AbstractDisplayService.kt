package kr.hqservice.display.nms.service

import kr.hqservice.display.nms.HQDisplayType
import kr.hqservice.display.nms.wrapper.NmsDisplayWrapper
import kr.hqservice.framework.nms.service.NmsEntityService
import org.bukkit.Location
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class AbstractDisplayService<T : NmsDisplayWrapper>(
    protected val nmsDisplayService: NmsDisplayService,
    private val type: HQDisplayType<T>
) : NmsEntityService<T> {
    private val targetClass = nmsDisplayService.getDisplayClass(type)

    final override fun getOriginalClass(): KClass<*> {
        return Location::class
    }

    final override fun getTargetClass(): KClass<*> {
        return targetClass
    }

    final override fun wrap(target: Location): T {
        return type.getWrapperClass()
            .primaryConstructor!!
            .call(nmsDisplayService.createNmsInstance(target, type), this)
    }

    final override fun unwrap(wrapper: T): Location {
        throw UnsupportedOperationException("")
    }
}
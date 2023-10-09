package kr.hqservice.display.nms.service

import kr.hqservice.display.nms.HQBillboard
import kr.hqservice.display.nms.HQDisplayType
import kr.hqservice.display.nms.wrapper.NmsDisplayWrapper
import kr.hqservice.display.nms.virtual.entity.AbstractVirtualDisplay
import kr.hqservice.framework.global.core.component.Qualifier
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.nms.Version
import kr.hqservice.framework.nms.service.NmsEntityService
import kr.hqservice.framework.nms.service.world.WorldService
import kr.hqservice.framework.nms.wrapper.NmsReflectionWrapper
import kr.hqservice.framework.nms.wrapper.getFunction
import org.bukkit.Location
import java.lang.reflect.Constructor
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.reflect.jvm.isAccessible

@Service
class NmsDisplayService(
    private val reflectionWrapper: NmsReflectionWrapper,
    @Qualifier("nms.world") private val worldService: WorldService,
) : NmsEntityService<NmsDisplayWrapper> {
    private val displayClass = reflectionWrapper.getNmsClass(
        "Display",
        Version.V_19.handle("world.entity")
    )

    private val transformationClass = Class.forName("com.mojang.math.Transformation").kotlin
    private val dataWatcherClass = reflectionWrapper.getNmsClass("DataWatcher",
        Version.V_19.handle("network.syncher"))
    private val dataAccessorClass = reflectionWrapper.getNmsClass("DataWatcherObject",
        Version.V_19.handle("network.syncher"))
/*
    private val dataWatcherSetFunction = dataWatcherClass.java.getMethod(
        "b", dataAccessorClass.java, JvmType.Object::class.javaObjectType,
    )*/

    private val dataWatcherSetFunction = reflectionWrapper.getFunction(dataWatcherClass, "b",
        listOf(dataAccessorClass, Any::class),
        Version.V_20_FORGE.handleFunction("m_135381_") { setParameterClasses(dataAccessorClass, Any::class) }
    )

    private val getEntityDataFunction = reflectionWrapper.getFunction(displayClass, "getEntityData",
        Version.V_19.handle("aj"),
        Version.V_20_FORGE.handle("m_20088_")
    )
    private val DATA_INTERPOLATION_DURATION_ID = reflectionWrapper.getStaticField(displayClass, "r",
        Version.V_20.handle("q"),
        Version.V_20_FORGE.handle("f_268449_")
    ).run {
        isAccessible = true
        call()!!
    }
    private val DATA_INTERPOLATION_START_DELTA_TICKS_ID = reflectionWrapper.getStaticField(displayClass, "q",
        Version.V_20.handle("p"),
        Version.V_20_FORGE.handle("f_276329_")
    ).run {
        isAccessible = true
        call()!!
    }

    private val setPosRawFunction = reflectionWrapper.getFunction(displayClass, "p", listOf(Double::class, Double::class, Double::class),
        Version.V_20_FORGE.handleFunction("m_20343_") { setParameterClasses(Double::class, Double::class, Double::class) }
    )
    // head -> n , body -> o
    private val setYRotFunction = reflectionWrapper.getFunction(displayClass, "f", listOf(Float::class),
        Version.V_20_FORGE.handleFunction("m_5616_") { setParameterClasses(Float::class) }
    )
    private val setTransformationFunction = reflectionWrapper.getFunction(displayClass, "a", listOf(transformationClass),
        Version.V_20_FORGE.handleFunction("m_269214_") { setParameterClasses(transformationClass) }
    )
    private val setBillboardFunction = reflectionWrapper.getFunction(displayClass, "a", listOf(HQBillboard.BillboardClass),
        Version.V_20_FORGE.handleFunction("m_269423_") { setParameterClasses(HQBillboard.BillboardClass) }
    )
    private val setViewRangeFunction = reflectionWrapper.getFunction(displayClass, "g", listOf(Float::class),
        Version.V_20.handleFunction("s") { setParameterClasses(Float::class) },
        Version.V_20_FORGE.handleFunction("m_269215_") { setParameterClasses(Float::class) }
    )
    private val setShadowRadiusFunction = reflectionWrapper.getFunction(displayClass, "h", listOf(Float::class),
        Version.V_20.handleFunction("t") { setParameterClasses(Float::class) },
        Version.V_20_FORGE.handleFunction("m_269526_") { setParameterClasses(Float::class) }
    )

    internal fun initializeLocation(virtualDisplay: AbstractVirtualDisplay, location: Location) {
        setPosRawFunction.call(virtualDisplay.getEntity(), location.x, location.y, location.z)
        setYRotFunction.call(virtualDisplay.getEntity(), location.yaw)
    }

    internal fun setEntityData(nmsWrapper: NmsDisplayWrapper, watcherObject: Any, obj: Any) {
        val data = getEntityDataFunction.call(nmsWrapper.getUnwrappedInstance())
        dataWatcherSetFunction.call(data, watcherObject, obj)
    }

    internal fun applyTransformation(virtualDisplay: AbstractVirtualDisplay) {
        setTransformationFunction.call(virtualDisplay.getEntity(), virtualDisplay.transformation.getUnwrappedInstance())
    }

    internal fun applyInterpolationDuration(virtualDisplay: AbstractVirtualDisplay) {
        val data = getEntityDataFunction.call(virtualDisplay.getEntity())
        dataWatcherSetFunction.call(data, DATA_INTERPOLATION_DURATION_ID, virtualDisplay.interpolationDuration)
    }

    internal fun applyInterpolationDelay(virtualDisplay: AbstractVirtualDisplay) {
        val data = getEntityDataFunction.call(virtualDisplay.getEntity())
        dataWatcherSetFunction.call(data, DATA_INTERPOLATION_START_DELTA_TICKS_ID, virtualDisplay.interpolationDuration)
    }

    internal fun applyBillboard(virtualDisplay: AbstractVirtualDisplay) {
        setBillboardFunction.call(virtualDisplay.getEntity(), virtualDisplay.billboard.getUnwrappedInstance())
    }

    internal fun applyViewRange(virtualDisplay: AbstractVirtualDisplay) {
        setViewRangeFunction.call(virtualDisplay.getEntity(), virtualDisplay.viewDistance)
    }

    internal fun applyShadowRadius(virtualDisplay: AbstractVirtualDisplay) {
        setShadowRadiusFunction.call(virtualDisplay.getEntity(), virtualDisplay.shadowRadius)
    }

    override fun getOriginalClass(): KClass<*> {
        return Location::class
    }

    override fun getTargetClass(): KClass<*> {
        return displayClass
    }

    fun <T : NmsDisplayWrapper> getDisplayClass(type: HQDisplayType<T>): KClass<*> {
        return reflectionWrapper.getNmsClass(
            "Display\$${type::class.simpleName}",
            Version.V_19.handle("world.entity")
        )
    }

    private fun <T : NmsDisplayWrapper> getConstructor(type: HQDisplayType<T>): Constructor<*> {
        return getDisplayClass(type).java.getConstructor(type.getUnwrappedInstance()::class.java, worldService.getTargetClass().java)
    }

    fun <T : NmsDisplayWrapper> createNmsInstance(location: Location, type: HQDisplayType<T>): Any {
        val cons = getConstructor(type)
        val level = worldService.wrap(location.world!!).getUnwrappedInstance()
        return cons.newInstance(type.getUnwrappedInstance(), level)
    }

    override fun wrap(target: Location): NmsDisplayWrapper {
        throw UnsupportedOperationException("")
    }

    override fun unwrap(wrapper: NmsDisplayWrapper): Location {
        throw UnsupportedOperationException("")
    }
}
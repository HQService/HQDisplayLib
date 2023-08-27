package kr.hqservice.display.nms.virtual.entity

import kr.hqservice.display.nms.HQBillboard
import kr.hqservice.display.nms.HQTransformation
import kr.hqservice.display.nms.service.NmsDisplayService
import kr.hqservice.framework.nms.virtual.AbstractVirtualEntity
import org.bukkit.Location
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class AbstractVirtualDisplay(
    private val location: Location,
    text: String
) : AbstractVirtualEntity(location, text) {
    companion object : KoinComponent {
        private val displayService: NmsDisplayService by inject()
    }

    var transformation: HQTransformation = HQTransformation()
        set(value) {
            field = value
            displayService.applyTransformation(this)
            switchMetaMask()
        }

    var interpolationDuration: Int = 0
        set(value) {
            field = value
            displayService.applyInterpolationDuration(this)
            switchMetaMask()
        }

    var interpolationDelay: Int = 0
        set(value) {
            field = value
            displayService.applyInterpolationDelay(this)
            switchMetaMask()
        }

    var billboard: HQBillboard = HQBillboard.CENTER
        set(value) {
            field = value
            displayService.applyBillboard(this)
            switchMetaMask()
        }

    var viewDistance: Float = Float.MAX_VALUE
        set(value) {
            field = value
            displayService.applyViewRange(this)
            switchMetaMask()
        }

    var shadowRadius: Float = 1f
        set(value) {
            field = value
            displayService.applyShadowRadius(this)
            switchMetaMask()
        }

    override fun initialize() {
        displayService.applyTransformation(this)
        displayService.applyViewRange(this)
        displayService.initializeLocation(this, location)
    }
}
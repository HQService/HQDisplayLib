package kr.hqservice.display.nms.wrapper

import kr.hqservice.display.nms.service.impl.NmsTextDisplayService
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Color

class NmsTextDisplayWrapper(
    nmsInstance: Any,
    private val service: NmsTextDisplayService
) : NmsDisplayWrapper(nmsInstance) {
    fun setText(text: BaseComponent) {
        service.setText(this, text)
    }

    fun setOpacity(opacity: Float) {
        service.setTextOpacity(this, opacity)
    }

    fun setBackgroundColor(color: Color) {
        service.setBackgroundColor(this, color)
    }
}
package kr.hqservice.display.nms.wrapper

import kr.hqservice.framework.nms.wrapper.EntityWrapper

sealed class NmsDisplayWrapper(
    private val nmsInstance: Any,
) : EntityWrapper {
    final override fun getUnwrappedInstance(): Any {
        return nmsInstance
    }
}
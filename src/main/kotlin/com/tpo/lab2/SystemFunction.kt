package com.tpo.lab2

import com.tpo.lab2.functions.LogPart
import com.tpo.lab2.functions.TrigPart

class SystemFunction(
    private val trigPart: TrigPart,
    private val logPart: LogPart,
) {
    fun compute(x: Double): Double =
        when {
            x <= 0.0 -> trigPart.compute(x)
            else -> logPart.compute(x)
        }
}

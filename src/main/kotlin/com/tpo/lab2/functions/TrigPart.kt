package com.tpo.lab2.functions

import com.tpo.lab2.functions.base.Cos
import com.tpo.lab2.functions.trig.Cot
import com.tpo.lab2.functions.trig.Csc
import com.tpo.lab2.functions.trig.Sec
import com.tpo.lab2.functions.trig.Sin
import com.tpo.lab2.functions.trig.Tan

class TrigPart(
    private val cos: Cos,
    private val sin: Sin,
    private val csc: Csc,
    private val sec: Sec,
    private val tan: Tan,
    private val cot: Cot,
) {
    fun compute(x: Double): Double {
        require(x <= 0) { "TrigPart is only defined for x <= 0, got: $x" }

        val cosVal = cos.compute(x)
        val cscVal = csc.compute(x)
        val secVal = sec.compute(x)
        val tanVal = tan.compute(x)
        val cotVal = cot.compute(x)

        val part1 = (cosVal / cscVal) * secVal * (tanVal * tanVal * tanVal) / cscVal
        val part2 = (cscVal * cscVal * cscVal) * (tanVal - cotVal) / tanVal

        return part1 + part2
    }
}

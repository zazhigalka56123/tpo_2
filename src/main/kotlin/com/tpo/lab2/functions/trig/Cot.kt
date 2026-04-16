package com.tpo.lab2.functions.trig

import com.tpo.lab2.functions.base.Cos
import kotlin.math.abs

class Cot(
    private val sin: Sin,
    private val cos: Cos,
) {
    fun compute(x: Double): Double {
        val sinVal = sin.compute(x)
        if (abs(sinVal) < 1e-12) throw ArithmeticException("cot(x) is undefined: sin($x) ≈ 0")
        return cos.compute(x) / sinVal
    }
}

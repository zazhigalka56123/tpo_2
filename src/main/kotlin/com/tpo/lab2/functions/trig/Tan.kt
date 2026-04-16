package com.tpo.lab2.functions.trig

import com.tpo.lab2.functions.base.Cos
import kotlin.math.abs

class Tan(
    private val sin: Sin,
    private val cos: Cos,
) {
    fun compute(x: Double): Double {
        val cosVal = cos.compute(x)
        if (abs(cosVal) < 1e-12) throw ArithmeticException("tan(x) is undefined: cos($x) ≈ 0")
        return sin.compute(x) / cosVal
    }
}

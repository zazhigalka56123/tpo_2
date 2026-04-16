package com.tpo.lab2.functions.trig

import kotlin.math.abs

class Csc(
    private val sin: Sin,
) {
    fun compute(x: Double): Double {
        val sinVal = sin.compute(x)
        if (abs(sinVal) < 1e-12) throw ArithmeticException("csc(x) is undefined: sin($x) ≈ 0")
        return 1.0 / sinVal
    }
}

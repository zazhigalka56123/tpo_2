package com.tpo.lab2.functions.trig

import com.tpo.lab2.functions.base.Cos
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.sqrt

class Sin(
    private val cos: Cos,
) {
    fun compute(x: Double): Double {
        val cosVal = cos.compute(x)
        val sinAbs = sqrt(max(0.0, 1.0 - cosVal * cosVal))

        val twoPi = 2.0 * PI
        var xNorm = x % twoPi
        if (xNorm < 0) xNorm += twoPi

        return if (xNorm < PI) sinAbs else -sinAbs
    }
}

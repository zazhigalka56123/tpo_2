package com.tpo.lab2.functions.base

import kotlin.math.PI
import kotlin.math.abs

class Cos(
    private val epsilon: Double = 1e-9,
    private val maxTerms: Int = 1000,
) {
    fun compute(x: Double): Double {
        require(!x.isNaN() && !x.isInfinite()) { "x must be finite, got: $x" }
        require(epsilon > 0) { "epsilon must be positive, got: $epsilon" }

        val twoPi = 2.0 * PI
        var xNorm = x % twoPi
        if (xNorm > PI) xNorm -= twoPi
        if (xNorm < -PI) xNorm += twoPi

        val xSq = xNorm * xNorm
        var term = 1.0
        var result = 0.0

        for (n in 0 until maxTerms) {
            result += term
            if (abs(term) < epsilon) break
            term *= -xSq / ((2 * n + 2).toDouble() * (2 * n + 1).toDouble())
        }

        return result
    }
}

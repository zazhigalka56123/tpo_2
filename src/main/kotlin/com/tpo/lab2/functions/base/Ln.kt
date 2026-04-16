package com.tpo.lab2.functions.base

import kotlin.math.abs

class Ln(
    private val epsilon: Double = 1e-9,
    private val maxTerms: Int = 10000,
) {
    fun compute(x: Double): Double {
        require(!x.isNaN() && !x.isInfinite()) { "x must be finite, got: $x" }
        if (x <= 0) throw ArithmeticException("ln(x) is undefined for x <= 0, got: $x")
        require(epsilon > 0) { "epsilon must be positive, got: $epsilon" }

        val t = (x - 1.0) / (x + 1.0)
        val tSq = t * t
        var power = t
        var result = 0.0

        for (k in 0 until maxTerms) {
            val term = power / (2 * k + 1).toDouble()
            result += term
            if (abs(term) < epsilon / 2.0) break
            power *= tSq
        }

        return 2.0 * result
    }
}

package com.tpo.lab2.functions

import com.tpo.lab2.functions.base.Ln
import com.tpo.lab2.functions.log.Log10
import com.tpo.lab2.functions.log.Log2
import com.tpo.lab2.functions.log.Log3
import com.tpo.lab2.functions.log.Log5

class LogPart(
    private val ln: Ln,
    private val log2: Log2,
    private val log3: Log3,
    private val log5: Log5,
    private val log10: Log10,
) {
    fun compute(x: Double): Double {
        require(x > 0) { "LogPart is only defined for x > 0, got: $x" }

        val sum1 = ln.compute(x) + log5.compute(x) + log3.compute(x)
        val sum2 = log10.compute(x) + log2.compute(x)
        val inner = sum1 * sum2
        val cubed = inner * inner * inner
        return cubed * cubed
    }
}

package com.tpo.lab2.functions.log

import com.tpo.lab2.functions.base.Ln

class Log10(
    private val ln: Ln,
) {
    private val lnBase = ln.compute(10.0)

    fun compute(x: Double): Double = ln.compute(x) / lnBase
}

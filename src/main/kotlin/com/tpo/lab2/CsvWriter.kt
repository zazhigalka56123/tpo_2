package com.tpo.lab2

import java.io.File

class CsvWriter(
    private val system: SystemFunction,
) {
    fun write(
        from: Double,
        to: Double,
        step: Double,
        outputPath: String,
    ) {
        require(step > 0) { "step must be positive, got: $step" }
        require(from <= to) { "from must be <= to" }

        File(outputPath).printWriter().use { out ->
            out.println("x,result")
            var x = from
            while (x <= to + step * 0.5) {
                val result =
                    try {
                        system.compute(x).toString()
                    } catch (e: ArithmeticException) {
                        "undefined"
                    }
                out.println("$x,$result")
                x += step
            }
        }
    }
}

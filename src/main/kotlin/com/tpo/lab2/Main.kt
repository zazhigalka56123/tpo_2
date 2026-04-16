package com.tpo.lab2

import com.tpo.lab2.functions.LogPart
import com.tpo.lab2.functions.TrigPart
import com.tpo.lab2.functions.base.Cos
import com.tpo.lab2.functions.base.Ln
import com.tpo.lab2.functions.log.Log10
import com.tpo.lab2.functions.log.Log2
import com.tpo.lab2.functions.log.Log3
import com.tpo.lab2.functions.log.Log5
import com.tpo.lab2.functions.trig.Cot
import com.tpo.lab2.functions.trig.Csc
import com.tpo.lab2.functions.trig.Sec
import com.tpo.lab2.functions.trig.Sin
import com.tpo.lab2.functions.trig.Tan

fun main() {
    val cos = Cos()
    val ln = Ln()
    val sin = Sin(cos)
    val csc = Csc(sin)
    val sec = Sec(cos)
    val tan = Tan(sin, cos)
    val cot = Cot(sin, cos)
    val log2 = Log2(ln)
    val log3 = Log3(ln)
    val log5 = Log5(ln)
    val log10 = Log10(ln)

    val trigPart = TrigPart(cos, sin, csc, sec, tan, cot)
    val logPart = LogPart(ln, log2, log3, log5, log10)
    val system = SystemFunction(trigPart, logPart)
    val writer = CsvWriter(system)

    writer.write(from = -5.0, to = 5.0, step = 0.1, outputPath = "output.csv")
    println("Done. Results written to output.csv")
}

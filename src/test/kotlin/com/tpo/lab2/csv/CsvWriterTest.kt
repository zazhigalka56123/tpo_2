package com.tpo.lab2.csv

import com.tpo.lab2.CsvWriter
import com.tpo.lab2.SystemFunction
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class CsvWriterTest {
    @TempDir
    lateinit var tempDir: Path

    private fun buildRealSystem(): SystemFunction {
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
        return SystemFunction(
            TrigPart(cos, sin, csc, sec, tan, cot),
            LogPart(ln, log2, log3, log5, log10),
        )
    }

    @Test
    fun `CSV file has header as first line`() {
        val file = tempDir.resolve("out.csv").toFile().absolutePath
        val writer = CsvWriter(buildRealSystem())
        writer.write(from = 1.0, to = 2.0, step = 1.0, outputPath = file)
        val lines = File(file).readLines()
        assertEquals("x,result", lines[0])
    }

    @Test
    fun `CSV row count matches expected step count`() {
        val file = tempDir.resolve("out.csv").toFile().absolutePath
        val writer = CsvWriter(buildRealSystem())
        writer.write(from = -2.0, to = 2.0, step = 0.5, outputPath = file)
        val lines = File(file).readLines()
        assertEquals(10, lines.size)
    }

    @Test
    fun `CSV writes undefined for singularity at x=0`() {
        val file = tempDir.resolve("out.csv").toFile().absolutePath
        val writer = CsvWriter(buildRealSystem())
        writer.write(from = 0.0, to = 0.0, step = 1.0, outputPath = file)
        val lines = File(file).readLines()
        assertEquals(2, lines.size)
        assertEquals("0.0,undefined", lines[1])
    }

    @Test
    fun `CSV writes numeric result for x=1 (log part, result=0)`() {
        val file = tempDir.resolve("out.csv").toFile().absolutePath
        val writer = CsvWriter(buildRealSystem())
        writer.write(from = 1.0, to = 1.0, step = 1.0, outputPath = file)
        val lines = File(file).readLines()
        assertEquals(2, lines.size)
        val (x, result) = lines[1].split(",")
        assertEquals("1.0", x)
        assertEquals(0.0, result.toDouble(), 1e-6)
    }

    @Test
    fun `CSV with step 0_1 produces correct row count`() {
        val file = tempDir.resolve("out.csv").toFile().absolutePath
        val writer = CsvWriter(buildRealSystem())
        writer.write(from = 1.0, to = 2.0, step = 0.1, outputPath = file)
        val lines = File(file).readLines()
        assertEquals(12, lines.size)
    }

    @Test
    fun `throws on non-positive step`() {
        val file = tempDir.resolve("out.csv").toFile().absolutePath
        val writer = CsvWriter(buildRealSystem())
        assertThrows(IllegalArgumentException::class.java) {
            writer.write(from = 0.0, to = 1.0, step = 0.0, outputPath = file)
        }
        assertThrows(IllegalArgumentException::class.java) {
            writer.write(from = 0.0, to = 1.0, step = -0.1, outputPath = file)
        }
    }
}

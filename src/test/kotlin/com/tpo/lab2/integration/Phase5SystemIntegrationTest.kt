package com.tpo.lab2.integration

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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@ExtendWith(MockitoExtension::class)
class Phase5SystemIntegrationTest {
    @Mock lateinit var mockTrigPart: TrigPart

    @Mock lateinit var mockLogPart: LogPart

    private val eps = 1e-6

    @Test
    fun `5A - x=0 routes to TrigPart`() {
        whenever(mockTrigPart.compute(0.0)).thenReturn(42.0)
        val system = SystemFunction(mockTrigPart, mockLogPart)
        assertEquals(42.0, system.compute(0.0), eps)
        verify(mockTrigPart).compute(0.0)
    }

    @Test
    fun `5A - x negative routes to TrigPart`() {
        whenever(mockTrigPart.compute(-1.0)).thenReturn(-5.0)
        val system = SystemFunction(mockTrigPart, mockLogPart)
        assertEquals(-5.0, system.compute(-1.0), eps)
        verify(mockTrigPart).compute(-1.0)
    }

    @Test
    fun `5A - x positive routes to LogPart`() {
        whenever(mockLogPart.compute(1.0)).thenReturn(99.0)
        val system = SystemFunction(mockTrigPart, mockLogPart)
        assertEquals(99.0, system.compute(1.0), eps)
        verify(mockLogPart).compute(1.0)
    }

    @Test
    fun `5A - x large positive routes to LogPart not TrigPart`() {
        whenever(mockLogPart.compute(100.0)).thenReturn(1234.0)
        val system = SystemFunction(mockTrigPart, mockLogPart)
        system.compute(100.0)
        verify(mockLogPart).compute(100.0)
    }

    @Test
    fun `5B - real TrigPart produces finite results for safe negative x`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        val sec = Sec(cos)
        val tan = Tan(sin, cos)
        val cot = Cot(sin, cos)
        val realTrig = TrigPart(cos, sin, csc, sec, tan, cot)
        val system = SystemFunction(realTrig, mockLogPart)

        listOf(-0.5, -1.0, -2.0, -3.0).forEach { x ->
            val result = system.compute(x)
            assertTrue(result.isFinite(), "Expected finite at x=$x, got $result")
        }
    }

    @Test
    fun `5B - real LogPart produces finite positive results for x greater than 1`() {
        val ln = Ln()
        val realLog = LogPart(ln, Log2(ln), Log3(ln), Log5(ln), Log10(ln))
        val system = SystemFunction(mockTrigPart, realLog)

        listOf(2.0, 5.0, 10.0, 100.0).forEach { x ->
            val result = system.compute(x)
            assertTrue(result.isFinite(), "Expected finite at x=$x, got $result")
            assertTrue(result > 0, "Expected positive at x=$x, got $result")
        }
    }

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
    fun `5C - system gives finite results for a range of x`() {
        val system = buildRealSystem()
        val safePoints = listOf(-3.5, -2.5, -2.0, -1.5, -1.0, -0.5, 0.5, 1.0, 2.0, 5.0, 10.0)
        safePoints.forEach { x ->
            val result = system.compute(x)
            assertTrue(result.isFinite(), "Expected finite at x=$x, got $result")
        }
    }

    @Test
    fun `5C - system at x=1 returns 0 because all logs are zero`() {
        val system = buildRealSystem()
        assertEquals(0.0, system.compute(1.0), eps)
    }

    @Test
    fun `5C - system trig side matches manual computation at x=-1`() {
        val system = buildRealSystem()
        val x = -1.0
        val c = cos(x)
        val s = sin(x)
        val cscV = 1.0 / s
        val secV = 1.0 / c
        val tanV = s / c
        val cotV = c / s
        val expected =
            (c / cscV) * secV * (tanV * tanV * tanV) / cscV +
                (cscV * cscV * cscV) * (tanV - cotV) / tanV
        assertEquals(expected, system.compute(x), eps)
    }

    @Test
    fun `5C - system throws at x=0 because csc is undefined`() {
        val system = buildRealSystem()
        assertThrows(ArithmeticException::class.java) { system.compute(0.0) }
    }

    @Test
    fun `5C - system throws at x=-pi because sin is zero`() {
        val system = buildRealSystem()
        assertThrows(ArithmeticException::class.java) { system.compute(-PI) }
    }
}

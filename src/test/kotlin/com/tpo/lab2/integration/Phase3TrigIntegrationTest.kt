package com.tpo.lab2.integration

import com.tpo.lab2.functions.TrigPart
import com.tpo.lab2.functions.base.Cos
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
import org.mockito.kotlin.whenever
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

@ExtendWith(MockitoExtension::class)
class Phase3TrigIntegrationTest {
    @Mock lateinit var mockCos: Cos

    @Mock lateinit var mockSin: Sin

    @Mock lateinit var mockCsc: Csc

    @Mock lateinit var mockSec: Sec

    @Mock lateinit var mockTan: Tan

    @Mock lateinit var mockCot: Cot

    private val eps = 1e-6

    @Test
    fun `3a stub - csc(pi over 2) = 1 when sin returns 1`() {
        val x = PI / 2
        whenever(mockSin.compute(x)).thenReturn(1.0)
        val csc = Csc(mockSin)
        assertEquals(1.0, csc.compute(x), eps)
    }

    @Test
    fun `3a stub - csc throws when sin returns 0`() {
        whenever(mockSin.compute(0.0)).thenReturn(0.0)
        val csc = Csc(mockSin)
        assertThrows(ArithmeticException::class.java) { csc.compute(0.0) }
    }

    @Test
    fun `3a real - csc matches 1 over sin`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        listOf(-2.0, -1.0, 0.5, 1.0, PI / 2, PI / 6).forEach { x ->
            assertEquals(1.0 / sin(x), csc.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `3a real - csc throws at x=0`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        assertThrows(ArithmeticException::class.java) { csc.compute(0.0) }
    }

    @Test
    fun `3b stub - sec(0) = 1 when cos returns 1`() {
        whenever(mockCos.compute(0.0)).thenReturn(1.0)
        val sec = Sec(mockCos)
        assertEquals(1.0, sec.compute(0.0), eps)
    }

    @Test
    fun `3b stub - sec throws when cos returns 0`() {
        val x = PI / 2
        whenever(mockCos.compute(x)).thenReturn(0.0)
        val sec = Sec(mockCos)
        assertThrows(ArithmeticException::class.java) { sec.compute(x) }
    }

    @Test
    fun `3b real - sec matches 1 over cos`() {
        val cos = Cos()
        val sec = Sec(cos)
        listOf(-2.0, -1.0, 0.0, 0.5, 1.0, PI / 3, PI / 4).forEach { x ->
            assertEquals(1.0 / cos(x), sec.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `3c stub - tan = sin over cos with controlled stubs`() {
        val x = 1.0
        whenever(mockSin.compute(x)).thenReturn(0.8414709848)
        whenever(mockCos.compute(x)).thenReturn(0.5403023059)
        val tan = Tan(mockSin, mockCos)
        assertEquals(0.8414709848 / 0.5403023059, tan.compute(x), eps)
    }

    @Test
    fun `3c stub - tan throws when cos returns 0`() {
        val x = PI / 2
        whenever(mockCos.compute(x)).thenReturn(0.0)
        val tan = Tan(mockSin, mockCos)
        assertThrows(ArithmeticException::class.java) { tan.compute(x) }
    }

    @Test
    fun `3c real - tan matches stdlib`() {
        val cos = Cos()
        val sin = Sin(cos)
        val tan = Tan(sin, cos)
        listOf(-2.0, -1.0, -0.5, 0.5, 1.0, PI / 4, PI / 6).forEach { x ->
            assertEquals(tan(x), tan.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `3d stub - cot = cos over sin with controlled stubs`() {
        val x = 1.0
        whenever(mockSin.compute(x)).thenReturn(0.8414709848)
        whenever(mockCos.compute(x)).thenReturn(0.5403023059)
        val cot = Cot(mockSin, mockCos)
        assertEquals(0.5403023059 / 0.8414709848, cot.compute(x), eps)
    }

    @Test
    fun `3d stub - cot throws when sin returns 0`() {
        whenever(mockSin.compute(0.0)).thenReturn(0.0)
        val cot = Cot(mockSin, mockCos)
        assertThrows(ArithmeticException::class.java) { cot.compute(0.0) }
    }

    @Test
    fun `3d real - cot matches cos over sin`() {
        val cos = Cos()
        val sin = Sin(cos)
        val cot = Cot(sin, cos)
        listOf(-2.0, -1.0, 0.5, 1.0, PI / 2, PI / 3).forEach { x ->
            assertEquals(cos(x) / sin(x), cot.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `3e stub - TrigPart uses injected stubs for computation`() {
        val x = -1.0
        val cosV = 0.5403023059
        val cscV = 1.1883951057
        val secV = 1.8508157177
        val tanV = -1.5574077246
        val cotV = -0.6420926160

        whenever(mockCos.compute(x)).thenReturn(cosV)
        whenever(mockCsc.compute(x)).thenReturn(cscV)
        whenever(mockSec.compute(x)).thenReturn(secV)
        whenever(mockTan.compute(x)).thenReturn(tanV)
        whenever(mockCot.compute(x)).thenReturn(cotV)

        val trigPart = TrigPart(mockCos, mockSin, mockCsc, mockSec, mockTan, mockCot)
        val result = trigPart.compute(x)

        val part1 = (cosV / cscV) * secV * (tanV * tanV * tanV) / cscV
        val part2 = (cscV * cscV * cscV) * (tanV - cotV) / tanV
        assertEquals(part1 + part2, result, eps)
    }

    @Test
    fun `3e real - TrigPart result is finite for normal negative x`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        val sec = Sec(cos)
        val tan = Tan(sin, cos)
        val cot = Cot(sin, cos)
        val trigPart = TrigPart(cos, sin, csc, sec, tan, cot)

        listOf(-0.5, -1.0, -1.5, -2.0, -3.0).forEach { x ->
            val result = trigPart.compute(x)
            assertTrue(result.isFinite(), "Expected finite result at x=$x, got $result")
        }
    }

    @Test
    fun `3e real - TrigPart matches expected value at x=-1`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        val sec = Sec(cos)
        val tan = Tan(sin, cos)
        val cot = Cot(sin, cos)
        val trigPart = TrigPart(cos, sin, csc, sec, tan, cot)

        val x = -1.0
        val c = cos(x)
        val s = sin(x)
        val expected =
            run {
                val cscV = 1.0 / s
                val secV = 1.0 / c
                val tanV = s / c
                val cotV = c / s
                val p1 = (c / cscV) * secV * (tanV * tanV * tanV) / cscV
                val p2 = (cscV * cscV * cscV) * (tanV - cotV) / tanV
                p1 + p2
            }
        assertEquals(expected, trigPart.compute(x), eps)
    }

    @Test
    fun `3e - TrigPart throws for x=0 (csc undefined)`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        val sec = Sec(cos)
        val tan = Tan(sin, cos)
        val cot = Cot(sin, cos)
        val trigPart = TrigPart(cos, sin, csc, sec, tan, cot)
        assertThrows(ArithmeticException::class.java) { trigPart.compute(0.0) }
    }

    @Test
    fun `3e - TrigPart throws for positive x`() {
        val cos = Cos()
        val sin = Sin(cos)
        val csc = Csc(sin)
        val sec = Sec(cos)
        val tan = Tan(sin, cos)
        val cot = Cot(sin, cos)
        val trigPart = TrigPart(cos, sin, csc, sec, tan, cot)
        assertThrows(IllegalArgumentException::class.java) { trigPart.compute(1.0) }
    }
}

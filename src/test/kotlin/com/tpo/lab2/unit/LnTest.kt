package com.tpo.lab2.unit

import com.tpo.lab2.functions.base.Ln
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.E
import kotlin.math.ln

class LnTest {
    private val eps = 1e-6
    private val ln = Ln()

    @Test
    fun `ln(1) equals 0`() {
        assertEquals(0.0, ln.compute(1.0), eps)
    }

    @Test
    fun `ln(e) equals 1`() {
        assertEquals(1.0, ln.compute(E), eps)
    }

    @Test
    fun `ln(e squared) equals 2`() {
        assertEquals(2.0, ln.compute(E * E), eps)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.1, 0.5, 0.9, 1.0, 1.5, 2.0, E, 5.0, 10.0, 100.0])
    fun `matches kotlin math ln within tolerance`(x: Double) {
        assertEquals(ln(x), this.ln.compute(x), eps)
    }

    @Test
    fun `ln is monotonically increasing`() {
        val values = listOf(0.1, 0.5, 1.0, 2.0, 5.0, 10.0)
        for (i in 0 until values.size - 1) {
            assertTrue(
                ln.compute(values[i]) < ln.compute(values[i + 1]),
                "Expected ln(${values[i]}) < ln(${values[i + 1]})",
            )
        }
    }

    @Test
    fun `ln(a times b) equals ln(a) plus ln(b)`() {
        val a = 2.0
        val b = 3.0
        assertEquals(ln.compute(a) + ln.compute(b), ln.compute(a * b), eps)
    }

    @Test
    fun `ln(a over b) equals ln(a) minus ln(b)`() {
        val a = 6.0
        val b = 2.0
        assertEquals(ln.compute(a) - ln.compute(b), ln.compute(a / b), eps)
    }

    @Test
    fun `throws ArithmeticException for x equals 0`() {
        assertThrows(ArithmeticException::class.java) { ln.compute(0.0) }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-0.1, -1.0, -5.0, -100.0])
    fun `throws ArithmeticException for negative x`(x: Double) {
        assertThrows(ArithmeticException::class.java) { ln.compute(x) }
    }

    @Test
    fun `throws on NaN input`() {
        assertThrows(IllegalArgumentException::class.java) { ln.compute(Double.NaN) }
    }

    @Test
    fun `throws on positive infinity`() {
        assertThrows(IllegalArgumentException::class.java) { ln.compute(Double.POSITIVE_INFINITY) }
    }

    @Test
    fun `throws on zero epsilon`() {
        assertThrows(IllegalArgumentException::class.java) { Ln(epsilon = 0.0).compute(1.0) }
    }

    @Test
    fun `throws on negative epsilon`() {
        assertThrows(IllegalArgumentException::class.java) { Ln(epsilon = -1e-5).compute(1.0) }
    }

    @Test
    fun `higher precision gives more accurate result`() {
        val x = 2.5
        val lowPrec = Ln(epsilon = 1e-3).compute(x)
        val highPrec = Ln(epsilon = 1e-9).compute(x)
        val expected = ln(x)
        assertEquals(expected, highPrec, 1e-6)
        assertTrue(Math.abs(highPrec - expected) <= Math.abs(lowPrec - expected) + 1e-4)
    }
}

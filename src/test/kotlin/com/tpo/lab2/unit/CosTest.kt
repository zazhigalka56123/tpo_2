package com.tpo.lab2.unit

import com.tpo.lab2.functions.base.Cos
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.PI
import kotlin.math.cos

class CosTest {
    private val eps = 1e-7
    private val cos = Cos()

    @Test
    fun `cos(0) equals 1`() {
        assertEquals(1.0, cos.compute(0.0), eps)
    }

    @Test
    fun `cos(pi) equals -1`() {
        assertEquals(-1.0, cos.compute(PI), eps)
    }

    @Test
    fun `cos(2pi) equals 1`() {
        assertEquals(1.0, cos.compute(2 * PI), eps)
    }

    @Test
    fun `cos(pi over 2) is approximately 0`() {
        assertEquals(0.0, cos.compute(PI / 2), eps)
    }

    @Test
    fun `cos(pi over 3) equals 0_5`() {
        assertEquals(0.5, cos.compute(PI / 3), eps)
    }

    @Test
    fun `cos(pi over 4) equals sqrt2 over 2`() {
        assertEquals(Math.sqrt(2.0) / 2.0, cos.compute(PI / 4), eps)
    }

    @Test
    fun `cos(pi over 6) equals sqrt3 over 2`() {
        assertEquals(Math.sqrt(3.0) / 2.0, cos.compute(PI / 6), eps)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.5, 1.0, 1.5, 2.0, PI / 3, PI / 4])
    fun `cos is an even function cos(x) == cos(-x)`(x: Double) {
        assertEquals(cos.compute(x), cos.compute(-x), eps)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, 0.5, 1.0, PI / 4, PI / 2, PI])
    fun `cos is periodic with period 2pi`(x: Double) {
        assertEquals(cos.compute(x), cos.compute(x + 2 * PI), eps)
        assertEquals(cos.compute(x), cos.compute(x - 2 * PI), eps)
    }

    @ParameterizedTest
    @ValueSource(doubles = [-5.0, -2.5, -1.0, -0.5, 0.0, 0.5, 1.0, 2.0, 3.0, 5.0, 10.0, 100.0])
    fun `matches kotlin math cos within tolerance`(x: Double) {
        assertEquals(cos(x), this.cos.compute(x), eps)
    }

    @Test
    fun `higher precision gives more accurate result`() {
        val x = 1.23
        val lowPrec = Cos(epsilon = 1e-3).compute(x)
        val highPrec = Cos(epsilon = 1e-9).compute(x)
        val expected = cos(x)
        assertTrue(Math.abs(highPrec - expected) < Math.abs(lowPrec - expected) + 1e-4)
        assertEquals(expected, highPrec, 1e-7)
    }

    @Test
    fun `throws on NaN input`() {
        assertThrows(IllegalArgumentException::class.java) { cos.compute(Double.NaN) }
    }

    @Test
    fun `throws on positive infinity`() {
        assertThrows(IllegalArgumentException::class.java) { cos.compute(Double.POSITIVE_INFINITY) }
    }

    @Test
    fun `throws on negative infinity`() {
        assertThrows(IllegalArgumentException::class.java) { cos.compute(Double.NEGATIVE_INFINITY) }
    }

    @Test
    fun `throws on zero epsilon`() {
        assertThrows(IllegalArgumentException::class.java) { Cos(epsilon = 0.0).compute(1.0) }
    }

    @Test
    fun `throws on negative epsilon`() {
        assertThrows(IllegalArgumentException::class.java) { Cos(epsilon = -1e-5).compute(1.0) }
    }
}

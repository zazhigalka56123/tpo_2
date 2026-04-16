package com.tpo.lab2.integration

import com.tpo.lab2.functions.base.Cos
import com.tpo.lab2.functions.trig.Sin
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt

@ExtendWith(MockitoExtension::class)
class Phase2SinIntegrationTest {
    @Mock
    lateinit var mockCos: Cos

    private val eps = 1e-7

    @Test
    fun `A - sin(0) is 0 when cos returns 1 (stub)`() {
        whenever(mockCos.compute(0.0)).thenReturn(1.0)
        val sin = Sin(mockCos)
        assertEquals(0.0, sin.compute(0.0), eps)
    }

    @Test
    fun `A - sin(pi over 2) is +1 when cos returns 0 in first quadrant (stub)`() {
        val x = PI / 2
        whenever(mockCos.compute(x)).thenReturn(0.0)
        val sin = Sin(mockCos)
        assertEquals(1.0, sin.compute(x), eps)
    }

    @Test
    fun `A - sin(3pi over 2) is -1 when cos returns 0 in third quadrant (stub)`() {
        val x = 3 * PI / 2
        whenever(mockCos.compute(x)).thenReturn(0.0)
        val sin = Sin(mockCos)
        assertEquals(-1.0, sin.compute(x), eps)
    }

    @Test
    fun `A - sin(pi over 6) is 0_5 when cos returns sqrt3 over 2 in first quadrant (stub)`() {
        val x = PI / 6
        whenever(mockCos.compute(x)).thenReturn(sqrt(3.0) / 2.0)
        val sin = Sin(mockCos)
        assertEquals(0.5, sin.compute(x), eps)
    }

    @Test
    fun `A - sin is negative in (pi, 2pi) quadrant (stub)`() {
        val x = PI + 0.5
        whenever(mockCos.compute(x)).thenReturn(0.8775825618903728)
        val sin = Sin(mockCos)
        val result = sin.compute(x)
        assertTrue(result < 0, "Expected negative sin in (π, 2π), got $result")
    }

    @Test
    fun `B - sin matches stdlib for positive x`() {
        val cos = Cos()
        val sin = Sin(cos)
        listOf(0.0, 0.5, 1.0, PI / 6, PI / 4, PI / 3, PI / 2, PI).forEach { x ->
            assertEquals(sin(x), sin.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `B - sin matches stdlib for negative x`() {
        val cos = Cos()
        val sin = Sin(cos)
        listOf(-0.5, -1.0, -PI / 4, -PI / 2, -PI).forEach { x ->
            assertEquals(sin(x), sin.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `B - sin satisfies Pythagorean identity with real cos`() {
        val cos = Cos()
        val sin = Sin(cos)
        listOf(-2.0, -1.0, 0.5, 1.0, PI / 3, 2.5).forEach { x ->
            val s = sin.compute(x)
            val c = cos.compute(x)
            assertEquals(1.0, s * s + c * c, 1e-6, "Pythagorean identity failed at x=$x")
        }
    }
}

package com.tpo.lab2.integration

import com.tpo.lab2.functions.LogPart
import com.tpo.lab2.functions.base.Ln
import com.tpo.lab2.functions.log.Log10
import com.tpo.lab2.functions.log.Log2
import com.tpo.lab2.functions.log.Log3
import com.tpo.lab2.functions.log.Log5
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.log2
import kotlin.math.pow

@ExtendWith(MockitoExtension::class)
class Phase4LogIntegrationTest {
    @Mock lateinit var mockLn: Ln

    @Mock lateinit var mockLog2: Log2

    @Mock lateinit var mockLog3: Log3

    @Mock lateinit var mockLog5: Log5

    @Mock lateinit var mockLog10: Log10

    private val eps = 1e-6

    @Test
    fun `4a stub - log2 = ln(x) over ln(2)`() {
        val lnX = 1.0
        val lnBase = ln(2.0)
        whenever(mockLn.compute(4.0)).thenReturn(lnX * 2)
        whenever(mockLn.compute(2.0)).thenReturn(lnBase)
        val log2 = Log2(mockLn)
        assertEquals((lnX * 2) / lnBase, log2.compute(4.0), eps)
    }

    @Test
    fun `4a real - log2 matches stdlib`() {
        val ln = Ln()
        val log2 = Log2(ln)
        listOf(0.5, 1.0, 2.0, 4.0, 8.0, 16.0, 100.0).forEach { x ->
            assertEquals(log2(x), log2.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `4b real - log3 matches ln(x) over ln(3)`() {
        val ln = Ln()
        val log3 = Log3(ln)
        listOf(1.0, 3.0, 9.0, 27.0, 0.5, 10.0).forEach { x ->
            assertEquals(ln(x) / ln(3.0), log3.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `4b real - log3(27) equals 3`() {
        val ln = Ln()
        val log3 = Log3(ln)
        assertEquals(3.0, log3.compute(27.0), eps)
    }

    @Test
    fun `4c real - log5 matches ln(x) over ln(5)`() {
        val ln = Ln()
        val log5 = Log5(ln)
        listOf(1.0, 5.0, 25.0, 125.0, 0.5, 10.0).forEach { x ->
            assertEquals(ln(x) / ln(5.0), log5.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `4c real - log5(25) equals 2`() {
        val ln = Ln()
        val log5 = Log5(ln)
        assertEquals(2.0, log5.compute(25.0), eps)
    }

    @Test
    fun `4d real - log10 matches stdlib`() {
        val ln = Ln()
        val log10 = Log10(ln)
        listOf(1.0, 10.0, 100.0, 1000.0, 0.1, 0.01).forEach { x ->
            assertEquals(kotlin.math.log10(x), log10.compute(x), eps, "Failed at x=$x")
        }
    }

    @Test
    fun `4d real - log10(1000) equals 3`() {
        val ln = Ln()
        val log10 = Log10(ln)
        assertEquals(3.0, log10.compute(1000.0), eps)
    }

    @Test
    fun `4e stub - LogPart delegates to injected log functions`() {
        val x = 2.0
        whenever(mockLn.compute(x)).thenReturn(1.0)
        whenever(mockLog5.compute(x)).thenReturn(0.5)
        whenever(mockLog3.compute(x)).thenReturn(0.6)
        whenever(mockLog10.compute(x)).thenReturn(0.3)
        whenever(mockLog2.compute(x)).thenReturn(1.0)

        val logPart = LogPart(mockLn, mockLog2, mockLog3, mockLog5, mockLog10)
        val inner = (1.0 + 0.5 + 0.6) * (0.3 + 1.0)
        val expected = (inner * inner * inner).let { it * it }
        assertEquals(expected, logPart.compute(x), eps)
    }

    @Test
    fun `4e real - LogPart at x=1 equals 0 (all logs are 0)`() {
        val ln = Ln()
        val logPart = LogPart(ln, Log2(ln), Log3(ln), Log5(ln), Log10(ln))
        assertEquals(0.0, logPart.compute(1.0), eps)
    }

    @Test
    fun `4e real - LogPart is positive for x greater than 1`() {
        val ln = Ln()
        val logPart = LogPart(ln, Log2(ln), Log3(ln), Log5(ln), Log10(ln))
        listOf(2.0, 5.0, 10.0, 100.0).forEach { x ->
            assertTrue(logPart.compute(x) > 0, "Expected positive result at x=$x")
        }
    }

    @Test
    fun `4e real - LogPart result matches manual calculation at x=10`() {
        val ln = Ln()
        val logPart = LogPart(ln, Log2(ln), Log3(ln), Log5(ln), Log10(ln))
        val x = 10.0
        val lnX = ln(x)
        val sum1 = lnX + ln(x) / ln(5.0) + ln(x) / ln(3.0)
        val sum2 = ln(x) / ln(10.0) + log2(x)
        val inner = sum1 * sum2
        val expected = inner.pow(6)
        assertEquals(expected, logPart.compute(x), eps * expected.absoluteValue + eps)
    }

    @Test
    fun `4e - LogPart throws IllegalArgumentException for x=0`() {
        val ln = Ln()
        val logPart = LogPart(ln, Log2(ln), Log3(ln), Log5(ln), Log10(ln))
        assertThrows(IllegalArgumentException::class.java) { logPart.compute(0.0) }
    }

    @Test
    fun `4e - LogPart throws for negative x`() {
        val ln = Ln()
        val logPart = LogPart(ln, Log2(ln), Log3(ln), Log5(ln), Log10(ln))
        assertThrows(IllegalArgumentException::class.java) { logPart.compute(-1.0) }
    }
}

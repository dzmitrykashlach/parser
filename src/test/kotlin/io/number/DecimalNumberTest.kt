package io.number

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DecimalNumberTest {
    @Test
    fun `test whole part conversion`() {
        assertEquals(123.0, DecimalNumber("123").toDouble())
        assertEquals(12300.0, DecimalNumber("12300").toDouble())
        assertEquals(0.0, DecimalNumber("0").toDouble())
    }

    @Test
    fun `test fraction part conversion`() {
        assertEquals(0.2, DecimalNumber("0", "2").toDouble())
        assertEquals(0.234, DecimalNumber("0", "234").toDouble())
        // This is really not true :)
        assertEquals(0.1, DecimalNumber("0", "1").toDouble())
    }

    @Test
    fun `test number conversion`() {
        assertEquals(10.2, DecimalNumber("10", "2").toDouble())
        assertEquals(10.002, DecimalNumber("10", "002").toDouble())
    }
}

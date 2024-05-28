package io.number

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DecimalNumberParserTest {
    @Test
    fun `test parse numbers`() {
        assertEquals(1.0, DecimalNumberParser("1").tryParse())
        assertEquals(1.2, DecimalNumberParser("1.2").tryParse())
        assertEquals(1.2, DecimalNumberParser("1,2").tryParse())
        assertEquals(0.223, DecimalNumberParser("0.223").tryParse())
        assertEquals(0.223, DecimalNumberParser("0,223").tryParse())
        assertEquals(1111.123, DecimalNumberParser("1111.123").tryParse())
        assertEquals(11123.0, DecimalNumberParser("11.123,0").tryParse())
        assertEquals(11123.0, DecimalNumberParser("11,123.0").tryParse())
        assertEquals(1123.2, DecimalNumberParser("1123.2").tryParse())
        assertEquals(1123.2, DecimalNumberParser("1123,2").tryParse())
        assertEquals(1123.2, DecimalNumberParser("1 123,2").tryParse())
        assertEquals(1123.2, DecimalNumberParser("1 123.2").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("-1 123.2").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("- 1 123.2").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("-  1 123.2").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("-$  1 123.2").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("1 123.2 $-").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("1 123.2 -").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("(1 123.2)").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("($1 123.2)").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("(1 123.2$)").tryParse())
        assertEquals(-1123.2, DecimalNumberParser("(1 123.2 )").tryParse())
        assertEquals(1123.2, DecimalNumberParser("0001123.2").tryParse())
        assertEquals(0.0, DecimalNumberParser("0000.0").tryParse())
        assertEquals(0.0, DecimalNumberParser("000.0").tryParse())
        assertEquals(0.0, DecimalNumberParser("000.000").tryParse())
        assertEquals(0.0, DecimalNumberParser("000.000.000").tryParse())
        assertEquals(0.0, DecimalNumberParser("000.000,000").tryParse())
        assertEquals(0.0, DecimalNumberParser("000,000,000").tryParse())
        assertEquals(0.0, DecimalNumberParser("000.000,000").tryParse())
        assertEquals(0.0, DecimalNumberParser("000,000.000").tryParse())
        assertEquals(0.0, DecimalNumberParser("0,000.000").tryParse())

    }

    @Test
    fun `test parse errors`() {

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1.").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser(".1").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser(".").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1  2").tryParse()
        }



        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("12 ").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("12 34").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("12 34,3").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1234,3.3").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("234,3.3").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("234.3.3").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser(" 234").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1234 234").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1.234_356").tryParse()
        }


        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1.abc").tryParse()
        }

        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("123.000").tryParse()
        }

    }

    @Test
    fun `test groups separated with space`() {
        assertEquals(1123.0, DecimalNumberParser("1 123").tryParse())
        assertEquals(11123.0, DecimalNumberParser("11 123").tryParse())
        assertEquals(113123.0, DecimalNumberParser("113 123").tryParse())
        assertEquals(6113123.0, DecimalNumberParser("6 113 123").tryParse())
    }

    @Test
    fun `test groups separated with dots`() {
        assertEquals(6113123.0, DecimalNumberParser("6.113.123").tryParse())

        // Ambiguous
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1.123").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("11.123").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("113.123").tryParse()
        }
    }

    @Test
    fun `test groups separated with commas`() {
        assertEquals(6113123.0, DecimalNumberParser("6,113,123.0").tryParse())

        // Ambiguous
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("1,123").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("11,123").tryParse()
        }
        assertThrows(NumberParseException::class.java) {
            DecimalNumberParser("113,123").tryParse()
        }
    }

    @Test
    fun `test space cannot be decimal separator`() {
        assertEquals(1123.0, DecimalNumberParser("1 123").tryParse())
    }
}

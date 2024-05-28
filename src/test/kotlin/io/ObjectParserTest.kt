package io

import io.number.DecimalNumberParser
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObjectParserTest {

    @Test
    fun decimalParser_positiveTest(){
        val s = "($1 123.2)"
        val dp = DecimalNumberParser(s)
        val op = ObjectParser(listOf(dp))
        val o = op.tryConvert()
        assertTrue { o is Double }
        assertEquals(-1123.2, o as Double)
    }

    @Test
    fun decimalParser_negativeTest(){
        val op = ObjectParser(listOf())
        val o = op.tryConvert()
        assertEquals(null,  o)
    }
}
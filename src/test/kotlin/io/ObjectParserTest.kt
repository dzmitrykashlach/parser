package io

import io.number.DecimalNumberParser
import io.text.TextParser
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObjectParserTest {

    @Test
    fun decimalParser_positiveTest(){
        val s = "($1 123.2)"
        val dp = DecimalNumberParser(s)
        val tp = TextParser(s)
        val op = ObjectParser(listOf(dp,tp))
        val o = op.tryConvert()
        assertTrue { o is Double }
        assertEquals(-1123.2, o as Double)
    }

    @Test
    fun sentimentParser_negativeTest(){
        val s = "I feel so empty inside. " +
                "I can't seem to shake this feeling of hopelessness and sadness. " +
                "I just want to be happy again. " +
                "I used to be so carefree, but something happened that changed me forever. " +
                "I feel like I'm trapped in a dark place, and I don't know how to get out."
        val dp = DecimalNumberParser(s)
        val tp = TextParser(s)
        val op = ObjectParser(listOf(dp,tp))
        val o = op.tryConvert()
        assertTrue { o is String }
        assertEquals("Negative", o as String)
    }
}
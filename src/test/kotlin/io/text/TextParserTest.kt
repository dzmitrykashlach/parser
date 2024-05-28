package io.text

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TextParserTest {

    @Test
    fun canRecognizeDepressiveText() {
        val sentiment = TextParser(
            "I feel so empty inside. " +
                    "I can't seem to shake this feeling of hopelessness and sadness. " +
                    "I just want to be happy again. " +
                    "I used to be so carefree, but something happened that changed me forever. " +
                    "I feel like I'm trapped in a dark place, and I don't know how to get out."
        ).tryParse()
        assertEquals("Negative", sentiment)
    }
}
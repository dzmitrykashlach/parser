package io.number

import kotlin.math.pow

class DecimalNumber(
    private var wholePart: String = "",
    private var decimalPart: String = "",
    private var positive: Boolean = true
    ) {

    fun setPositive(positive: Boolean) {
        this.positive = positive
    }
    fun addWholePart(part: String) {
        wholePart += part
    }

    fun addDecimalPart(part: String) {
        decimalPart += part
    }

    fun toDouble(): Double {
        check(wholePart.isNotEmpty()) { "Whole part is empty" }
        var d = 0.0
        d += toNumber(wholePart, wholePart.length - 1, -1)
        d += toNumber(decimalPart, -1, -1)
        return this.positive.let { if (it) d else -d }
    }

    private fun toNumber(text: String, startPower: Int, powerStep: Int): Double {
        var d = 0.0
        var power = startPower
        for (c in text.toCharArray()) {
            check(c.isDigit()) { "Not a digit: '$c'" }
            d += (c - '0') * 10.0.pow(power)
            power += powerStep
        }
        return d
    }
}

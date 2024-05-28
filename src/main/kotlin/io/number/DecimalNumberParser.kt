package io.number

import io.Parser

/*
 * Grammar:
 *
 * number = whole_part (decimal_separator decimal_part)?
 * decimal_part = INTEGER
 * decimal_separator = DOT | COMMA // Sticky
 * whole_part = grouped_number | INTEGER
 * grouped_number = group (group_separator full_group)*
 * group_separator = DOT | COMMA | SPACE  // Sticky
 * full_group = INTEGER // 3 digits only
 * group = INTEGER // 3 or fewer digits
 */

class DecimalNumberParser(private val text: String) : Parser<Double> {

    companion object {
        private val GROUP_SEPARATORS = listOf(TokenType.SPACE, TokenType.DOT, TokenType.COMMA)
        private val DECIMAL_SEPARATORS = listOf(TokenType.DOT, TokenType.COMMA)
        private val ALL_SEPARATORS = (GROUP_SEPARATORS + DECIMAL_SEPARATORS).distinct()
    }

    private val lexer = Lexer(text)
    private val tokens = lexer.asSequence().toList()
    private var forwardIndex: Int = 0
    private var backwardIndex: Int = tokens.size - 1
    private val number = DecimalNumber()
    private val eofToken = Token("", TokenType.EOF, text.length)


    override fun tryParse(): Double {
        number()
        return number.toDouble()
    }

    private fun peekFront(offset: Int = 0): Token {
        val next = forwardIndex + offset
        return if (next >= tokens.size) eofToken else tokens[next]
    }

    private fun peekBack(offset: Int = 0): Token {
        val previous = backwardIndex - offset
        return if (previous <= 0) eofToken else tokens[previous]
    }

    private fun checkNext(vararg types: TokenType): Token {
        val token = peekFront()
        if (!types.contains(token.type)) {
            parseError(*types)
        }
        return token
    }

    private fun stepForward(): Token {
        forwardIndex++
        return peekFront()
    }

    private fun stepBackward(): Token {
        backwardIndex--
        return peekBack()
    }

    private fun isSequence(vararg types: TokenType): Boolean {
        var next = forwardIndex
        for (type in types) {
            val token = if (next >= tokens.size) eofToken else tokens[next]
            if (token.type != type) {
                return false
            }
            next++
        }
        return true
    }

    private fun eof(): Boolean {
        return peekFront().type == TokenType.EOF
    }

    private fun number() {
        if (tokens.size > 1) {
            checkNegative()
        }
        wholePart()
        if (eof()) {
            return
        }
        checkNext(*DECIMAL_SEPARATORS.toTypedArray().plus(TokenType.INTEGER), TokenType.EOF)
        stepForward()
        val token = checkNext(TokenType.INTEGER)
        number.addDecimalPart(token.payload)
        stepForward()
        checkNext(
            TokenType.EOF,
            TokenType.RIGHT_BRACKET,
            TokenType.MINUS,
            TokenType.SPACE,
            TokenType.USD
        )
    }

    private fun wholePart() {
        val token = checkNext(TokenType.INTEGER)
        number.addWholePart(token.payload)
        stepForward()
        if (token.payload.length <= 3) {
            // Optional group number
            groupedNumber()
        }
    }


    private fun checkNegative() {
        var frontToken = peekFront()
        var backToken = peekBack()
        var probablyNegative = false
        while (frontToken.type != TokenType.INTEGER) {
            if (frontToken.type == TokenType.MINUS) {
                number.setPositive(false)
                traverseForwardUntil(TokenType.INTEGER)
                break
            }
            if (frontToken.type == TokenType.LEFT_BRACKET) {
                probablyNegative = true
                traverseForwardUntil(TokenType.INTEGER)
                break
            }
            if (forwardIndex == backwardIndex - 1) {
                break
            }
            stepForward()
        }
        while (backToken.type != TokenType.INTEGER) {
            if (backToken.type == TokenType.MINUS) {
                number.setPositive(false)
                traverseBackUntil(TokenType.INTEGER)
                break
            }
            if (backToken.type == TokenType.RIGHT_BRACKET && probablyNegative) {
                number.setPositive(false)
                traverseBackUntil(TokenType.INTEGER)
                break
            }
            if (forwardIndex == backwardIndex - 1) {
                break
            } else {
                stepBackward()
            }
        }
    }

    private fun traverseBackUntil(type: TokenType): Token {
        var token = stepBackward()
        while (token.type != type) {
            token = stepBackward()
        }
        return token
    }

    private fun traverseForwardUntil(type: TokenType): Token {
        var token = stepForward()
        while (token.type != type) {
            token = stepForward()
        }
        return token
    }


    private fun groupedNumber() {
        val separator = peekFront().type
        if (!GROUP_SEPARATORS.contains(separator)) {
            return
        }
        // SEP 3DIGITS EOF - ambiguous
        // SEP 3DIGITS ANY_SEP - group
        // SEP ! 3DIGITS - not a group
        // otherwise not a group
        if (DECIMAL_SEPARATORS.contains(separator) &&
            isSequence(separator, TokenType.INTEGER, TokenType.EOF) && peekFront(1).payload.length == 3
        ) {
            // If first part is 0 - resolve as decimal point
            if (number.toDouble() == 0.0) {
                return
            }
            customError("Unable to distinguish between decimal separator and group separator")
        }
        // Optional group sequence
        if (!DECIMAL_SEPARATORS.contains(separator) ||
            (isSequence(separator, TokenType.INTEGER) &&
                    peekFront(1).payload.length == 3 &&
                    ALL_SEPARATORS.contains(peekFront(2).type))
        ) {
            while (isSequence(separator, TokenType.INTEGER) && peekFront(1).payload.length == 3) {
                number.addWholePart(stepForward().payload)
                stepForward()
            }
        }
    }

    private fun parseError(vararg expectedTokens: TokenType) {
        val token = peekFront()
        val validTokens = expectedTokens.joinToString()
        System.err.println("Unexpected token: ${token.type} at ${token.index}. Valid tokens at this point: $validTokens")
        System.err.println(text)
        System.err.println(getErrorMarker())
        throw NumberParseException("Unexpected token: ${token.type} at ${token.index}: '$text'")
    }

    private fun customError(message: String) {
        System.err.println(message)
        System.err.println(text)
        System.err.println(getErrorMarker())
        throw NumberParseException("$message: '$text'")
    }

    private fun getErrorMarker(): String {
        val token = peekFront()
        var line = ""
        if (token.index > 0) {
            line = "-".repeat(token.index)
        }
        line += "^"
        return line
    }
}

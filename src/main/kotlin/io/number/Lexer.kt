package io.number

class Lexer(
    source: String
) : Iterator<Token> {
    private val chars = source.toCharArray()
    private var index: Int = 0

    fun nextToken(): Token {
        if (index == chars.size) {
            return Token("", TokenType.EOF, index)
        }
        val start = index
        val c = chars[index]
        if (c.isDigit()) {
            val tokenPayload = StringBuilder()
            while (index < chars.size && chars[index].isDigit()) {
                tokenPayload.append(chars[index++])
            }
            return Token(tokenPayload.toString(), TokenType.INTEGER, start)
        }
        index++
        return when (c) {
            ' ' -> Token(" ", TokenType.SPACE, start)
            '.' -> Token(".", TokenType.DOT, start)
            ',' -> Token(",", TokenType.COMMA, start)
            '-' -> Token("-", TokenType.MINUS, start)
            '(' -> Token("(", TokenType.LEFT_BRACKET, start)
            ')' -> Token(")", TokenType.RIGHT_BRACKET, start)
            '$' -> Token(")", TokenType.USD, start)
            else -> Token(c.toString(), TokenType.CHAR, start)
        }
    }

    override fun hasNext(): Boolean {
        return index < chars.size
    }

    override fun next(): Token {
        return nextToken()
    }
}

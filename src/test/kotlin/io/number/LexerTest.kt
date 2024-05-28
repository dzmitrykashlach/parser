package io.number

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LexerTest {
    @Test
    fun `test number`() {
        val lexer = Lexer("1234")
        assertEquals(Token("1234", TokenType.INTEGER, 0), lexer.nextToken())
        assertEquals(Token("", TokenType.EOF, 4), lexer.nextToken())
        assertEquals(Token("", TokenType.EOF, 4), lexer.nextToken())
    }

    @Test
    fun `test letters`() {
        val lexer = Lexer("1 2.3,4")
        assertEquals(Token("1", TokenType.INTEGER, 0), lexer.nextToken())
        assertEquals(Token(" ", TokenType.SPACE, 1), lexer.nextToken())
        assertEquals(Token("2", TokenType.INTEGER, 2), lexer.nextToken())
        assertEquals(Token(".", TokenType.DOT, 3), lexer.nextToken())
        assertEquals(Token("3", TokenType.INTEGER, 4), lexer.nextToken())
        assertEquals(Token(",", TokenType.COMMA, 5), lexer.nextToken())
        assertEquals(Token("4", TokenType.INTEGER, 6), lexer.nextToken())
        assertEquals(Token("", TokenType.EOF, 7), lexer.nextToken())
    }
}

package io.number

data class Token(
    val payload: String,
    val type: TokenType,
    val index: Int
)

enum class TokenType {
    INTEGER,
    COMMA,
    DOT,
    SPACE,
    CHAR,
    MINUS,
    LEFT_BRACKET,
    RIGHT_BRACKET,
    USD,
    EOF
}

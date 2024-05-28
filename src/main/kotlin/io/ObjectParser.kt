package io

class ObjectParser(val parsers: List<Parser<*>>) {
    /*constructor() : this(
        listOf(
//        BankCardConvertor(), BigDecimalConvertor()
        )
    )*/

    fun tryConvert(): Any? = parsers.firstNotNullOfOrNull {
        it.tryParse() }
}
package io

class ObjectParser(val parsers: List<Parser<*>>) {

    fun tryConvert(): Any? = parsers.firstNotNullOfOrNull {
        runCatching { it.tryParse() }.getOrNull()
         }
}
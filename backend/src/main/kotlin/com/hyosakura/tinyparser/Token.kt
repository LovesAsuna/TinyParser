package com.hyosakura.tinyparser

import com.hyosakura.analyzer.grammar.Term

/**
 * @author LovesAsuna
 **/
data class Token(val term: Term, val value: String = "") {
    override fun toString(): String {
        return when (term.symbol) {
            "identifier" -> "identifier($value)"
            "number" -> "const: $value"
            else -> term.symbol
        }
    }
}
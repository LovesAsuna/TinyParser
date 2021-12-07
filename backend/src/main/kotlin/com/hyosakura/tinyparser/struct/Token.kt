package com.hyosakura.tinyparser.struct

/**
 * @author LovesAsuna
 **/
data class Token(val term: Term, val value: String = "")

data class Term(val symbol: String) {
    override fun toString(): String = symbol
}
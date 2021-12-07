package com.hyosakura.tinyparser.enumeration

import com.hyosakura.tinyparser.struct.Term

/**
 * @author LovesAsuna
 **/
enum class Symbols(val term: Term) {
    SEMICOLON(Term(";")),
    ASSIGN(Term("=")),
    LESS(Term("<")),
    EQUAL(Term("==")),
    PLUS(Term("+")),
    MINUS(Term("-")),
    TIMES(Term("*")),
    DIVIDE(Term("/")),
    LPAREN(Term("(")),
    RPAREN(Term(")")),
    GREATER(Term(">")),
    POWER(Term("^")),
    GREATER_EQUAL(Term(">=")),
    LESS_EQUAL(Term("<=")),
    NOT_EQUAL(Term("<>")),
    MOD(Term("%")),
    AND(Term("and")),
    OR(Term("or")),
    NOT(Term("not"))
}
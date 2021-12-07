package com.hyosakura.tinyparser.enumeration

import com.hyosakura.tinyparser.struct.Term

/**
 * @author LovesAsuna
 **/
enum class KeyWords(val term: Term) {
    IF(Term("if")),
    THEN(Term("then")),
    END(Term("end")),
    ELSE(Term("else")),
    REPEAT(Term("repeat")),
    UNTIL(Term("until")),
    READ(Term("read")),
    WRITE(Term("write")),
    WHILE(Term("while")),
    DO(Term("do")),
    FOR(Term("for")),
    TO(Term("to")),
    DOWNTO(Term("downto")),
    ENDDO(Term("enddo"))
}
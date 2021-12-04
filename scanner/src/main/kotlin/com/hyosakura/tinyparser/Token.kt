package com.hyosakura.tinyparser

import com.hyosakura.analyzer.grammar.Term

/**
 * @author LovesAsuna
 **/
data class Token(val term: Term, val value: String = "")
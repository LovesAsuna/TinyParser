package com.hyosakura.tinyparser

import com.hyosakura.analyzer.grammar.*

object Rules {
    private val ruleList = listOf(
        "program" to listOf("stmt-sequence"),
        "stmt-sequence" to listOf("statement", ";statement"),
        ";statement" to listOf(";", "statement", ";statement"),
        ";statement" to listOf(""),
        "statement" to listOf("if-stmt"),
        "statement" to listOf("repeat-stmt"),
        "statement" to listOf("assign-stmt"),
        "statement" to listOf("read-stmt"),
        "statement" to listOf("write-stmt"),
        "statement" to listOf("while-stmt"),
        "statement" to listOf("dowhile-stmt"),
        "statement" to listOf("for-stmt"),
        "if-stmt" to listOf("if", "(", "exp", ")", "stmt-sequence", "else-stmt-sequence"),
        "else-stmt-sequence" to listOf("else", "stmt-sequence"),
        "else-stmt-sequence" to listOf(""),
        "repeat-stmt" to listOf("repeat", "stmt-sequence", "until", "exp"),
        "assign-stmt" to listOf("identifier", ":=", "exp"),
        "read-stmt" to listOf("read", "identifier"),
        "write-stmt" to listOf("write", "exp"),
        "while-stmt" to listOf("while", "exp", "do", "stmt-sequence", "endwhile"),
        "dowhile-stmt" to listOf("do", "stmt-sequence", "while", "(", "exp", ")"),
        "for-stmt" to listOf("for", "assign-stmt", "rest-for-stmt"),
        "rest-for-stmt" to listOf("asfor-stmt"),
        "rest-for-stmt" to listOf("defor-stmt"),
        "asfor-stmt" to listOf("to", "simple-exp", "do", "stmt-sequence", "enddo"),
        "defor-stmt" to listOf("downto", "simple-exp", "do", "stmt-sequence", "enddo"),
        "exp" to listOf("simple-exp", "comp-simp-exp"),
        "comp-simp-exp" to listOf("comparision-op", "simple-exp"),
        "comp-simp-exp" to listOf(""),
        "comparision-op" to listOf("<"),
        "comparision-op" to listOf("="),
        "comparision-op" to listOf(">"),
        "simple-exp" to listOf("term", "add-op-term"),
        "add-op-term" to listOf("addop", "term", "add-op-term"),
        "add-op-term" to listOf(""),
        "addop" to listOf("+"),
        "addop" to listOf("-"),
        "term" to listOf("factor", "mul-factor"),
        "mul-factor" to listOf("mulop", "factor", "mul-factor"),
        "mul-factor" to listOf(""),
        "mulop" to listOf("*"),
        "mulop" to listOf("/"),
        "mulop" to listOf("%"),
        "factor" to listOf("(", "exp", ")"),
        "factor" to listOf("number"),
        "factor" to listOf("identifier"),
    )

    fun buildGrammar(): Grammar {
        val head = NonTerm(ruleList.first().first)
        val ruleMap = mutableMapOf<NonTerm, MutableList<MutableList<Symbol>>>()
        val nonTermList = ruleList.map {
            it.first
        }
        ruleList.forEach { rule ->
            val right = mutableListOf<Symbol>()
            rule.second.forEach {
                right.add(
                    if (nonTermList.contains(it)) {
                        NonTerm(it)
                    } else {
                        Term(it)
                    }
                )
            }
            if (right.isEmpty()) right.add(Empty)
            ruleMap.computeIfAbsent(NonTerm(rule.first)) { mutableListOf() }.add(right)
        }
        return Grammar(head, ruleMap)
    }
}
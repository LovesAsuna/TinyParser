package com.hyosakura.tinyparser

import com.hyosakura.analyzer.grammar.Term
import com.hyosakura.tinyparser.enumeration.KeyWords
import com.hyosakura.tinyparser.enumeration.Symbols

/**
 * @author LovesAsuna
 **/
class Parser {
    private lateinit var tokens: List<Token>
    private var index = 0
    private lateinit var token: Token

    fun parseToTree(code: String): SyntaxTree {
        tokens = Scanner.split(code)
        return SyntaxTree(parse())
    }

    private fun getToken() {
        token = tokens[index++]
    }

    private fun match(term: Term): Boolean {
        return if (token.term == term) {
            getToken()
            true
        } else {
            throw IllegalArgumentException("Expected: ${term.symbol} but got: ${token.value}")
        }
    }

    fun parse(): SyntaxTreeNode {
        getToken()
        val root = getNode()
        statementSequence(root)
        return root
    }

    private fun statementSequence(parent: SyntaxTreeNode? = null): SyntaxTreeNode? {
        val root = statement(parent)
        while (token.term == Symbols.SEMICOLON.term) {
            match(Symbols.SEMICOLON.term)
            val child2 = statement(parent)
            if (parent != null) {
                parent.children.add(root)
                parent.children.add(child2)
            } else {
                root.children.add(child2)
            }
        }
        if (parent != null) return null
        return root
    }

    private fun statement(parent: SyntaxTreeNode? = null): SyntaxTreeNode {
        return when (token.term) {
            KeyWords.IF.term -> ifStatement()
            KeyWords.REPEAT.term -> repeatStatement()
            Term("identifier") -> assignStatement()
            KeyWords.READ.term -> readStatement()
            KeyWords.WRITE.term -> writeStatement()
            KeyWords.FOR.term -> forStatement(parent!!)
            KeyWords.DO.term -> doWhileStatement(parent!!)
            else -> throw IllegalArgumentException("Unexpected token: ${token.term}")
        }
    }

    private fun doWhileStatement(parent: SyntaxTreeNode): SyntaxTreeNode {
        val root = getNode(token)
        match(KeyWords.DO.term)
        statementSequence(root)
        match(KeyWords.WHILE.term)
        match(Symbols.LPAREN.term)
        root.children.add(expression())
        match(Symbols.RPAREN.term)
        return root
    }

    private fun forStatement(parent: SyntaxTreeNode): SyntaxTreeNode {
        val root = getNode(token)
        match(KeyWords.FOR.term)
        val assign = getNode(Token(Symbols.ASSIGN.term), token)
        match(Term("identifier"))
        match(Symbols.ASSIGN.term)
        val assignExp = simpleExpression()
        assign.children.add(assignExp)
        parent.children.add(assign)
        fun buildTo(): SyntaxTreeNode {
            val to = getNode(Token(Symbols.PLUS.term, "+"))
            to.children.add(assignExp)
            to.children.add(getNode(Token(Term("number"), "1")))
            return to
        }
        fun buildDownTo(): SyntaxTreeNode {
            val to = getNode(Token(Symbols.MINUS.term, "-"))
            to.children.add(assignExp)
            to.children.add(getNode(Token(Term("number"), "1")))
            return to
        }
        val condition = when (token.term) {
            KeyWords.TO.term -> {
                match(KeyWords.TO.term)
                expression() to buildTo()
            }
            KeyWords.DOWNTO.term -> {
                match(KeyWords.DOWNTO.term)
                expression() to buildDownTo()
            }
            else -> throw IllegalArgumentException("Unexpected token: ${token.term}")
        }
        match(KeyWords.DO.term)
        val child4 = statementSequence()
        root.children.add(child4!!)
        root.children.add(condition.second)
        fun buildEqual(): SyntaxTreeNode {
            val equal = getNode(Token(Symbols.EQUAL.term, "=="))
            equal.children.add(condition.first)
            return equal
        }
        root.children.add(buildEqual())
        match(KeyWords.ENDDO.term)
        return root
    }

    private fun ifStatement(): SyntaxTreeNode {
        val root = getNode(token)
        match(KeyWords.IF.term)
        val child1 = expression()
        root.children.add(child1)
        match(KeyWords.THEN.term)
        val child2 = statementSequence(root)
        if (child2 != null) {
            root.children.add(child2)
        }
        if (token.term == KeyWords.ELSE.term) {
            match(KeyWords.ELSE.term)
            val child3 = statementSequence()
            root.children.add(child3!!)
        }
        match(KeyWords.END.term)
        return root
    }

    private fun repeatStatement(): SyntaxTreeNode {
        val root = getNode(token)
        match(KeyWords.REPEAT.term)
        val child1 = statementSequence()
        root.children.add(child1!!)
        match(KeyWords.UNTIL.term)
        val child2 = expression()
        root.children.add(child2)
        return root
    }

    private fun assignStatement(): SyntaxTreeNode {
        val root = getNode(Token(Symbols.ASSIGN.term), token)
        match(Term("identifier"))
        match(Symbols.ASSIGN.term)
        val child = expression()
        root.children.add(child)
        return root
    }

    private fun readStatement(): SyntaxTreeNode {
        val root = getNode(token)
        match(KeyWords.READ.term)
        root.minor = token
        match(Term("identifier"))
        return root
    }

    private fun writeStatement(): SyntaxTreeNode {
        val root = getNode(token)
        match(KeyWords.WRITE.term)
        val writeBody = expression()
        root.children.add(writeBody)
        return root
    }

    private fun expression(): SyntaxTreeNode {
        val child1 = simpleExpression()
        if (token.term == Symbols.LESS.term ||
            token.term == Symbols.LESS_EQUAL.term ||
            token.term == Symbols.GREATER.term ||
            token.term == Symbols.GREATER_EQUAL.term ||
            token.term == Symbols.EQUAL.term ||
            token.term == Symbols.NOT_EQUAL.term
        ) {
            val root = comparisonOp()
            val child2 = simpleExpression()
            root.children.add(child1)
            root.children.add(child2)
            return root
        }
        return child1
    }

    private fun comparisonOp(): SyntaxTreeNode {
        val root = getNode(token)
        when (token.term) {
            Symbols.LESS.term -> match(Symbols.LESS.term)
            Symbols.LESS_EQUAL.term -> match(Symbols.LESS_EQUAL.term)
            Symbols.GREATER.term -> match(Symbols.GREATER.term)
            Symbols.GREATER_EQUAL.term -> match(Symbols.GREATER_EQUAL.term)
            Symbols.EQUAL.term -> match(Symbols.EQUAL.term)
            Symbols.NOT_EQUAL.term -> match(Symbols.NOT_EQUAL.term)
        }
        return root
    }

    private fun simpleExpression(): SyntaxTreeNode {
        val child = term()
        while (token.term == Symbols.PLUS.term || token.term == Symbols.MINUS.term) {
            val root = getNode(token)
            val child2 = addOp()
            val child3 = term()
        }
        return child
    }

    private fun addOp(): SyntaxTreeNode {
        val root = getNode(token)
        when (token.term) {
            Symbols.PLUS.term -> match(Symbols.PLUS.term)
            Symbols.MINUS.term -> match(Symbols.MINUS.term)
        }
        return root
    }

    private fun term(): SyntaxTreeNode {
        val root = factor()
        var op: SyntaxTreeNode? = null
        while (token.term == Symbols.TIMES.term || token.term == Symbols.DIVIDE.term) {
            op = mulOp()
            val child = factor()
            op.children.add(root)
            op.children.add(child)
        }
        if (op != null) return op
        return root
    }

    private fun mulOp(): SyntaxTreeNode {
        val root = getNode(token)
        when (token.term) {
            Symbols.TIMES.term -> match(Symbols.TIMES.term)
            Symbols.DIVIDE.term -> match(Symbols.DIVIDE.term)
        }
        return root
    }

    private fun factor(): SyntaxTreeNode {
        return when (token.term) {
            Term("identifier") -> {
                val root = getNode(token)
                match(Term("identifier"))
                root
            }
            Term("number") -> {
                val root = getNode(token)
                match(Term("number"))
                root
            }
            Symbols.LPAREN.term -> {
                match(Symbols.LPAREN.term)
                val root = expression()
                match(Symbols.RPAREN.term)
                root
            }
            else -> throw IllegalArgumentException("Unexpected token: ${token.term}")
        }
    }

    private fun getNode(token: Token? = null, minor: Token? = null): SyntaxTreeNode {
        return SyntaxTreeNode(token, minor, mutableListOf())
    }
}
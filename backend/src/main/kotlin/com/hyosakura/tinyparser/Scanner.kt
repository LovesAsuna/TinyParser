package com.hyosakura.tinyparser

import com.hyosakura.tinyparser.struct.Term
import com.hyosakura.tinyparser.struct.Token

/**
 * @author LovesAsuna
 **/
object Scanner {
    val keywords = setOf(
        "if", "then", "end", "else", "repeat", "until", "and", "or", "not",
        "read", "write", "while", "do", "for", "to", "downto", "enddo"
    )

    val symbols = setOf(
        ";", "=", "<", "==", "+", "-", "*", "/", "(", ")", ">", "^", ">=", "<=", "<>", "%"
    )

    fun split(code: String): List<Token> {
        val tokenList = mutableListOf<Token>()
        splitSingleLine(code, tokenList)
        return tokenList
    }

    private fun splitNumber(str: String): Pair<Token, String> {
        val firstPart = str.takeWhile(Char::isDigit)
        val rest = str.substring(firstPart.length)
        return if (rest.isEmpty() || !rest.startsWith(".")) {
            Token(Term("number"), firstPart) to rest
        } else {
            val secondPart = rest.substring(1).takeWhile(Char::isDigit)
            Token(Term("number"), "$firstPart.$secondPart") to str.drop(firstPart.length + 1 + secondPart.length)
        }
    }

    private fun splitIdentifier(str: String): Pair<Token, String> {
        val rest = str.substring(1)
        val result = rest.takeWhile {
            it.isLetter() || it.isDigit() || it == '_'
        }
        val id = "${str.first()}$result"
        val strType = if (keywords.contains(id)) id else "identifier"
        return Token(Term(strType), id) to str.substring(1 + result.length)
    }

    private fun splitSingleLine(line: String, tokens: MutableList<Token>) {
        if (line.isEmpty()) {
            tokens.add(Token(Term("$")))
            return
        }
        val c = line.first()
        when {
            c == '{' -> splitSingleLine(line.substring(1).dropWhile { it != '}' }.substring(1), tokens)
            c.isWhitespace() -> splitSingleLine(line.substring(1), tokens)
            c.isLetter() || c == '_' -> {
                val (token, rest) = splitIdentifier(line)
                tokens.add(token)
                splitSingleLine(rest, tokens)
            }
            c.isDigit() -> {
                val (token, rest) = splitNumber(line)
                tokens.add(token)
                splitSingleLine(rest, tokens)
            }
            c == '\"' -> {
                val regex = line.substring(1).takeWhile { it != '\"' }
                tokens.add(Token(Term("regex"), regex))
                splitSingleLine(line.substring(1).dropWhile { it != '\"' }.substring(1), tokens)
            }
            symbols.any { line.startsWith(it) } -> {
                val symbol = symbols.filter {
                    line.startsWith(it)
                }.maxByOrNull {
                    it.length
                }!!
                tokens.add(Token(Term(symbol), symbol))
                splitSingleLine(line.substring(symbol.length), tokens)
            }
        }
    }
}
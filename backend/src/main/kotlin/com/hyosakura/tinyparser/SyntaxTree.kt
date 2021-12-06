package com.hyosakura.tinyparser


/**
 * @author LovesAsuna
 **/
class SyntaxTree(val root: SyntaxTreeNode) {
    override fun toString(): String {
        val builder = StringBuilder("")
        toString(builder, root, -1)
        return builder.substring(1).toString()
    }

    private fun toString(builder: StringBuilder, node: SyntaxTreeNode, indent: Int) {
        for (i in 0 until indent) {
            builder.append("  ")
        }
        builder.append(node.toString())
        builder.append("\n")
        for (child in node.children) {
            toString(builder, child, indent + 1)
        }
    }
}

data class SyntaxTreeNode(
    val token: Token?,
    var minor: Token?,
    val children: MutableList<SyntaxTreeNode>
) {
    override fun toString(): String {
        if (token == null) {
            return ""
        }
        return when (token.term.symbol) {
            "=" -> "Assign to: ${minor!!.value}"
            in Scanner.symbols -> "Op: ${token.value}"
            "identifier" -> "Id: ${token.value}"
            "number" -> "const: ${token.value}"
            else -> "${token.term}${if (minor != null) " :${minor!!.value}" else ""}"
        }
    }
}
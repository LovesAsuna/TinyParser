package com.hyosakura.tinyparser

fun main() {
    val parser = Parser()
    val test1 =
        """
            { Sample program
              in TINY language -
              computes factorial
            }
            read x; { input an integer }
            if ( 0<x ) then { don't compute if x <= 0 }
              for  fact = x downto 1 
              do 
                fact = fact * x
              enddo;
              write fact  { output factorial of x }
            end
        """.trimIndent()
    val test2 =
        """
            { Sample program
              in TINY language -
              computes factorial
            }
            read x; { input an integer }
            if ( x>0 ) then { don't compute if x <= 0 }
              fact = 1;
              do 
                fact = fact * x;
                x = x - 1
              while((x>0));
              write fact  { output factorial of x }
            end
        """.trimIndent()
    val tree = parser.parseToTree(test2)
    println(tree)
}
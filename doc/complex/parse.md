# parsing

grammar without precedence:

    S ::= (S)                                // parentheses
        | i | e | π | ∞ | decimalNumber      // constants
        | z | x                              // variable
        | S!                                 //factorial
        | +S | -S
        | arg S
        | conj S
        | cos S
        | cosh S
        | exp S
        | Im S
        | ln S
        | mod S
        | |S|
        | opp S
        | Re S
        | sin S
        | sinh S
        | tan S
        | tanh S
        | S + S     
        | S - S     
        | S * S     
        | S / S     
        | S \ S
        | SS                                 // invisible multiplication
        
precedence table:

    (binds less)
    E+E E-E     //summand
    E*E E/E     //factor
    E\E
    sin E | +E | -E
    E!
    EE          //invisible multiplication
    (binds more)


grammar with precedence:
(first weak binding, then strong)
"E" means the next lower parser.

    expr        ::= E
    summands    ::= E ~ rep( "+"~E | "-"~E )
    factors     ::= E ~ rep( "*"~E | "/"~E )
    powers      ::= E ~ rep( "\"~E )
    functions   ::= rep( "-" | "+" | "arg" | "conj" | ...)~E
    factorial   ::= E ~ rep( "!" )
    
    atom        ::= constants | decimal | parens | abs
    
    constants   ::= """(\d+(\.\d*)?)?([xzieπ∞]+)"""                 // like 2.5πi no spaces
    decimal     ::= decimalNumber
    parens      ::= "(" ~> expr <~ ")" 
    abs         ::= "|" ~ expr ~ "|"
    
This grammar does not produce 3 sin 0 = 3 * sin(0).

## possible simplications

1. invisible-multiplication restricted
    - decimal only as first factor
    - other factors must be single-character: xzieπ∞
    - all other multiplications must be visible
    - 1+2i
    - πi
2. functions must have parentheses
    - sin(π)

## todo

* better-prefix, most important: e\\-πi
* functions without parentheses
* space-multiplication

## future

Maybe this is faster: 
http://www.lihaoyi.com/post/Fastparse2EvenFasterScalaParserCombinators.html
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

    (binds more)
    E!
    AB          //invisible multiplication
    sin A
    A\B
    A*B A/B     //factor
    -A +A       //prefix
    A+B A-B     //summand
    (binds less)


grammar with precedence:
(first weak binding, then strong)

    E ::= E0
    E0 ::= E1 ~ rep( "+"~E1 | "-"~E1 )      // summands
    E1 ::= rep( "-" | "+" ) ~ E2            // prefix
    E2 ::= E3 ~ rep( "*"~E3 | "/"~E3 )      // factors
    E3 ::= E4 ~ rep( "\"~E4 )               // power
    E4 ::= rep( "arg" | "conj" | ...)~E5    // functions
    E5 ::= E6 ~ rep( E6 )                   // invisible multiplication
    E6 ::= E7 ~ rep( "!" )                  // factorial
    E7 ::= "z" | "x" | "i" | "e" | "π" | "∞" | decimalNumber | "("~E0~")"
    
This grammar does not produce 3 sin 0 = 3 * sin(0).
        
## future

Maybe this is faster: 
http://www.lihaoyi.com/post/Fastparse2EvenFasterScalaParserCombinators.html
# parsing

grammar without precedence:

    S ::= i | e | π | ∞ | decimalNumber      // constants
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
        
precedence table:

    (binds more)
    AB          //invisible multiplication
    A! sin(A)
    A\B
    A*B A/B     //factor
    -A +A       //prefix
    A+B A-B     //summand
    (binds less)


grammar with precedence:

    E ::= E0
    E0 ::= E1 ~ rep( "+"~E1 | "-"~E1 )      // summands
    E1 ::= rep( "-" | "+" ) ~ E2            // prefix
    E2 ::= E3 ~ rep( "*"~E3 | "/"~E3 )      // factors
    E3 ::= E4 ~ rep( "\"~E4 )               // power
    E4 ::= E5 ~ rep( "!" )                  // factorial
    E5 ::= rep( "arg" | "conj" | ...)~E6    // functions
    E6 ::= E7 ~ rep( E7 )                   // invisible multiplication
    E7 ::= "z" | "x" | "i" | "e" | "π" | "∞" | decimalNumber | "("~E0~")"
        
## future

Maybe this is faster: 
http://www.lihaoyi.com/post/Fastparse2EvenFasterScalaParserCombinators.html
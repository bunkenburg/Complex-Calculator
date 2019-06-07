# parsing

grammar:

    S ::= K         // constants
        | z         // variable
        | x         // variable
        | S!        //factorial
        | +S
        | -S
        | f S       // conj sinh cosh tanh arg cos sin exp ab opp sin tan Im ln Re
        | S + S     // + - * / \\
        
precedence:

    (binds more)
    [invisible multiplication]
    \
    * /
    + -
    (binds less)
    
code:
    
    Expression ::= 
          B(E, token, E)    //binary
        | C(Complex)        //constant
        | U(token, E)       //unary function
        | V                 //variable
    
## future

Maybe this is faster: 
http://www.lihaoyi.com/post/Fastparse2EvenFasterScalaParserCombinators.html
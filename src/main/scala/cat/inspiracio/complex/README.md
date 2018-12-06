# Complex Numbers

How does the client programmer use Complex numbers?

    import cat.inspiracio.complex._

One type:

    val z: Complex
    
Literals will be transformed to Complex:

    0: Byte
    1: Int
    2L: Long
    3.2: Float
    4.5: Double

Constants:

    e : Double
    π : Double
    i : Complex
    ∞ : Complex
    
Operators:
    
       +a
       -a
    a + b
    a - b
    a * b
    a / b
    a ^ b   Careful: precedence not right.

Functions:

    sin(z)
    sinh(z)
    cos(z)
    cosh(z)
    tan(z)
    tanh(z)
    exp(z)
    ln(z)

Comparison:

    a == b              Exact for floating point.
    a === b             Exact for floating point.
    a === b +- 0.1      Approximate equality.

Formatting with toString:

    e     (when precise)
    -e    (when precise)
    π     (when precise)
    -π    (when precise)
    i
    15    (when integer)
    15.2  (when real)
    12.8i (when imaginary)
    3 + 2i
    ∞

Pattern matching:

    z match {

        case Natural(17) => ???
        case Natural(n) => ???   //matches natural

        case Integer(18) => ???
        case Integer(n) => ???   //matches integer
        
        case E => ???   // matches e
        case Pi => ???  // matches π
        case I => ???  // matches i
        
        case Real(0) => ??? //matches a finite real number
        case Real(re) => ??? //matches any finite real number

        case Imaginary(4.2) => ???  //imaginary
        case Imaginary(im) => ??? //imaginary
        
        case Polar(5, Math.PI) => ???
        case Polar(m, a) => ???
        
        case Cartesian(3.2, 4.1) => ???
        case Cartesian(re, im) => ???
        
        case Infinity => ??? 
    }

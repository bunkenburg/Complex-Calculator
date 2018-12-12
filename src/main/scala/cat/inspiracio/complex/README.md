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
    cos(z)
    tan(z)

    sinh(z)
    cosh(z)
    tanh(z)

    exp(z)
    ln(z)

Comparison:

    a == b              Exact for floating point.
    a === b             Exact for floating point.
    a === b +- 0.1      Approximate equality with angle on sphere in radians.

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
        case Natural(n) => ???   //binding n

        case Integer(18) => ???
        case Integer(n) => ???   //binding n
        
        case I => ???  // matches i
        
        case Real(0) => ??? //matches a finite real number
        case Real(re) => ??? //binding re

        case Imaginary(4.2) => ???  //imaginary
        case Imaginary(im) => ??? //binding im
        
        case Polar(5, Pi) => ???
        case Polar(m, a) => ??? //binding m and a
        
        case Cartesian(3.2, 4.1) => ???
        case Cartesian(re, im) => ???   //binding re and im
        
        case ∞ => ??? 
    }

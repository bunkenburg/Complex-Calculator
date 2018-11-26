# Complex Numbers

How does the client programmer use Complex numbers?

    import cat.inspiracio.numbers.Complex._

One type:

    val z: Complex
    
Constants:

    e : Double
    π : Double
    i : Complex
    ∞ : Complex

Functions:

    -z
    sin(z)
    sinh(z)
    cos(z)
    cosh(z)
    tan(z)
    tanh(z)
    exp(z)
    ln(z)

Operators:
    
    a + b
    a - b
    a * b
    a / b
    a ^ b

They also work when the first argument has of types Byte, Int, Long, Float, Double because there are conversions from these types to Complex.

They are overloaded so that the second argument can have type Byte, Int, Float, Double, Complex.

Comparison:

    ==  Complex.equals() Problems with floats.
    === Complex.complexEq Uses scalatics.

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
        
        case Polar(m, a) => ???
        case Polar(5, Math.PI) => ???
        
        case Cartesian(3.2, 4.1) => ???
        case Cartesian(re, im) => ???
        
        case Infinity => ??? 
    }

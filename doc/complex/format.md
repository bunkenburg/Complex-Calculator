# complex number formatting

## objectives

- reasonable precision
- configurable precision
- represent in terms of π for angles
- represent integers
- represent rationals like 1/2 3/4 1/3 2/3

Specify:
* precision
* cartesian or polar?

where implemented?

    CartesianComplex
        format(Double): String
        toString(): String
    ∞.toString

## what mechanisms in Java/Scala ?

### string interpolation

    f"..."
    String interpolation
    custom string interpolation:
        https://docs.scala-lang.org/overviews/core/string-interpolation.html
        https://docs.scala-lang.org/sips/string-interpolation.html

    abstract class java.text.Format
    interface java.util.Formattable

    %[argument_index$][flags][width][.precision]conversion
    %[flags][width]conversion

    argument_index = 1$ 2$ 3$ ..

    flags
        -   left-justify
        #   alternate
        +   always sign
            leading space for positive
        0   zero-padded
        ,   grouping
        (   negative in parenthesis

    width   
    The optional width is a positive decimal integer indicating the minimum 
    number of characters to be written to the output. 
    
    .precision
    The optional precision is a non-negative decimal integer usually used to 
    restrict the number of characters. The specific behavior depends on the conversion. 
        aef after decimal point
        g   total digits
    
    conversion:
    The required conversion is a character indicating how the argument should be 
    formatted. The set of valid conversions for a given argument depends on the 
    argument's data type. 
        b   boolean
        h   hex
        s   Formattable.formatTo or toString
        c   character
        d   decimal integer     <-- for our Double
        o   octal
        x   hex
        e   scientific          <-- may be relevant?
        f   float: decimal      <-- may be relevant?               
        g   scientific or float, depends on precision and rounding  <-- maybe better?
        a   hex float
        t   time
        %
        n   line

    scala> f"${i}%-#20.3S"
    res23: String = alternate=true left=true upper=true locale=en_GB width=20 precision=3

add the method:

    class Complex extends Formattable    
    
      override def formatTo(fmt: Formatter, flags: Int, width: Int, precision: Int): Unit = {
        val locale = fmt.locale
        val alternate = (flags & ALTERNATE) == ALTERNATE
        val left = (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY
        val upper = (flags & UPPERCASE) == UPPERCASE
        //width
        //precision
        fmt.format(...)
      }

### java.text.NumberFormat

base class:

    abstract class java.text.Format {
        format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
        formatToCharacterIterator(Object obj)
        parseObject(String source, ParsePosition pos) 
    }

for double numbers:

    abstract class NumberFormat extends Format {
    
        //formatToCharacterIterator(Object obj) 
    
        format(double): String
        abstract format(double number, StringBuffer toAppendTo, FieldPosition pos): StringBuffer
    
        format(long): String
        abstract format(long number, StringBuffer toAppendTo, FieldPosition pos): StringBuffer
    
        abstract format(Object number, StringBuffer toAppendTo, FieldPosition pos): StringBuffer
    
        getCurrency(): Currency     <--- irrelevant
        setCurrency(c)
    
        getMaximumFractionDigits(): int
        setMaximumFractionDigits(Int)
        
        getMinimumFractionDigits(): int
        setMinimumFractionDigits(Int)
        
        getMaximumIntegerDigits(): Int
        setMaximumIntegerDigits(Int)
        
        getMinimumIntegerDigits(): Int
        setMinimumIntegerDigits(Int)
        
        getRoundingMode(): RoundingMode
        setRoundingMode(RoundingMode)
        
        isGroupingUsed(): Boolean   <--- not interested
        setGroupingUsed(Boolean)
        
        isParseIntegerOnly(): Boolean   <--- not interested
        setParseIntegerOnly(Boolean)
        
        parse(source: String): Number   <-- Can Complex extend Number?
        parse(source: String, p: ParsePosition): Number
        parseObject(String source, ParsePosition pos): Number
    }

The class DecimalFormat uses patterns.
If I make my own patterns, lots of freedom, but can get very complicated
and it is not needed by any client or use case.

    class DecimalFormat extends NumberFormat{ 
        DecimalFormat(pattern: String)
        
        applyPattern(pattern: String)
        applyLocalizedPattern(pattern: String)
        
        getDecimalFormatSymbols(): DecimalFormatSymbols <-- not interested
        setDecimalFormatSYmbols(DecimalFormatSymbols)
        
        getGroupingSize(): Int  <-- not interested
        setGroupingSize(Int)
        
        getMultiplier(): Int    <-- 100 for percent
        setMultiplier(Int)
        
        getNegativePrefix(): String <-- not interested
        getNegativeSuffix(): String
        
        getPositivePrefix(): String <-- not interested
        getPositiveSuffix(): String
        
        isDecimalSeparatorAlwaysShown(): Boolean    <-- not interested
        setDecimalSeparatorAlwaysShown(Boolean)
        
        isParseBigDecimal(): Boolean    <-- does not apply
        setParseBigDecimal(Boolean)
        
        toLocalizedPattern(): String    
        toPattern(): String
    }

ComplexFormat extends NumberFormat

    /** A class like this would give me any freedom that I need. */
    class ComplexFormat extends NumberFormat{
        
        format(Complex): String
        parse(String): Complex
        
        setCartesian(Boolean)
        setPolar(Boolean)
    }

Can Complex implement java.lang.Number?

    /** Convertible to primitive types.
        Conversion may lose information about magnitude.
        Conversion may lose precision.
        Conversion may return a different sign!
        Can conversion also lose the imaginary part?
    */
    abstract class java.lang.Number {
        byteValue: byte
        doubleValue: double
        floatValue: float
        intValue: int
        longValue: long
        shortValue: short
    }
    
Complex could implement that, with useless implementations.

## classes

* Complex implements Formattable
* ComplexFormat extends NumberFormat

For easy configuration, best if ComplexFormat does the work: 
it has most information, and we can always add
more configuration by adding setters to it.

But better to let Complex do toString: don't want
to instantiate ComplexFormat for each c.toString.

    class Complex {
        def toString = formatTo(some defaults)
        def formatTo(...)
    }

Configuration:

* locale    --nothing
* alternate --for polar
* left      --nothing
* upper     --nothing
* width     --nothing, we are not making fixed-width fields
* precision --interesting
* maximumFractionDigits
* minimumFractionDigits
* maximumIntegerDigits
* minimumIntegerDigits
* currency
* roundingMode  --can be interesting
* groupingUsed
* parseIntegerOnly

## Double.toString spec as inspiration

https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Double.html

1. NaN -> "NaN"
2. val sign = "-" or ""
3. val m = magnitude
4. m==infinite => "Infinity"
5. m==0 => "-0.0" or "0.0"
6. 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05
7. m < 10⁻³ or 10⁷ < m => "computerized scientific notation"

    val n = such that 10^n ≤ m < 10^(n+1)
    
    val a = the mathematically exact quotient of m and 10^n so that 1 ≤ a < 10
    
    the integer part of a, as a single decimal digit, followed by '.', 
    followed by decimal digits representing the fractional part of a, 
    followed by the letter 'E', followed by a representation of n as a decimal integer, 
    as produced by the method Integer.toString(int)

How many digits must be printed for the fractional part of m or a? 

There must be at least one digit to represent the fractional part, 
and beyond that as many, but only as many, more digits as are needed to 
uniquely distinguish the argument value from adjacent values of type double. 
That is, suppose that x is the exact mathematical value represented by the 
decimal representation produced by this method for a finite nonzero argument d. 
Then d must be the double value nearest to x; or if two double values are equally 
close to x, then d must be one of them and the least significant bit of the 
significand of d must be 0.

In practice, there can be up to 16 digits ... must restrict that in a configurable way.

10^(-3) = 0.001

10⁷ = 10000000


## spec for Complex.toString

0. null => I don't care.

1. if the number is integer and small, all digits, no point.

    -9999999    -> "-9999999"
    
    -1000000    -> "-1000000"
    
    -1000       -> "-1000"
    
    -1          -> "-1"
    
    0           -> "0"
    
    1           -> "1"
    
    1000        -> "1000"
    
    1000000     -> "1000000"
    
    9999999     -> "9999999"
    
    choose a limit:
    * Double.toString limit for scientific = 10⁷ = 10 000 000
    * Integer.MAX_VALUE = 2^31 -1 =             2 147 483 647 
    * Long.MAX_VALUE = 2^63 -1 =    9 223 372 036 854 775 807
    
    small = |n| < limit
    
2. if the number is real:

    1. NaN -> "NaN"                             //should never happen
    5. m==0 => "0"                  //already covered as natural
    4. m==infinite => "∞"
    2. val sign = "-" or ""
    3. val m = magnitude
    
    5. m==e => "e"
    5. m==π => "π"
    
    6. 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05     //ok as is, maybe cut trailing 0 and .
    
    7. m < 10⁻³ or 10⁷ < m => "computerized scientific notation"

        val n = such that 10^n ≤ m < 10^(n+1)
        
        val a = the mathematically exact quotient of m and 10^n so that 1 ≤ a < 10
        
        the integer part of a, as a single decimal digit, followed by '.', 
        followed by decimal digits representing the fractional part of a, 
        followed by the letter 'E', followed by a representation of n as a decimal integer, 
        as produced by the method Integer.toString(int)
        
        restrict digits in a amb n to precision
        
        clean it: "-3.153E-8" => "-3.153 * 10^-8"
        
        //if a==1 , just 10^n ?
        
        //drop trailing 0 or a ?

3. if the number is imaginary:

    1. NaN -> "NaN"                             //should never happen
    2. val sign = "-" or ""
    3. val m = magnitude
    
    4. m==infinite => "∞"
    5. m==0 => "0"                  //already covered as natural
    5. m==e => "ei"
    5. m==π => "πi"
    
    6. 10⁻³ <= m < 10⁷ => "1655.0i" or "7856.05i"     //ok as is, maybe cut trailing 0 and .
    
    7. m < 10⁻³ or 10⁷ < m => "computerized scientific notation"

        val n = such that 10^n ≤ m < 10^(n+1)
        
        val a = the mathematically exact quotient of m and 10^n so that 1 ≤ a < 10
        
        the integer part of a, as a single decimal digit, followed by '.', 
        followed by decimal digits representing the fractional part of a, 
        followed by the letter 'E', followed by a representation of n as a decimal integer, 
        as produced by the method Integer.toString(int)
        
        restrict digits in a amb n to precision
        
        clean it: "-3.153E-8.764" => "-3.153i * 10^(-8.764)"

        //if a==1 , just i * 10^n ?
        
        //drop trailing 0 or a ?

4. if the number is complex:

    val r = formatReal(re)
    
    val i = formatImaginary(im)
    
    if 0<i => r + " + " + i    
    
    if i<0 => r + " - " + i    
    
5. if polar is requested:
    
    1. 0
    2. ∞
    3.  val m =     //taking into account precision
        
        val rho = principal value
        
        ${m}e^${rho}πi  like    3.21e^0.75πi
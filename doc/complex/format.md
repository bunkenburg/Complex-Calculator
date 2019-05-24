# complex number formatting

objectives:

- reasonable rounding
- configurable rounding
- represent in terms of pi
    for angles
- represent integers
- represent rationals
    1/2 3/4 1/3 2/3

1/3 -> 0.333 or 0.3333333 or 1/3
2 * e^πi

Specify:
* precision
* cartesian or polar?

where implemented?

    CartesianComplex
        toString(Double): String
        toString(): String
    ∞.toString

what else is there?

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
    .precision
        aef after decimal point
        g   total digits
    conversion:
        b   boolean
        h   hashcode
        s   Formattable.formatTo or toString
        c   character
        d   decimal integer
        o   octal
        x   hex
        e   scientific
        f   float
        g   scientific or float
        a   hex float
        t   time
        %
        n   line

    scala> f"${i}%-#20.3S"
    res23: String = alternate=true left=true upper=true locale=en_GB width=20 precision=3

let's do it!

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

abstract class java.text.Format {
    format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    formatToCharacterIterator(Object obj)
    parseObject(String source, ParsePosition pos) 
}

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

// Pattern! If I make my own patterns, lots of freedom but complicated.
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

/** A class like this would give me any freedom that I need. 
    And Formattable can be implemented using this class.
*/
class ComplexFormat extends NumberFormat{
    
    format(Complex): String
    parse(String): Complex
    
    setCartesian(Boolean)
    setPolar(Boolean)
    findPi
    findQuotients
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
Complex could implement that, with implementation that are mostly useless.
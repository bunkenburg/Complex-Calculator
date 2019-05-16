# complex number formatting

objectives:

- reasonable rounding
- configurable rounding
- represent in terms of pi
    for angles
- represent integers
- represent rationals
    1/2 3/4 1/3 2/3

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

    java.util.Format
    java.util.Formattable

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
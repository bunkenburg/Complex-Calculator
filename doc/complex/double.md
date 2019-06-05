    def triple(d: Double)= {
      val m = abs(d)
      if(0.003 <= m && m < 10000000)
        throw new RuntimeException

      val s = d.toString
      val parts = s.split("E")

      val sign = if(d < 0.0) -1 else +1
      val mantissa = parts(0).toDouble
      val exponent = parts(1).toInt
      (sign, mantissa, exponent)
    }

            // This gets the mantissa and exponent --- but in base 2 which is not what I want!
            // https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Double.html
            val bits: Long = java.lang.Double.doubleToRawLongBits(d)
            val d1 = java.lang.Double.longBitsToDouble(bits)
            val sign: Int = if((bits >> 63) == 0) 1 else -1
            val exponent = (bits >> 52) & 0x7ffL
            val mantissa = if(exponent == 0)
              (bits & 0xfffffffffffffL) << 1
            else
              (bits & 0xfffffffffffffL) | 0x10000000000000L
            val result = sign * mantissa * 2\(exponent-1075)
            val posinf = bits==0x7ff0000000000000L
            val neginf = bits== 0xfff0000000000000L


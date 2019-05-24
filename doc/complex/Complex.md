# Complex numbers

aims:

* only one import: import cat.inspiracio.complex._
* syntax as close as possible to standard mathematics syntax
* geared towards view as points on Riemann sphere

trait and classes:

* abstract class cat.inspiracio.complex.Complex
    - serves as type
    - gives default implementations for many operators
* class cat.inspiracio.complex.imp.CartesianComplex(re: Double, im: Double)
    - always finite
    - lazy in re, im, mod, arg
* object cat.inspiracio.complex.∞
    - because ∞ often is a special case
    - is the only infinite value
    
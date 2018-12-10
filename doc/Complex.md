# Complex numbers

aims:

* only one import: import cat.inspiracio.complex._
* syntax as close as possible to standed mathematics syntax
* geared towards view as points on Riemann sphere

trait and classes:

* trait cat.inspiracio.complex.Complex
    - serves as type
    - declares functions like sin
    - gives default implementations for many operators
* class cat.inspiracio.complex.imp.CartesianComplex(re: Double, im: Double)
    - always finite
    - make lazy in re, im, mod, arg
* object cat.inspiracio.complex.imp.Infinity
    - because ∞ often is a special case
    - is the only infinite value
    
## todo

* get rid of finite
* get rid of isZero
* get rid of modulus
* get rid of argument
* for re im modulus arguments: make them private lazy val. Calculate at most once.
x remove methods sin and similar: syntax not natural
x are Complex, Real, Infinity, etc in the right place?

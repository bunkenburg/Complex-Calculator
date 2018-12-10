# Complex numbers

aims:

* only one import: import cat.inspiracio.complex._
* syntax as close as possible to standed mathematics syntax
* geared towards view as points on Riemann sphere

trait and classes:

* trait cat.inspiracio.complex.Complex
    - serves as type
    - declares methods other classes have to implement
    - gives default implementations for many methods
* class cat.inspiracio.complex.imp.CartesianComplex(re: Double, im: Double)
    - always finite
    - make lazy in re, im, mod, arg
* object cat.inspiracio.complex.imp.Infinity
    - because ∞ often is a special case
    - is the only infinite value
    
## todo

* remove methods sin and similar: syntax not natural
* for re im modulus arguments: make them lazy. Calculate at most once.
* get rid of finite
* get rid of isZero
x are Complex, Real, Infinity, etc in the right place?

# Complex numbers

aims:

* only one import: import cat.inspiracio.complex._
* syntax as close as possible to standed mathematics syntax
* geared towards view as points on Riemann sphere

implementations:

* trait Complex
    - serves as type
    - declares methods other classes have to implement
    - gives default implementations for many methods
* class Real(r: Double)
    - many simplified functions, like 
    - sin = Math.sin
    - sinh
    - exp
    - ln
    - We don't need this, because it still inherits from 
        CartesianComplex. It doesn't really help because it
        is not disjunct from CartesianComplex.
* class CartesianComplex(re: Double, im: Double)
    - always finite
* object Infinity
    - because ∞ often is a special case
    - is the only infinite value
    
## todo

* are Complex, Real, Infinity, etc in the right place?
* for re im modulus arguments: make them lazy. Calculate at most once.

* trait Complex
    - serves as type
    - declares methods other classes have to implement
    - gives default implementations for many methods
* class Real(r: Double)
    - many simplified functions, like 
    - sin = Math.sin
    - sinh
    - exp
    - ln
* class CartesianComplex(re: Double, im: Double)
    - always finite
* object Infinity
    - because ∞ often is a special case
    - is the only infinite value

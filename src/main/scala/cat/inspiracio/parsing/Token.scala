/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
 *
 * This file is part of Complex Calculator.
 *
 * Complex Calculator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Complex Calculator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Complex Calculator. If not, see <http://www.gnu.org/licenses/>.
 * */
package cat.inspiracio.parsing

object Token extends Enumeration {
  type Token = Value

  val Unknown = Value("NOTOKEN")

  val Conj = Value("conj")
  val Cosh = Value("cosh")
  val Tanh = Value("tanh")
  val Arg = Value("arg")
  val Cos = Value("cos")
  val Exp = Value("exp")
  val Mod = Value("mod")
  val Opp = Value("opp")
  val Sin = Value("sin")
  val Tan = Value("tan")
  val Im = Value("Im")
  val Ln = Value("ln")
  val Re = Value("Re")
  val Sinh = Value("sinh")
  val Fac = Value("!")
  val Asin = Value("asin")
  val Acos = Value("acos")
  val Atan = Value("atan")

  val Plus = Value("+")
  val Minus = Value("-")
  val Mult = Value("*")
  val Div = Value("/")
  val Power = Value("\\")
}

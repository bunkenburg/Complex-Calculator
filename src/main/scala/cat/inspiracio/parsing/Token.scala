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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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

  val NOTOKEN = Value("NOTOKEN")

  val CONJTOKEN = Value("conj")
  val COSHTOKEN = Value("cosh")
  val TANHTOKEN = Value("tanh")
  val ARGTOKEN = Value("arg")
  val COSTOKEN = Value("cos")
  val EXPTOKEN = Value("exp")
  val MODTOKEN = Value("mod")
  val OPPTOKEN = Value("opp")
  val SINTOKEN = Value("sin")
  val TANTOKEN = Value("tan")
  val IMTOKEN = Value("Im")
  val LNTOKEN = Value("ln")
  val RETOKEN = Value("Re")
  val DTOKEN = Value("D")
  val SINHTOKEN = Value("sinh")
  val FACTOKEN = Value("!")
  val ASINTOKEN = Value("asin")
  val ACOSTOKEN = Value("acos")
  val ATANTOKEN = Value("atan")

  val SUMTOKEN = Value("+")
  val DIFFERENCETOKEN = Value("-")
  val PRODUCTTOKEN = Value("*")
  val QUOTIENTTOKEN = Value("/")
  val POWERTOKEN = Value("^")
}

/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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

import cat.inspiracio.complex._

// Referenced classes of package bunkenba.parsing:
//            SyntaxTree

class SyntaxTreeBinary(var token: Int, var left: SyntaxTree, var right: SyntaxTree) extends SyntaxTree {

  override def unparse: String = "(" + left.unparse + ")" + SyntaxTree.token2String(token) + "(" + right.unparse + ")"

  override def evaluate(z: Complex): Complex = {

    val a: Complex = left.evaluate(z)
    val b: Complex = right.evaluate(z)

    token match {

      case -1 => throw new RuntimeException("SyntaxTreeBinary.evaluate(NOTOKEN)")

      case 0 => a + b
      case 1 => a - b
      case 2 => a * b
      case 3 => a / b
      case 4 => a ^ b

      case 5 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 6 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 7 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 8 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 9 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 10 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 11 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 12 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 13 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 14 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 15 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 16 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 17 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
      case 18 => throw new RuntimeException("SyntaxTreeBinary.evaluate with unary token " + SyntaxTree.token2String(token))
    }

  }
}
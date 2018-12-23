/*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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
package cat.inspiracio.parsing;

// Referenced classes of package bunkenba.parsing:
//            SyntaxTree

import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.package$;
import cat.inspiracio.numbers.BugException;
import cat.inspiracio.numbers.PartialException;

public class SyntaxTreeUnary extends SyntaxTree {

    public SyntaxTreeUnary(int i, SyntaxTree syntaxtree) {
        token = i;
        argument = syntaxtree;
    }

    public String unparse(){
        if(token == 20)
            return "(" + argument.unparse() + ")!";
        else
            return SyntaxTree.token2String(token) + "(" + argument.unparse() + ")";
    }

    public void partialEvaluate() throws BugException{
        throw new BugException("SyntaxTreeUnary.partialEvaluate not implemented.");
    }

    public Complex evaluate(Complex ec) throws BugException, PartialException {
        Complex ec1 = argument.evaluate(ec);
        Complex ec2 = null;
        switch(token) {
        case -1: 
            throw new BugException("SyntaxTreeUnary.evaluate(NOTOKEN)");

        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
            throw new BugException("SyntaxTreeUnary.evaluate(acos,asin,atan)");

        case 20: // '\024'
            ec2 = package$.MODULE$.fac(ec1);
            break;

        case 0: // '\0'
            ec2 = ec1;
            break;

        case 1: // '\001'
            ec2 = ec1.unary_$minus();
            break;

        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
            throw new BugException("SyntaxTreeUnary.evaluate with binary token " + SyntaxTree.token2String(token));

        case 5: // '\005'
            ec2 = package$.MODULE$.conj(ec1);
            break;

        case 19: // '\023'
            ec2 = package$.MODULE$.sinh( ec1 );
            break;

        case 6: // '\006'
            ec2 = package$.MODULE$.cosh(ec1 );
            break;

        case 7: // '\007'
            ec2 = package$.MODULE$.tanh( ec1 );
            break;

        case 8: // '\b'
            ec2 = argument(ec1);
            break;

        case 9: // '\t'
            ec2 = package$.MODULE$.cos( ec1 );
            break;

        case 10: // '\n'
            ec2 = package$.MODULE$.exp( ec1 );
            break;

        case 11: // '\013'
            ec2 = abs( ec1 );
            break;

        case 12: // '\f'
            ec2 = package$.MODULE$.opp( ec1 );
            break;

        case 13: // '\r'
            ec2 = package$.MODULE$.sin( ec1 );
            break;

        case 14: // '\016'
            ec2 = package$.MODULE$.tan( ec1 );
            break;

        case 15: // '\017'
            ec2 = Im( ec1 );
            break;

        case 16: // '\020'
            ec2 = package$.MODULE$.ln( ec1 );
            break;

        case 17: // '\021'
            ec2 = Re( ec1 );
            break;

        case 18: // '\022'
            throw new BugException("SyntaxTreeUnary.evaluate with unexpected token " + SyntaxTree.token2String(token));
        }
        return ec2;
    }

    private SyntaxTree argument;
    private int token;
}

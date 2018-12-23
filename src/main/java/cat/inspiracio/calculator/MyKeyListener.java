/*	Copyright 2018 Alexander Bunkenburg alex@inspiracio.cat
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
package cat.inspiracio.calculator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static cat.inspiracio.calculator.Calculator.Mode.CALC;

class MyKeyListener extends KeyAdapter {

    private Calculator calculator;

    MyKeyListener(Calculator c){ calculator=c; }

    @Override public void keyPressed(KeyEvent keyevent){
        int i = keyevent.getKeyCode();
        if(i==8 || i==127){
            calculator.display.delete();
            keyevent.consume();
            return;
        }
        if(i==10){
            if(calculator.mode == CALC){
                calculator.eraseOldResult();
                calculator.doEquals();
            }
            keyevent.consume();
            return;
        }
        if(i==12){
            calculator.display.clearAll();
            if(calculator.mode!=CALC) {
                String s = "f(" + calculator.variable + ") = ";
                calculator.display.prepend(s);
            }
            keyevent.consume();
            return;
        }
        if(i!=37 && i!=39)
            keyevent.consume();
    }

    @Override public void keyTyped(KeyEvent keyevent){
        char c = keyevent.getKeyChar();
        if(calculator.ALLOWED_CHARS.indexOf(c)==-1){
            keyevent.consume();
        }else{
            if(calculator.mode==CALC)
                calculator.eraseOldResult();
            if(c=='='){
                if(calculator.mode==CALC)
                    calculator.doEquals();
            }else
                calculator.display.paste(c);
            if(calculator.mode!=CALC)
                calculator.functionChange();
        }
        keyevent.consume();
    }

}

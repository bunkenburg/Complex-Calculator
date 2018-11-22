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
package cat.inspiracio.calculator;

import javax.swing.*;

/** The text area of the complex calculator. 
 * This class just adds some convenient methods for copy/paste functionality to TextArea. */
final class Display extends JTextArea {
	
	//Constructor ------------------------------------------
	
	/** @param fontSize Font size */
    Display(int fontSize){
        super("", 3, 1);
    }

    //Methods ------------------------------------------------
    
    final void clearAll(){setText("");}

    final void delete(){
        int i = getSelectionStart();
        int j = getSelectionEnd();
        int k = getCaretPosition();
        if(i == j){
            if(k > 0){
                replaceRange("", k - 1, k);
                select(k - 1, k - 1);
                setCaretPosition(k - 1);
                return;
            }
        } else{
            replaceRange("", i, j);
            setSelectionEnd(i);
            setCaretPosition(i);
        }
    }

    final void paste(char c){
        paste(String.valueOf(c));
    }

    final void paste(String s){
        int i = getSelectionStart();
        int j = getSelectionEnd();
        int k = getCaretPosition();
        if(i == j){
            insert(s, k);
            setCaretPosition(k + s.length());
            select(k + s.length(), k + s.length());
        } else{
            replaceRange(s, i, j);
            setCaretPosition(i + s.length());
            select(i + s.length(), i + s.length());
        }
    }

    final void prepend(String s){
        int i = getSelectionStart();
        int j = getSelectionEnd();
        int k = getCaretPosition();
        replaceRange(s, 0, 0);
        setSelectionStart(i + s.length());
        setSelectionEnd(j + s.length());
        setCaretPosition(k + s.length());
    }

    final void replace(char c, char c1){
        int i = getSelectionStart();
        int j = getSelectionEnd();
        int k = getCaretPosition();
        setText(getText().replace(c, c1));
        select(i, j);
        setCaretPosition(k);
    }
}
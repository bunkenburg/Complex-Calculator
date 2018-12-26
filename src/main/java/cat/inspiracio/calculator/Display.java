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
 * */
package cat.inspiracio.calculator;

import javax.swing.*;

/** The text area of the complex calculator. 
 *
 * This class just adds some convenient methods
 * for copy/paste functionality to TextArea.
 *
 * This could really be a lot better. */
class Display extends JTextArea {
	
	//Constructor ------------------------------------------
	
    Display(int fontSize){
        super("", 3, 1);
    }

    //Methods ------------------------------------------------
    
    void clearAll(){setText("");}

    /** delete selected or backspace */
    void delete(){
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int caret = getCaretPosition();
        if(start == end){
            if(0 < caret){
                replaceRange("", caret - 1, caret);
                select(caret - 1, caret - 1);
                setCaretPosition(caret - 1);
            }
        } else{
            replaceRange("", start, end);
            setSelectionEnd(start);
            setCaretPosition(start);
        }
    }

    void paste(char c){
        paste(String.valueOf(c));
    }

    void paste(String s){
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int caret = getCaretPosition();
        if(start == end){
            insert(s, caret);
            setCaretPosition(caret + s.length());
            select(caret + s.length(), caret + s.length());
        } else {
            replaceRange(s, start, end);
            setCaretPosition(start + s.length());
            select(start + s.length(), start + s.length());
        }
    }

    /** Put s in from of displayed string */
    void prepend(String s){
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int caret = getCaretPosition();
        replaceRange(s, 0, 0);
        setSelectionStart(start + s.length());
        setSelectionEnd(end + s.length());
        setCaretPosition(caret + s.length());
    }

    /** Replace one char by another. */
    void replace(char c, char c1){
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int caret = getCaretPosition();
        setText(getText().replace(c, c1));
        select(start, end);
        setCaretPosition(caret);
    }
}
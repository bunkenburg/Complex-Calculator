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

import cat.inspiracio.numbers.BugException;
import cat.inspiracio.numbers.PartialException;
import cat.inspiracio.numbers.Square;
import cat.inspiracio.parsing.SyntaxTree;
import cat.inspiracio.complex.*;

import javax.swing.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

import static cat.inspiracio.calculator.Mode.*;

/** The Calculator application.
 * This is a frame, the main window.
 * This class has the main method. */
public final class Calculator extends JFrame {

    //State -------------------------------------------

    /** The mode that the program is in: Calculation, z->fz mapping, z->|fz| mapping, or Re(fz). */
    private Mode mode = CALC;
    Mode mode(){return mode;}
    
    private Display display;

    private JButton equalsButton;
    private JButton zButton;

    private char variable = 'z';
    char variable(){return variable;}

    private ComplexWorld cW;
    private ZWorld zW;
    private FzWorld fzW;
    private ThreeDWorld modfzW;
    private RefxWorld refxW;

    private SyntaxTree f;
    private boolean inAnApplet = true;

    //Constructor --------------------------------------
    
    /** Instantiate a Complex Calculator. */
    public Calculator(){
        super("Complex Calculator");
        buildFrame();
        buildButtons();
        setJMenuBar(new Menus(this));
        pack();
        becomeVisible();
    }

    private void buildFrame(){
        display = new Display(12);
        setResizable(false);
        setPrecision(4);
    }

    private void setPrecision(int n){ Complex$.MODULE$.setPrecision(n); }

    private void buildButtons(){
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
        display.addKeyListener(new MyKeyListener(this));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 5;
        constraints.gridheight = 3;
        constraints.weightx = 1.0D;
        constraints.weighty = 1.0D;
        constraints.fill = 1;
        layout.setConstraints(display, constraints);
        add(display);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        ActionListener listener = e -> {
                if(mode == CALC)
                    eraseOldResult();
                String command = e.getActionCommand();
                display.paste(command);
                display.requestFocus();
                if(mode != CALC)
                    functionChange();
            };
        Bx abx[] = {
                new Bx("!", 0, 3, 1, listener),
                new Bx("del", 3, 3, 1, e -> {
                        if(mode == CALC)
                            eraseOldResult();
                        display.delete();
                        display.requestFocus();
                        if(mode != CALC)
                            functionChange();
                    }),
                new Bx("C", 4, 3, 1, e -> {
                        display.clearAll();
                        if(mode != CALC)
                            display.prepend("f(" + variable + ") = ");
                        display.requestFocus();
                    }),
                new Bx("sinh", 0, 4, 1, listener),
                new Bx("cosh", 1, 4, 1, listener),
                new Bx("tanh", 2, 4, 1, listener),
                new Bx("conj", 3, 4, 1, listener),
                new Bx("opp", 4, 4, 1, listener),
                new Bx("sin", 0, 5, 1, listener),
                new Bx("cos", 1, 5, 1, listener),
                new Bx("tan", 2, 5, 1, listener),
                new Bx("Re", 3, 5, 1, listener),
                new Bx("Im", 4, 5, 1, listener),
                new Bx("ln", 0, 7, 1, listener),
                new Bx("exp", 1, 7, 1, listener),
                new Bx("^", 2, 7, 1, listener),
                new Bx("mod", 3, 7, 1, listener),
                new Bx("arg", 4, 7, 1, listener),
                new Bx("i", 0, 8, 1, listener),
                new Bx("e", 1, 8, 1, listener),
                new Bx("π", 2, 8, 1, listener),
                new Bx("(", 3, 8, 1, listener),
                new Bx(")", 4, 8, 1, listener),
                new Bx("7", 0, 9, 1, listener),
                new Bx("8", 1, 9, 1, listener),
                new Bx("9", 2, 9, 1, listener),
                new Bx("*", 3, 9, 1, listener),
                new Bx("/", 4, 9, 1, listener),
                new Bx("4", 0, 10, 1, listener),
                new Bx("5", 1, 10, 1, listener),
                new Bx("6", 2, 10, 1, listener),
                new Bx("+", 3, 10, 1, listener),
                new Bx("-", 4, 10, 1, listener),
                new Bx("1", 0, 11, 1, listener),
                new Bx("2", 1, 11, 1, listener),
                new Bx("3", 2, 11, 1, listener),
                new Bx("z", 3, 11, 1, e -> {
                        display.paste(variable);
                        display.requestFocus();
                        if(mode != CALC)
                            functionChange();
                    }),
                new Bx("=", 4, 11, 2, e -> {
                        doEquals();
                        display.requestFocus();
                    }),
                new Bx("0", 0, 12, 1, listener),
                new Bx(".", 1, 12, 1, listener),
                new Bx("∞", 2, 12, 1, listener)
        };
        for(int i = 0; i < abx.length; i++){
            JButton button = new JButton(abx[i].label);
            if(abx[i].label.equals("z"))
                zButton = button;
            else if(abx[i].label.equals("="))
                equalsButton = button;
            button.addActionListener(abx[i].al);
            constraints.gridx = abx[i].gridx;
            constraints.gridy = abx[i].gridy;
            constraints.gridheight = abx[i].gridheight;
            layout.setConstraints(button, constraints);
            add(button);
        }
    }

    private void becomeVisible(){
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent windowevent){quit();}
        });
        setMode(CALC);
        pack();
        setLocation();
        setVisible(true);
        display.requestFocus();
    }

    /** Find a good place on the screen for the new Calculator frame. */
    private void setLocation(){
        setLocationByPlatform(true);
    }

    //Methods ---------------------------------------------------------

    /** Run the Calculator as stand-alone application. */
    public static void main(String args[]){
        Calculator calculator = new Calculator();
        calculator.inAnApplet = false;
    }

    /** Adds a complex number to the display. */
    final void add(Complex c){
        String s = "(" + c + ")";
        display.paste(s);
    }

    /** Gets the expression from the display, parses it, evaluates it,
     * and append the result to the display. */
	void doEquals(){
        String s=display.getText();
        display.append(" = ");
        try{
            SyntaxTree tree = new SyntaxTree().parse(s);
            Complex c = tree.evaluate(null);
            display.append(c.toString());
            cW.add(c);
        }catch(BugException e){
        	e.printStackTrace();
        }catch(PartialException e){
        	e.printStackTrace();
        }catch(ParseException e){
        	e.printStackTrace();
        }
    }

    void eraseOldResult(){
        String s = display.getText();
        int i = s.lastIndexOf('=');
        if(i != -1){
            int j = display.getCaretPosition();
            s = s.substring(0, i);
            int k = s.length();
            display.setText(s);
            j = j >= k ? k : j;
            display.setCaretPosition(j);
        }
    }

    /** Callback: f(z)=... has changed. */
    void functionChange(){
        try{
            String s = SyntaxTree.stripBlanks(display.getText());
            if(s.startsWith("f(" + variable + ")=")){
                f = new SyntaxTree().parse(s.substring(5));
                switch(mode){
                    case MODFZ: modfzW.functionChange(f); break;
                    case REFX: refxW.functionChange(f); break;
                    case FZ: fzW.functionChange(f); break;
                }
            }
        }
        catch(ParseException _ex) { }
    }

    Square getSquare(){ return zW.getSquare(); }

    public void quit(){
        if(inAnApplet){
            dispose();
            if(cW != null) cW.dispose();
            if(zW != null) zW.dispose();
            if(fzW != null) fzW.dispose();
            if(modfzW != null) modfzW.dispose();
            if(refxW != null) refxW.dispose();
        } else
            System.exit(0);
    }

    private void setArgPrincipal(){
        Complex$.MODULE$.setArgPrincipal();
    }

    private void setArgContinuous(){
        Complex$.MODULE$.setArgContinuous();
    }

    /** Sets the mode, opening and closing windows. */
    void setMode(Mode m){
        switch(m){
        
        case CALC:
            setArgPrincipal();
            display.clearAll();
            equalsButton.setEnabled(true);
            zButton.setEnabled(false);
            complexWorld();
            disposeZWorld();
            disposeFZWorld();
            disposeModFZWorld();
            disposeReFXWorld();
            break;

        case FZ:
            setArgContinuous();
            variable = 'z';
            if(mode == REFX)
                display.replace('x', 'z');
            else if(mode ==CALC){
                display.clearAll();
                display.prepend("f(z) = ");
            }
            equalsButton.setEnabled(false);
            zButton.setEnabled(true);
            zButton.setText("z");
            disposeComplexWorld();
            zWorld();
            zW.setMode(FZ);
            fzWorld();
            fzW.setzWorld(zW);
            zW.setfzWorld(fzW);
            disposeModFZWorld();
            disposeReFXWorld();
            break;

        case MODFZ:
            setArgContinuous();
            variable = 'z';
            if(mode ==REFX)
                display.replace('x', 'z');
            else if(mode ==CALC){
                display.clearAll();
                display.prepend("f(z) = ");
            }
            equalsButton.setEnabled(false);
            zButton.setEnabled(true);
            zButton.setText("z");
            disposeComplexWorld();
            zWorld();
            zW.setMode(MODFZ);
            disposeFZWorld();
            modFZWorld();
            zW.setmodfzWorld(modfzW);
            disposeReFXWorld();
            break;

        case REFX:
            setArgContinuous();
            variable = 'x';
            if(mode == CALC){
                display.clearAll();
                display.prepend("f(x) = ");
            } else{
                display.replace('z', 'x');
            }
            equalsButton.setEnabled(false);
            zButton.setEnabled(true);
            zButton.setText("x");
            disposeComplexWorld();
            disposeZWorld();
            disposeFZWorld();
            disposeModFZWorld();
            refxWorld();
            break;
        }
        mode = m;
        functionChange();
    }

    private void complexWorld(){
        if(cW == null)
            cW = new ComplexWorld(this);
    }

    private void disposeComplexWorld(){
        if(cW != null){
            cW.dispose();
            cW = null;
        }
    }

    private void zWorld(){
        if(zW == null)
            zW = new ZWorld(this);
    }

    private void disposeZWorld() {
        if (zW != null) {
            zW.dispose();
            zW = null;
        }
    }

    private void fzWorld(){
        if(fzW == null)
            fzW = new FzWorld(this);
    }

    private void disposeFZWorld(){
        if(fzW != null){
            fzW.dispose();
            fzW = null;
        }
    }

    private void modFZWorld(){
        if(modfzW == null)
            modfzW = new ThreeDWorld(this);
    }

    private void disposeModFZWorld(){
        if(modfzW != null){
            modfzW.dispose();
            modfzW = null;
        }
    }

    private void refxWorld(){
        if(refxW == null)
            refxW = new RefxWorld(this);
    }

    private void disposeReFXWorld(){
        if(refxW != null){
            refxW.dispose();
            refxW = null;
        }
    }

    // display -------------------------

    void delete(){display.delete();}
    void clearAll(){display.clearAll();}
    void prepend(String s){display.prepend(s);}
    void paste(char c){display.paste(c);}
}
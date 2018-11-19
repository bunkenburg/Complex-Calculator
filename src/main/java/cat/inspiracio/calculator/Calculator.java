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

import static cat.inspiracio.calculator.Calculator.Mode.CALC;
import static cat.inspiracio.calculator.Calculator.Mode.FZ;
import static cat.inspiracio.calculator.Calculator.Mode.MODFZ;
import static cat.inspiracio.calculator.Calculator.Mode.REFX;
import cat.inspiracio.numbers.BugException;
import cat.inspiracio.numbers.EC;
import cat.inspiracio.numbers.PartialException;
import cat.inspiracio.numbers.Square;
import cat.inspiracio.parsing.SyntaxTree;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

/** The Calculator application.
 * This is a frame, the main window.
 * This class has the main method. */
public final class Calculator extends JFrame {
	
    public static enum Mode{CALC, FZ, MODFZ, REFX}

    private static final String PI="\u03C0";
	private static final String INF="\u221E";
    
    static final int precisions[] = {2, 4, 6, 8, 10};
    private final static String ALLOWED_CHARS = " !sinhcoshtanhconjoppReImlnexp^modargiepi()789*/456+-123=0.z";

    //State -------------------------------------------

    /** The mode that the program is in: Calculation, z->fz mapping, z->|fz| mapping, or Re(fz). */
    private Mode mode = CALC;
    
    private CalculatorDisplay display;
    private JButton equalsButton;
    private JButton zButton;
    private char variable = 'z';
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
        buildMenuBar();
        becomeVisible();
    }

    private void buildFrame(){
        display = new CalculatorDisplay(12);
        setBackground(Color.lightGray);
        setResizable(false);
        EC.setPrecision(4);
        setFont(new Font("SansSerif", 0, 12));
    }

    private void buildMenuBar(){
        JMenuBar menubar = new Menus(this);
        setFont(getFont(), menubar);
        setJMenuBar(menubar);
    }

    private void buildButtons(){
        GridBagLayout gridbaglayout = new GridBagLayout();
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        setLayout(gridbaglayout);
        display.addKeyListener(new MyKeyListener());
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        gridbagconstraints.gridwidth = 5;
        gridbagconstraints.gridheight = 3;
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.weighty = 1.0D;
        gridbagconstraints.fill = 1;
        gridbaglayout.setConstraints(display, gridbagconstraints);
        add(display);
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.gridheight = 1;
        ActionListener actionlistener = new ActionListener() {
            @Override public void actionPerformed(ActionEvent actionevent){
                if(mode ==CALC)
                    eraseOldResult();
                display.paste(actionevent.getActionCommand());
                display.requestFocus();
                if(mode !=CALC)
                    functionChange();
            }
        };
        Bx abx[] = {
                new Bx("!", 0, 3, 1, actionlistener),
                new Bx("del", 3, 3, 1, new ActionListener() {
                    @Override public void actionPerformed(ActionEvent actionevent){
                        if(mode ==CALC)
                            eraseOldResult();
                        display.delete();
                        display.requestFocus();
                        if(mode !=CALC)
                            functionChange();
                    }
                }),
                new Bx("C", 4, 3, 1, new ActionListener() {
                    @Override public void actionPerformed(ActionEvent actionevent){
                        display.clearAll();
                        if(mode !=CALC)
                            display.prepend("f(" + variable + ") = ");
                        display.requestFocus();
                    }
                }),
                new Bx("sinh", 0, 4, 1, actionlistener),
                new Bx("cosh", 1, 4, 1, actionlistener),
                new Bx("tanh", 2, 4, 1, actionlistener),
                new Bx("conj", 3, 4, 1, actionlistener),
                new Bx("opp", 4, 4, 1, actionlistener),
                new Bx("sin", 0, 5, 1, actionlistener),
                new Bx("cos", 1, 5, 1, actionlistener),
                new Bx("tan", 2, 5, 1, actionlistener),
                new Bx("Re", 3, 5, 1, actionlistener),
                new Bx("Im", 4, 5, 1, actionlistener),
                new Bx("ln", 0, 7, 1, actionlistener),
                new Bx("exp", 1, 7, 1, actionlistener),
                new Bx("^", 2, 7, 1, actionlistener),
                new Bx("mod", 3, 7, 1, actionlistener),
                new Bx("arg", 4, 7, 1, actionlistener),
                new Bx("i", 0, 8, 1, actionlistener),
                new Bx("e", 1, 8, 1, actionlistener),
                new Bx(PI, 2, 8, 1, actionlistener),
                new Bx("(", 3, 8, 1, actionlistener),
                new Bx(")", 4, 8, 1, actionlistener),
                new Bx("7", 0, 9, 1, actionlistener),
                new Bx("8", 1, 9, 1, actionlistener),
                new Bx("9", 2, 9, 1, actionlistener),
                new Bx("*", 3, 9, 1, actionlistener),
                new Bx("/", 4, 9, 1, actionlistener),
                new Bx("4", 0, 10, 1, actionlistener),
                new Bx("5", 1, 10, 1, actionlistener),
                new Bx("6", 2, 10, 1, actionlistener),
                new Bx("+", 3, 10, 1, actionlistener),
                new Bx("-", 4, 10, 1, actionlistener),
                new Bx("1", 0, 11, 1, actionlistener),
                new Bx("2", 1, 11, 1, actionlistener),
                new Bx("3", 2, 11, 1, actionlistener),
                new Bx("z", 3, 11, 1, new ActionListener() {
                    @Override public void actionPerformed(ActionEvent actionevent){
                        display.paste(variable);
                        display.requestFocus();
                        if(mode !=CALC)
                            functionChange();
                    }
                }),
                new Bx("=", 4, 11, 2, new ActionListener() {
                    @Override public void actionPerformed(ActionEvent actionevent){
                        doEquals();
                        display.requestFocus();
                    }
                }),
                new Bx("0", 0, 12, 1, actionlistener),
                new Bx(".", 1, 12, 1, actionlistener),
                new Bx(INF, 2, 12, 1, actionlistener)
        };
        for(int i = 0; i < abx.length; i++){
            JButton button = new JButton(abx[i].label);
            if(abx[i].label.equals("z"))
                zButton = button;
            else if(abx[i].label.equals("="))
                equalsButton = button;
            button.addActionListener(abx[i].al);
            gridbagconstraints.gridx = abx[i].gridx;
            gridbagconstraints.gridy = abx[i].gridy;
            gridbagconstraints.gridheight = abx[i].gridheight;
            gridbaglayout.setConstraints(button, gridbagconstraints);
            add(button);
        }
    }

    private void becomeVisible(){
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent windowevent){quit();}
        });
        setMode(CALC);
        pack();
        setLocationByPlatform(true);
        setVisible(true);
        display.requestFocus();
    }

    //Methods ---------------------------------------------------------

    /** Run the Calculator as stand-alone application. */
    public static void main(String args[]){
        Calculator calculator = new Calculator();
        calculator.inAnApplet = false;
    }

    /** Adds a complex number to the display. */
    final void add(EC ec){display.paste("(" + ec + ")");}

    /** Gets the expression from the display, parses it, evaluates it,
     * and append the result to the display. */
	private void doEquals(){
        String s=display.getText();
        display.append(" = ");
        try{
            SyntaxTree tree=SyntaxTree.parse(s);
            EC ec=tree.evaluate(null);
            display.append(ec.toString());
            cW.add(ec);
            return;
        }catch(BugException bugexception){
        	bugexception.printStackTrace();
            return;
        }catch(PartialException partialexception){
        	partialexception.printStackTrace();
            return;
        }catch(ParseException parseexception){
        	parseexception.printStackTrace();
        }
    }

    private void eraseOldResult(){
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

    private void functionChange(){
        try{
            String s = SyntaxTree.stripBlanks(display.getText());
            if(s.startsWith("f(" + variable + ")=")){
                f = SyntaxTree.parse(s.substring(5));
                if(mode ==MODFZ){
                    modfzW.functionChange(f);
                    return;
                }
                if(mode ==REFX){
                    refxW.functionChange(f);
                    return;
                }
                if(mode ==FZ){
                    fzW.functionChange(f);
                    return;
                }
            }
        }
        catch(ParseException _ex) { }
    }

    Square getSquare(){
        return zW.getSquare();
    }

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

    public void setFontSize(int i){
        Font font = new Font("SansSerif", 0, i);
        display.setFont(font);
        if(cW != null)cW.setFont(font);
        if(zW != null)zW.setFont(font);
        if(fzW != null)fzW.setFont(font);
        if(modfzW != null)modfzW.setFont(font);
        if(refxW != null)refxW.setFont(font);
        setFont(font);
        getMenuBar().setFont(font);
        doLayout();
        pack();
    }

    private static void setFont(Font font, JMenu menu){
        menu.setFont(font);
        for(int i = 0; i < menu.getItemCount(); i++)
            setFont(font, menu.getItem(i));
    }

    private static void setFont(Font font, JMenuBar menubar){
        menubar.setFont(font);
        for(int i = 0; i < menubar.getMenuCount(); i++)
            setFont(font, menubar.getMenu(i));
    }

    private static void setFont(Font font, JMenuItem menuitem){
        if(menuitem instanceof JMenu)
            setFont(font, (JMenu)menuitem);
        else
            menuitem.setFont(font);
    }

    /** Sets the mode, opening and closing windows. */
    void setMode(Mode i){
        switch(i){
        
        case CALC:
            EC.setArgPrincipal();
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
            EC.setArgContinuous();
            variable = 'z';
            if(mode==REFX)
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
            EC.setArgContinuous();
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
        	EC.setArgContinuous();
            variable = 'x';
            if(mode ==CALC){
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
        mode = i;
        functionChange();
        firePropertyChange("mode", this, mode);//Fire event after the change
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

    //private inner classes -----------------------------------------------------------
    
    private final class MyKeyListener extends KeyAdapter{
        MyKeyListener(){}

        @Override public void keyPressed(KeyEvent keyevent){
            int i = keyevent.getKeyCode();
            if(i==8 || i==127){
                display.delete();
                keyevent.consume();
                return;
            }
            if(i==10){
                if(mode==CALC){
                    eraseOldResult();
                    doEquals();
                }
                keyevent.consume();
                return;
            }
            if(i==12){
                display.clearAll();
                if(mode!=CALC)
                    display.prepend("f(" + variable + ") = ");
                keyevent.consume();
                return;
            }
            if(i!=37 && i!=39)
                keyevent.consume();
        }

        @Override public void keyTyped(KeyEvent keyevent){
            char c = keyevent.getKeyChar();
            if(ALLOWED_CHARS.indexOf(c)==-1){
                keyevent.consume();
            }else{
                if(mode==CALC)
                    eraseOldResult();
                if(c=='='){
                    if(mode==CALC)
                        doEquals();
                }else
                    display.paste(c);
                if(mode!=CALC)
                    functionChange();
            }
            keyevent.consume();
        }

    }//MyKeyListener

    private final class Bx{
        String label;
        int gridx;
        int gridy;
        int gridheight;
        ActionListener al;
        Bx(String s, int i, int j, int k, ActionListener actionlistener){
            label = s;
            gridx = i;
            gridy = j;
            gridheight = k;
            al = actionlistener;
        }
    }

}
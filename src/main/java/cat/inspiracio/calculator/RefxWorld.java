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

import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.package$;
import cat.inspiracio.parsing.SyntaxTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing

final class RefxWorld extends JFrame{

    private static final Dimension MIN_SIZE = new Dimension(400, 300);
    private static int AXISSPACE = 30;
    private static int AXISMARKING = 40;
    private static int FONTHEIGHT = 10;
    private static int TRIANGLESIZE = 5;
    private static int MARKLENGTH = 2;

    private Calculator calculator;
    protected JPanel buttonPanel;
    private RefxCanvas canvas;

    protected int prevx;
    protected int prevy;
    private double Max;
    private double Min;
    private SyntaxTree f;

    //Constructor ----------------------------------------------------------------
    
    RefxWorld(Calculator c){
        canvas = new RefxCanvas();
        calculator = c;
        setTitle("Re(f(x)) World");
        resetExtremes();

        JButton button = new JButton("Zoom In");
        button.addActionListener(e -> zoomIn() );
        JButton button1 = new JButton("Zoom Out");
        button1.addActionListener( e -> zoomOut() );
        JButton button2 = new JButton("Reset");
        button2.addActionListener(e -> reset() );

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setLayout(new FlowLayout(0));
        buttonPanel.add(button);
        buttonPanel.add(button1);
        buttonPanel.add(button2);

        setLayout(new BorderLayout());
        add("North", buttonPanel);
        add("Center", canvas);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent){calculator.quit();}
        });

        pack();
        setLocationRelativeTo(calculator);
        setLocationByPlatform(true);
        setLocation();
        setVisible(true);
    }

    // event listeners -------------------

    private void reset(){
        canvas.reset();
        canvas.repaint();
    }

    private void zoomOut(){
        canvas.zoomOut();
        canvas.repaint();
    }

    private void zoomIn(){
        canvas.zoomIn();
        canvas.repaint();
    }

    private void setLocation(){
        setLocationRelativeTo(calculator);
        setLocationByPlatform(true);
    }

    //Methods ---------------------------------------------------------
    
    void functionChange(SyntaxTree syntaxtree){
        f = syntaxtree;
        canvas.repaint();
    }

    private static double raiseSmooth(double d){
        int i;
        for(i = 0; d < 1.0D; i--)
            d *= 10D;
        while(d >= 10D){
            d /= 10D;
            i++;
        }
        if(d > 5D)
            d = 10D;
        else if(d > 2.5D)
            d = 5D;
        else if(d > 2D)
            d = 2.5D;
        else if(d > 1.0D)
            d = 2D;
        for(; i < 0; i++)
            d /= 10D;
        for(; i > 0; i--)
            d *= 10D;
        return d;
    }

    protected void resetExtremes(){
        Min = (1.0D / 0.0D);
        Max = (-1.0D / 0.0D);
    }

    @Override public void update(Graphics g){paint(g);}

    protected void updateExtremes(double d){
        Max = Math.max(Max, d);
        Min = Math.min(Min, d);
    }

    public void setFont(Font font){
        super.setFont(font);
        canvas.setFont(font);
    }

    //Inner class --------------------------------------------------------
    
    private class RefxCanvas extends JComponent{

    	//State ----------------------------------------------------

        private DoubleBuffer doubleBuffer;
        private double ScaleFactor;
        private double CenterX;
        private double CenterY;
        private double Top;
        private double Left;
        private double Bottom;
        private double Right;
        private Polygon upTriangle;
        private Polygon downTriangle;

        //Constructor ----------------------------------------------

        RefxCanvas(){
        	ScaleFactor = 40D;
        	setBackground(Color.white);
        	doubleBuffer = new DoubleBuffer(this);
        	addMouseListener(new MouseAdapter() {
        		public void mousePressed(MouseEvent mouseevent){
        			prevx = mouseevent.getX();
        			prevy = mouseevent.getY();
        			mouseevent.consume();
        		}
        		public void mouseReleased(MouseEvent mouseevent){
        			int i = mouseevent.getX();
        			int j = mouseevent.getY();
        			shift(prevx - i, prevy - j);
        			paint(getGraphics());
        			prevx = i;
        			prevy = j;
        			mouseevent.consume();
        		}
        	});
        	addMouseMotionListener(new MouseMotionAdapter() {
        		public void mouseDragged(MouseEvent mouseevent){
        			int i = mouseevent.getX();
        			int j = mouseevent.getY();
        			shift(prevx - i, prevy - j);
        			paint(getGraphics());
        			prevx = i;
        			prevy = j;
        			mouseevent.consume();
        		}
        	});
        }//RefxCanvas()

        //Methods ------------------------------------------------------

        private void xy2Point(double x, double y, Point point){
            point.x = (int)((x - Left) * ScaleFactor);
            point.y = -(int)((y - Top) * ScaleFactor);
        }

        private void drawIt(Drawing drawing){
            resetExtremes();
            boolean flag = false;
            int i = 0;
            int k = getSize().height;
            for(int l = getSize().width; i < l; i++)
                try{
                    Complex z = Real(pix2x(i));
                    Complex ec = f.evaluate(z);
                    double d;
                    if( finite(ec) )
                        d = Re(ec);
                    else
                        throw new Exception();
                    updateExtremes(d);
                    int j;
                    if(d < Bottom)
                        j = k;
                    else if(Top < d)
                        j = -1;
                    else
                        j = (int)((Top - d) * ScaleFactor);
                    if(flag){
                        drawing.lineTo(i, j);
                    } else{
                        drawing.moveTo(i, j);
                        flag = true;
                    }
                }
                catch(Exception _ex){
                    flag = false;
                }
        }

        @Override public Dimension getPreferredSize(){return getMinimumSize();}
        @Override public Dimension getMinimumSize(){return RefxWorld.MIN_SIZE;}

        @Override public void paint(Graphics g){
            g = doubleBuffer.offScreen(g);
            Drawing drawing = new Drawing(g);
            Top = CenterY + Pix2Math(getSize().height / 2);
            Bottom = CenterY - Pix2Math(getSize().height / 2);
            Left = CenterX - Pix2Math(getSize().width / 2);
            Right = CenterX + Pix2Math(getSize().width / 2);
            Point point = new Point();
            Point point1 = new Point();
            Point point2 = new Point();
            double d = RefxWorld.raiseSmooth(Pix2Math(RefxWorld.AXISMARKING));
            int l = Math2Pix(d);
            double d1 = 0.0D;
            double d2 = 0.0D;
            double d3 = Left + Pix2Math(RefxWorld.AXISSPACE);
            double d4 = Right - Pix2Math(RefxWorld.AXISSPACE);
            double d5 = Bottom + Pix2Math(RefxWorld.AXISSPACE);
            double d6 = Top - Pix2Math(RefxWorld.AXISSPACE);
            if(d3 <= 0.0D && d4 >= 0.0D)
                d1 = 0.0D;
            else if(d3 > 0.0D)
                d1 = d3;
            else if(d4 < 0.0D)
                d1 = d4;
            if(d5 <= 0.0D && d6 >= 0.0D)
                d2 = 0.0D;
            else if(d5 > 0.0D)
                d2 = d5;
            else if(d6 < 0.0D)
                d2 = d6;
            xy2Point(d1, d2, point2);
            xy2Point(d3, d2, point);
            xy2Point(d4, d2, point1);
            drawing.drawLine(point, point1, Color.lightGray);
            Polygon polygon = drawing.mkTriangle(point1, Direction.EAST, RefxWorld.TRIANGLESIZE);
            g.drawPolygon(polygon);
            polygon = drawing.mkTriangle(point, Direction.WEST, RefxWorld.TRIANGLESIZE);
            g.drawPolygon(polygon);
            int j = point2.y;
            double d7 = Math.ceil(d3 / d);
            d3 = d7 * d;
            int i = x2Pix(d3);
            for(; d3 < d4; d3 += d){
                drawing.moveTo(i, j);
                drawing.line(0, RefxWorld.MARKLENGTH);
                g.drawString(toString(d3), i + RefxWorld.MARKLENGTH, j + RefxWorld.FONTHEIGHT);
                i += l;
            }
            xy2Point(d1, d5, point);
            xy2Point(d1, d6, point1);
            drawing.drawLine(point, point1, Color.lightGray);
            upTriangle = drawing.mkTriangle(point1, Direction.NORTH, RefxWorld.TRIANGLESIZE);
            g.drawPolygon(upTriangle);
            downTriangle = drawing.mkTriangle(point, Direction.SOUTH, RefxWorld.TRIANGLESIZE);
            g.drawPolygon(downTriangle);
            i = point2.x;
            d7 = Math.ceil(d5 / d);
            d5 = d7 * d;
            for(int k = y2Pix(d5); d5 < d6; k -= l){
                if(d5 != 0.0D || d1 != 0.0D){
                    String s = toString(d5);
                    drawing.moveTo(i, k);
                    drawing.line(-RefxWorld.MARKLENGTH, 0);
                    g.drawString(s, i - RefxWorld.MARKLENGTH - g.getFontMetrics().stringWidth(s), k + RefxWorld.FONTHEIGHT);
                }
                d5 += d;
            }

            if(f != null)
                drawIt(drawing);
            if(Top <= Max)
                g.fillPolygon(upTriangle);
            if(Min <= Bottom)
                g.fillPolygon(downTriangle);
            doubleBuffer.onScreen();
        }

        private double Pix2Math(int i){
            return (double)i / ScaleFactor;
        }

        private int Math2Pix(double d){
            return (int)(d * ScaleFactor);
        }

        private int x2Pix(double x){
            return (int)((x - Left) * ScaleFactor);
        }

        private int y2Pix(double y){
            return -(int)((y - Top) * ScaleFactor);
        }

        private double pix2x(int i){
            return (double)i / ScaleFactor + Left;
        }

        private void reset(){
            CenterX = 0.0D;
            CenterY = 0.0D;
        }

        public void setFont(Font font){
            super.setFont(font);
            RefxWorld.FONTHEIGHT = getFontMetrics(font).getAscent();
            RefxWorld.AXISSPACE = 3 * RefxWorld.FONTHEIGHT;
            RefxWorld.AXISMARKING = 4 * RefxWorld.FONTHEIGHT;
            RefxWorld.TRIANGLESIZE = RefxWorld.FONTHEIGHT / 2;
            RefxWorld.MARKLENGTH = RefxWorld.FONTHEIGHT / 5;
        }

        void shift(int i, int j){
            CenterY -= Pix2Math(j);
            CenterX += Pix2Math(i);
        }

        private void zoomIn(){ScaleFactor *= 2D;}
        private void zoomOut(){ScaleFactor /= 2D;}

        // helpers ---------------------------------------

        private String toString(double d){
            return package$.MODULE$.double2Complex(d).toString();
        }

        protected Complex Cartesian(double re, double im){
            Complex i = package$.MODULE$.i();
            return i.$times(im).$plus(re);
        }

        protected Complex Real(double re){
            return package$.MODULE$.double2Complex(re);
        }

    }//class RefxCanvas

    // helpers -----------------------------------

    protected boolean finite(Complex z){return package$.MODULE$.finite(z);}
    protected double Im(Complex c){ return cat.inspiracio.complex.package$.MODULE$.Im(c); }
    protected double Re(Complex c){ return cat.inspiracio.complex.package$.MODULE$.Re(c); }
    private String toString(double d){ return package$.MODULE$.double2Complex(d).toString(); }

}
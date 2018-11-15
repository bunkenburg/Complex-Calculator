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

import cat.inspiracio.numbers.Circle;
import cat.inspiracio.numbers.EC;
import cat.inspiracio.numbers.Rectangle;
import cat.inspiracio.numbers.Square;
import cat.inspiracio.parsing.SyntaxTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing, Matrix44, 
//            Vector2, Vector3

final class ThreeDWorld extends JFrame {

	//State ---------------------------------------------

    private static final Dimension MIN_SIZE = new Dimension(400, 300);
    static final double XYFACTOR = 0.6D;
    private ThreeDCanvas canvas;
    //private static final double EPSILON = 0.0001D;
    //private static final double undefinedModulus = -0.20000000000000001D;
    //private static final double infiniteModulus = 1.2D;
    private Square square;
    private SyntaxTree f;
    double M[][];
    static final int n = 10;
    private Calculator calculator;

	//Constructor ---------------------------------------

	/** Makes a new 3d world. 
	 * @param calculator1 Connected to this calculator. */
    ThreeDWorld(Calculator calculator1){
        super("|f(z)| World");
        square = new Square(new Circle(EC.ZERO, 1.0D));
        M = new double[21][];
        for(int i = 0; i < 21; i++)
            M[i] = new double[21];
        calculator = calculator1;
        square = calculator1.getSquare();
        canvas = new ThreeDCanvas();
        setLayout(new BorderLayout());
        add("Center", canvas);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent){calculator.quit();}
        });
        JMenuBar mb=calculator.makeMenuBar();
        setJMenuBar(mb);
        pack();
        setLocationRelativeTo(calculator);
        setLocationByPlatform(true);
        setVisible(true);
    }
    
    //Methods -----------------------------------------

    /** Close and remove the window. */
    @Override public void dispose(){
    	MMenuBar mb=(MMenuBar)this.getJMenuBar();
    	mb.dispose();
    	super.dispose();
    }

    void functionChange(SyntaxTree syntaxtree){
        f = syntaxtree;
        setNeighbourhood();
        canvas.repaint();
    }

    void setNeighbourhood(){
        if(f == null){
            for(int i1 = -10; i1 <= 10; i1++){
                for(int i = -10; i <= 10; i++)
                    M[10 + i1][10 + i] = 0.0D;
            }
            return;
        }
        double d1 = square.getSide() / 2D / 10D;
        EC ec1 = square.getCenter();
        for(int j1 = -10; j1 <= 10; j1++){
            for(int j = -10; j <= 10; j++){
                EC ec = EC.mkCartesian(d1 * (double)j1, d1 * (double)j);
                try{
                    M[10 + j1][10 + j] = f.evaluate(ec1.add(ec)).mod();
                }
                catch(Exception _ex){
                    M[10 + j1][10 + j] = -0.20000000000000001D;
                }
            }
        }
        double d = 0.0D;
        for(int k1 = -10; k1 <= 10; k1++){
            for(int k = -10; k <= 10; k++)
                if(!Double.isInfinite(M[10 + k1][10 + k]))
                    d = Math.max(d, M[10 + k1][10 + k]);
        }
        if(d < 0.0001D)
            d = 1.0D;
        for(int l1 = -10; l1 <= 10; l1++){
            for(int l = -10; l <= 10; l++)
                if(Double.isInfinite(M[10 + l1][10 + l]))
                    M[10 + l1][10 + l] = 1.2D;
                else if(M[10 + l1][10 + l] < 0.0D)
                    M[10 + l1][10 + l] = -0.20000000000000001D;
                else
                    M[10 + l1][10 + l] = M[10 + l1][10 + l] / d;
        }
    }

    void squareChange(Square square1){
        square = square1;
        setNeighbourhood();
        canvas.paint(canvas.getGraphics());
    }

    public void setFont(Font font){
        super.setFont(font);
        canvas.setFont(font);
    }

    public void update(Graphics g){paint(g);}

    //Inner class ThreeDCanvas ------------------------------------------------------------
    
	private class ThreeDCanvas extends JComponent{
    
		//State ----------------------------------------------------------
		
        private final DoubleBuffer doubleBuffer = new DoubleBuffer(this);
        private int FONT_HEIGHT;
        private int prevx;
        private int prevy;
        Vector2 v[][];
        Vector3 eye;
        Vector3 direct;
        int nxpix;
        int nypix;
        private Matrix44 Q;
        boolean xforward;
        boolean zforward;
        double xyscale;
        Vector2 v5;
        private Polygon quad;
        private Polygon tri;

        //Constructor ---------------------------------------------------
        
        ThreeDCanvas(){
            FONT_HEIGHT = 12;
            v = new Vector2[2][21];
            v[0] = new Vector2[21];
            v[1] = new Vector2[21];
            for(int i = 0; i < 21; i++){
                v[0][i] = new Vector2();
                v[1][i] = new Vector2();
            }
            eye = new Vector3(3D, 0.5D, 1.0D);
            direct = new Vector3(2.5D, 0.5D, 0.5D);
            Q = new Matrix44();
            v5 = new Vector2();
            quad = new Polygon(new int[4], new int[4], 4);
            tri = new Polygon(new int[3], new int[3], 3);
            setBackground(Color.white);
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent mouseevent){
                    prevx = mouseevent.getX();
                    prevy = mouseevent.getY();
                    mouseevent.consume();
                }
                public void mouseReleased(MouseEvent mouseevent){
                    int j = mouseevent.getX();
                    int k = mouseevent.getY();
                    shift(prevx - j, prevy - k);
                    paint(getGraphics());
                    prevx = j;
                    prevy = k;
                    mouseevent.consume();
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent mouseevent){
                    int j = mouseevent.getX();
                    int k = mouseevent.getY();
                    shift(prevx - j, prevy - k);
                    paint(getGraphics());
                    prevx = j;
                    prevy = k;
                    mouseevent.consume();
                }
            });
            initQ();
        }

        //Methods -------------------------------------------------------
        
        void drawBackAxes(Drawing drawing, boolean flag, boolean flag1){
            String s = ((Rectangle) (square)).botLeft.toString();
            moveTo3(drawing, -0.5D, 0.0D, -0.5D);
            if(flag && flag1)
                drawing.move(2, -2);
            else if(flag && !flag1)
                drawing.move(-drawing.g.getFontMetrics().stringWidth(s) - 2, FONT_HEIGHT);
            else if(!flag && flag1)
                drawing.move(2, FONT_HEIGHT);
            else if(!flag && !flag1)
                drawing.move(2, FONT_HEIGHT);
            drawing.drawString(s);
            moveTo3(drawing, 0.5D, 0.0D, -0.5D);
            s = ((Rectangle) (square)).botRight.toString();
            if(flag && flag1)
                drawing.move(-drawing.g.getFontMetrics().stringWidth(s) - 2, FONT_HEIGHT);
            else if(flag && !flag1)
                drawing.move(2, FONT_HEIGHT);
            else if(!flag && flag1)
                drawing.move(2, -2);
            else if(!flag && !flag1)
                drawing.move(2, FONT_HEIGHT);
            drawing.drawString(((Rectangle) (square)).botRight.toString());
            moveTo3(drawing, -0.5D, 0.0D, 0.5D);
            s = ((Rectangle) (square)).topLeft.toString();
            if(flag && flag1)
                drawing.move(2, FONT_HEIGHT);
            else if(flag && !flag1)
                drawing.move(2, -2);
            else if(!flag && flag1)
                drawing.move(2, FONT_HEIGHT);
            else if(!flag && !flag1)
                drawing.move(-drawing.g.getFontMetrics().stringWidth(s) - 2, FONT_HEIGHT);
            drawing.drawString(s);
            moveTo3(drawing, 0.5D, 0.0D, 0.5D);
            s = ((Rectangle) (square)).topRight.toString();
            if(flag && flag1)
                drawing.move(2, FONT_HEIGHT);
            else if(flag && !flag1)
                drawing.move(2, FONT_HEIGHT);
            else if(!flag && flag1)
                drawing.move(-drawing.g.getFontMetrics().stringWidth(s) - 2, FONT_HEIGHT);
            else if(!flag && !flag1)
                drawing.move(2, -2);
            drawing.drawString(s);
            if(flag)
                drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 0.0D, 0.5D);
            else
                drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 0.0D, 0.5D);
            if(flag1)
                drawLine3(drawing, -0.5D, 0.0D, -0.5D, 0.5D, 0.0D, -0.5D);
            else
                drawLine3(drawing, -0.5D, 0.0D, 0.5D, 0.5D, 0.0D, 0.5D);
            if(!flag || !flag1)
                drawLine3(drawing, 0.5D, 0.0D, 0.5D, 0.5D, 1.0D, 0.5D);
            if(!flag || flag1)
                drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 1.0D, -0.5D);
            if(flag || !flag1)
                drawLine3(drawing, -0.5D, 0.0D, 0.5D, -0.5D, 1.0D, 0.5D);
            if(flag || flag1)
                drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 1.0D, -0.5D);
            if(flag)
                drawLine3(drawing, -0.5D, 1.0D, -0.5D, -0.5D, 1.0D, 0.5D);
            else
                drawLine3(drawing, 0.5D, 1.0D, -0.5D, 0.5D, 1.0D, 0.5D);
            if(flag1){
                drawLine3(drawing, -0.5D, 1.0D, -0.5D, 0.5D, 1.0D, -0.5D);
                return;
            } else{
                drawLine3(drawing, -0.5D, 1.0D, 0.5D, 0.5D, 1.0D, 0.5D);
                return;
            }
        }

        void drawFrontAxes(Drawing drawing, boolean flag, boolean flag1){
            if(flag)
                drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 0.0D, 0.5D);
            else
                drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 0.0D, 0.5D);
            if(flag1)
                drawLine3(drawing, -0.5D, 0.0D, 0.5D, 0.5D, 0.0D, 0.5D);
            else
                drawLine3(drawing, -0.5D, 0.0D, -0.5D, 0.5D, 0.0D, -0.5D);
            if(flag && flag1)
                drawLine3(drawing, 0.5D, 0.0D, 0.5D, 0.5D, 1.0D, 0.5D);
            else if(flag && !flag1)
                drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 1.0D, -0.5D);
            else if(!flag && flag1)
                drawLine3(drawing, -0.5D, 0.0D, 0.5D, -0.5D, 1.0D, 0.5D);
            else if(!flag && !flag1)
                drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 1.0D, -0.5D);
            if(flag)
                drawLine3(drawing, 0.5D, 1.0D, -0.5D, 0.5D, 1.0D, 0.5D);
            else
                drawLine3(drawing, -0.5D, 1.0D, -0.5D, -0.5D, 1.0D, 0.5D);
            if(flag1){
                drawLine3(drawing, -0.5D, 1.0D, 0.5D, 0.5D, 1.0D, 0.5D);
                return;
            } else{
                drawLine3(drawing, -0.5D, 1.0D, -0.5D, 0.5D, 1.0D, -0.5D);
                return;
            }
        }

        void drawImaginaryAxis(Drawing drawing){
            drawLine3(drawing, 0.0D, 0.0D, -0.5D, 0.0D, 0.0D, 0.5D);
        }

        void drawModAxis(Drawing drawing){
            drawLine3(drawing, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D);
        }

        void drawRealAxis(Drawing drawing){
            drawLine3(drawing, -0.5D, 0.0D, 0.0D, 0.5D, 0.0D, 0.0D);
        }

        void drawLine3(Drawing drawing, double d, double d1, double d2, double d3, double d4, double d5){
            moveTo3(drawing, d, d1, d2);
            lineTo3(drawing, d3, d4, d5);
        }

        int cx(int i){
            if(xforward)
                return i;
            else
                return -i;
        }

        int cz(int i){
            if(zforward)
                return i;
            else
                return -i;
        }

        void drawIt(Drawing drawing){
            Vector2 vector2 = new Vector2();
            Vector2 vector2_1 = new Vector2();
            Vector3 vector3 = new Vector3(0.0D, 0.0D, 0.0D);
            f3d2d(vector3, vector2);
            Vector3 vector3_1 = new Vector3(1.0D, 0.0D, 0.0D);
            f3d2d(vector3_1, vector2_1);
            xforward = vector2.y >= vector2_1.y;
            vector3_1 = new Vector3(0.0D, 0.0D, 1.0D);
            f3d2d(vector3_1, vector2_1);
            zforward = vector2.y >= vector2_1.y;
            drawBackAxes(drawing, xforward, zforward);
            double d2 = -0.5D;
            double d3 = 0.5D;
            //byte byte0 = 20;
            if(!xforward){
                double d11 = d2;
                d2 = d3;
                d3 = d11;
            }
            //int j1 = byte0 + 1;
            double d4 = (d3 - d2) / 20D;
            double d8 = -0.5D;
            double d9 = 0.5D;
            //byte byte1 = 20;
            if(!zforward){
                double d12 = d8;
                d8 = d9;
                d9 = d12;
            }
            double d10 = (d9 - d8) / 20D;
            double d = d2;
            double d7 = d8;
            for(int i = -10; i <= 10; i++){
                double d5 = M[10 + cx(i)][10 + cz(-10)];
                v[0][10 + i].x = Q.data[0][0] * d + Q.data[0][1] * d5 + Q.data[0][2] * d7;
                v[0][10 + i].y = Q.data[1][0] * d + Q.data[1][1] * d5 + Q.data[1][2] * d7;
                d += d4;
            }
            for(int i1 = -9; i1 <= 10; i1++){
                if(i1 == 1)
                    drawRealAxis(drawing);
                //boolean flag = true;
                double d1 = d2;
                d7 += d10;
                for(int j = -10; j <= 10; j++){
                    double d6 = M[10 + cx(j)][10 + cz(i1)];
                    v[1][10 + j].x = Q.data[0][0] * d1 + Q.data[0][1] * d6 + Q.data[0][2] * d7;
                    v[1][10 + j].y = Q.data[1][0] * d1 + Q.data[1][1] * d6 + Q.data[1][2] * d7;
                    d1 += d4;
                }
                for(int k = -10; k <= 9; k++){
                    if(k == 0 && i1 == 0){
                        drawModAxis(drawing);
                        drawImaginaryAxis(drawing);
                    }
                    patch(drawing, v[0][10 + k], v[0][10 + k + 1], v[1][10 + k], v[1][10 + k + 1]);
                }
                for(int l = -10; l <= 10; l++){
                    v[0][10 + l].x = v[1][10 + l].x;
                    v[0][10 + l].y = v[1][10 + l].y;
                }
            }
            drawFrontAxes(drawing, xforward, zforward);
        }

        @SuppressWarnings("unused")
		void f2dPix(Vector2 vector2, Point point){
            point.x = fx(vector2.x);
            point.y = fy(vector2.y);
        }

        private void f3d2d(Vector3 vector3, Vector2 vector2){
            vector2.x = Q.data[0][0] * vector3.x + Q.data[0][1] * vector3.y + Q.data[0][2] * vector3.z;
            vector2.y = Q.data[1][0] * vector3.x + Q.data[1][1] * vector3.y + Q.data[1][2] * vector3.z;
        }

        private void f3dPix(double d, double d1, double d2, Point point){
            point.x = fx(Q.data[0][0] * d + Q.data[0][1] * d1 + Q.data[0][2] * d2);
            point.y = fy(Q.data[1][0] * d + Q.data[1][1] * d1 + Q.data[1][2] * d2);
        }

        double angle(double d, double d1){
            return Math.atan2(d1, d);
        }

        int fx(double d){
            return (int)(d * xyscale + (double)nxpix * 0.5D);
        }

        int fy(double d){
            return (int)(-d * xyscale + (double)nypix * 0.80000000000000004D);
        }

        @Override public Dimension getPreferredSize(){return getMinimumSize();}
        @Override public Dimension getMinimumSize(){return ThreeDWorld.MIN_SIZE;}

        void initQ(){
            Q = Matrix44.tran3(eye);
            double d = angle(-direct.x, -direct.y);
            Q.preRot('z', d);
            double d3 = Math.sqrt(direct.x * direct.x + direct.y * direct.y);
            double d1 = angle(-direct.z, d3);
            Q.preRot('y', d1);
            double d4 = Math.sqrt(d3 * d3 + direct.z * direct.z);
            double d2 = angle(-direct.x * d4, direct.y * direct.z);
            Q.preRot('z', -d2);
            Q.postRot('y', 1.5707963267948966D);
        }

        void lineTo3(Drawing drawing, double d, double d1, double d2){
            Point point = new Point();
            f3dPix(d, d1, d2, point);
            drawing.lineTo(point.x, point.y);
        }

        void moveTo3(Drawing drawing, double d, double d1, double d2){
            Point point = new Point();
            f3dPix(d, d1, d2, point);
            drawing.moveTo(point.x, point.y);
        }

        public void paint(Graphics g){
            g = doubleBuffer.offScreen(g);
            Drawing drawing = new Drawing(g);
            nxpix = getSize().width;
            nypix = getSize().height;
            xyscale = (double)Math.min(nxpix, nypix) * XYFACTOR;
            drawIt(drawing);
            doubleBuffer.onScreen();
        }

        void patch(Drawing drawing, Vector2 vector2, Vector2 vector2_1, Vector2 vector2_2, Vector2 vector2_3){
            double d = (vector2_1.x - vector2.x) * (vector2_3.y - vector2_2.y) - (vector2_1.y - vector2.y) * (vector2_3.x - vector2_2.x);
            if(Math.abs(d) > 0.0001D){
                double d1 = ((vector2_2.x - vector2.x) * (vector2_3.y - vector2_2.y) - (vector2_2.y - vector2.y) * (vector2_3.x - vector2_2.x)) / d;
                if(d1 >= 0.0D && d1 <= 1.0D){
                    v5.x = (1.0D - d1) * vector2.x + d1 * vector2_1.x;
                    v5.y = (1.0D - d1) * vector2.y + d1 * vector2_1.y;
                    triangle(drawing, vector2, vector2_2, v5);
                    triangle(drawing, vector2_1, vector2_3, v5);
                    return;
                }
                d = (vector2_2.x - vector2.x) * (vector2_3.y - vector2_1.y) - (vector2_2.y - vector2.y) * (vector2_3.x - vector2_1.x);
                if(Math.abs(d) > 0.0001D){
                    double d2 = ((vector2_1.x - vector2.x) * (vector2_3.y - vector2_1.y) - (vector2_1.y - vector2.y) * (vector2_3.x - vector2_1.x)) / d;
                    if(d2 >= 0.0D && d2 <= 1.0D){
                        v5.x = (1.0D - d2) * vector2.x + d2 * vector2_2.x;
                        v5.y = (1.0D - d2) * vector2.y + d2 * vector2_2.y;
                        triangle(drawing, vector2, vector2_1, v5);
                        triangle(drawing, vector2_2, vector2_3, v5);
                        return;
                    } else{
                        quadrilateral(drawing, vector2, vector2_1, vector2_3, vector2_2);
                        return;
                    }
                } else{
                    quadrilateral(drawing, vector2, vector2_1, vector2_3, vector2_2);
                    return;
                }
            }
            d = (vector2_2.x - vector2.x) * (vector2_3.y - vector2_1.y) - (vector2_2.y - vector2.y) * (vector2_3.x - vector2_1.x);
            if(Math.abs(d) > 0.0001D){
                double d3 = ((vector2_1.x - vector2.x) * (vector2_3.y - vector2_1.y) - (vector2_1.y - vector2.y) * (vector2_3.x - vector2_1.x)) / d;
                if(d3 >= 0.0D && d3 <= 1.0D){
                    v5.x = (1.0D - d3) * vector2.x + d3 * vector2_2.x;
                    v5.y = (1.0D - d3) * vector2.y + d3 * vector2_2.y;
                    triangle(drawing, vector2, vector2_1, v5);
                    triangle(drawing, vector2_2, vector2_3, v5);
                    return;
                } else{
                    quadrilateral(drawing, vector2, vector2_1, vector2_3, vector2_2);
                    return;
                }
            } else{
                quadrilateral(drawing, vector2, vector2_1, vector2_3, vector2_2);
                return;
            }
        }

        void quadrilateral(Drawing drawing, Vector2 vector2, Vector2 vector2_1, Vector2 vector2_2, Vector2 vector2_3){
            quad.xpoints[0] = fx(vector2.x);
            quad.xpoints[1] = fx(vector2_1.x);
            quad.xpoints[2] = fx(vector2_2.x);
            quad.xpoints[3] = fx(vector2_3.x);
            quad.ypoints[0] = fy(vector2.y);
            quad.ypoints[1] = fy(vector2_1.y);
            quad.ypoints[2] = fy(vector2_2.y);
            quad.ypoints[3] = fy(vector2_3.y);
            drawing.fillPolygon(quad, Color.lightGray);
            drawing.g.drawPolygon(quad);
        }

        public void setFont(Font font){
            super.setFont(font);
            FONT_HEIGHT = getFontMetrics(font).getAscent();
        }

        void shift(int i, int j){
            double d = (double)i * (6.2831853071795862D / (double)getSize().height);
            Q.postRot('y', -d);
        }

        void triangle(Drawing drawing, Vector2 vector2, Vector2 vector2_1, Vector2 vector2_2){
            tri.xpoints[0] = fx(vector2.x);
            tri.xpoints[1] = fx(vector2_1.x);
            tri.xpoints[2] = fx(vector2_2.x);
            tri.ypoints[0] = fy(vector2.y);
            tri.ypoints[1] = fy(vector2_1.y);
            tri.ypoints[2] = fy(vector2_2.y);
            drawing.fillPolygon(tri, Color.lightGray);
            drawing.g.drawPolygon(tri);
        }

        public void update(Graphics g){paint(g);}

    }//inner class ThreeDCanvas

}
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

import java.awt.*;

/** Keeps two graphics objects for off-screen drawing. */
final class DoubleBuffer{

	//State -------------------------------------------------------
	
    private final Component component;
    private Dimension offDimension;
    private Image offImage;
    private Graphics offGraphics;
    private Graphics onGraphics;

    //Constructors -----------------------------------------------
    
    DoubleBuffer(Component component1){
        component = component1;
    }

    //Methods ----------------------------------------------------
    
    Graphics offScreen(Graphics g){
        onGraphics = g;
        Dimension dimension = component.getSize();
        if(offGraphics == null || dimension.width != offDimension.width || dimension.height != offDimension.height){
            offDimension = dimension;
            offImage = component.createImage(dimension.width, dimension.height);
            offGraphics = offImage.getGraphics();
        }
        offGraphics.setColor(component.getBackground());
        offGraphics.fillRect(0, 0, dimension.width, dimension.height);
        offGraphics.setColor(Color.black);
        return offGraphics;
    }

    void onScreen(){
        onGraphics.drawImage(offImage, 0, 0, component);
    }

}
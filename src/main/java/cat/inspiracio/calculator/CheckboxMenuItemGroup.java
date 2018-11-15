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
import java.awt.CheckboxMenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/** Some menu items with checkboxes that collaborate. 
 * Only one can be checked. */
class CheckboxMenuItemGroup{

	//State -------------------------------------------------------
	
	/** I only remember the currently checked menu item. */
    private JCheckBoxMenuItem current;

    //Constructor -------------------------------------------------
    
    /** Makes a fresh group. */
    CheckboxMenuItemGroup(){}

    //Methods -----------------------------------------------------
    
    /** Add a checkbox menu item to the group. 
     * The checked menu item becomes unchecked, and this new
     * menu item becomes the checked one. */
    void add(final JCheckBoxMenuItem mi){
        if(current!=null)
            current.setState(false);
        mi.setState(true);
        current=mi;
        
        mi.addItemListener(new ItemListener() {
            @Override public void itemStateChanged(ItemEvent e){
            	boolean selected=e.getStateChange()==ItemEvent.SELECTED;
            	//Becomes selected
            	if(selected){
            		if(mi==current){
            			//do nothing, already selected
            		}else{
            			//Unselect the current one and this becomes the current
            			current.setState(false);
            			current=mi;
            		}
            	}
            	//Becomes unselected
            	else{
            		if(mi==current){
            			//Trying to unselect the current one: but these are checkbox menu items, so they can't be unselected.
            			//I set its state back to true.
            			mi.setState(true);
            		}else{
            			//unselect another one. How can this happen? The other ones should be unselected already.
            			mi.setState(false);
            		}
            	}
            }
        });
    }

    /** Gets the currently checked menu item. */
    JCheckBoxMenuItem getSelected(){return current;}

    /** Sets the checked menu item and unchecks the previously
     * checked one. */
    void setSelected(JCheckBoxMenuItem checkboxmenuitem){
        if(current != checkboxmenuitem){
            current.setState(false);
            checkboxmenuitem.setState(true);
            current = checkboxmenuitem;
        }
    }

}
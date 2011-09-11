/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.com
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
package inspiracio.calculator;

import static inspiracio.calculator.Calculator.Mode.CALC;
import static inspiracio.calculator.Calculator.Mode.FZ;
import static inspiracio.calculator.Calculator.Mode.MODFZ;
import static inspiracio.calculator.Calculator.Mode.REFX;
import inspiracio.calculator.Calculator.Mode;
import inspiracio.numbers.EC;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** A menu bar for Complex Calculator windows that remembers the listeners it has registered in other components. */
final class MMenuBar extends MenuBar{

	private Calculator calculator;
	private PropertyChangeListener modeListener;
	private PropertyChangeListener precisionListener;

	/** Make a menu bar for complex calculator windows. 
	 * @param calculator The calculator. It has the state. */
	MMenuBar(Calculator calculator){
		this.calculator=calculator;
		
		Menu mFile = new Menu("File");
		MenuItem menuitem = new MenuItem("About Complex Calculator...");
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionevent){
				new AboutBox(MMenuBar.this.calculator, "Complex Calculator");
			}
		});
		mFile.add(menuitem);
		menuitem = new MenuItem("Quit", new MenuShortcut(81));
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionevent){MMenuBar.this.calculator.quit();}
		});
		mFile.add(menuitem);
		this.add(mFile);
		
		//Mode menu
		//Make a property change listener so that a mode change affects all three instances of the menu bar.
		Menu mMode = new Menu("Mode");
		final CheckboxMenuItem checkboxmenuitem = new CheckboxMenuItem("Calculate");
		final CheckboxMenuItem checkboxmenuitem1 = new CheckboxMenuItem("z -> f(z)");
		final CheckboxMenuItem checkboxmenuitem2 = new CheckboxMenuItem("z -> |f(z)|");
		final CheckboxMenuItem checkboxmenuitem3 = new CheckboxMenuItem("x -> Re(f(x))");
		checkboxmenuitem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == 1)MMenuBar.this.calculator.setMode(CALC);
			}
		});
		mMode.add(checkboxmenuitem);
		checkboxmenuitem1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == 1)MMenuBar.this.calculator.setMode(FZ);
			}
		});
		mMode.add(checkboxmenuitem1);
		checkboxmenuitem2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == 1)MMenuBar.this.calculator.setMode(MODFZ);
			}
		});
		mMode.add(checkboxmenuitem2);
		checkboxmenuitem3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == 1)MMenuBar.this.calculator.setMode(REFX);
			}
		});
		mMode.add(checkboxmenuitem3);
		final CheckboxMenuItemGroup gMode = new CheckboxMenuItemGroup();
		gMode.add(checkboxmenuitem);
		gMode.add(checkboxmenuitem1);
		gMode.add(checkboxmenuitem2);
		gMode.add(checkboxmenuitem3);
		Mode mode=MMenuBar.this.calculator.getMode();
		gMode.setSelected(mode != CALC ? mode !=FZ ? mode !=MODFZ ? checkboxmenuitem3 : checkboxmenuitem2 : checkboxmenuitem1 : checkboxmenuitem);
		//A listener to select the right mode.
		this.modeListener=new PropertyChangeListener(){
			@Override public void propertyChange(PropertyChangeEvent evt){
				Mode mode=MMenuBar.this.calculator.getMode();
				gMode.setSelected(mode != CALC ? mode !=FZ ? mode !=MODFZ ? checkboxmenuitem3 : checkboxmenuitem2 : checkboxmenuitem1 : checkboxmenuitem);
			}};
		MMenuBar.this.calculator.addPropertyChangeListener("mode", this.modeListener);
		this.add(mMode);

		//Precision menu
		final Menu mPrecision = new Menu("Precision");
		final CheckboxMenuItemGroup gPrecision= new CheckboxMenuItemGroup();
		for(int l = 0; l < Calculator.precisions.length; l++){
			CheckboxMenuItem mi = new CheckboxMenuItem(Integer.toString(Calculator.precisions[l]));
			final int j =Calculator.precisions[l];
			mi.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent itemevent){
					if(itemevent.getStateChange() == 1)EC.setPrecision(j);
				}
			});
			mPrecision.add(mi);
			gPrecision.add(mi);
		}
		int precision=EC.getPrecision();
		for(int i1 = 0; i1 < mPrecision.getItemCount(); i1++){
			CheckboxMenuItem checkboxmenuitem8 = (CheckboxMenuItem)mPrecision.getItem(i1);
			if(checkboxmenuitem8.getLabel().equals(Integer.toString(precision)))
				gPrecision.setSelected(checkboxmenuitem8);
		}
		//A listener to select the right precision.
		this.precisionListener=new PropertyChangeListener(){
			@Override public void propertyChange(PropertyChangeEvent evt){
				int precision=EC.getPrecision();
				for(int i1 = 0; i1 < mPrecision.getItemCount(); i1++){
					CheckboxMenuItem mi = (CheckboxMenuItem)mPrecision.getItem(i1);
					if(mi.getLabel().equals(Integer.toString(precision)))
						gPrecision.setSelected(mi);
				}
			}};
		EC.addPropertyChangeListener("precision", precisionListener);
		this.add(mPrecision);
	}

	void dispose(){
		this.calculator.removePropertyChangeListener("mode", modeListener);
		EC.removePropertyChangeListener("precision", modeListener);
	}
}
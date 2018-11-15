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
import cat.inspiracio.calculator.Calculator.Mode;
import cat.inspiracio.numbers.EC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** A menu bar for Complex Calculator windows that remembers the listeners it
 * has registered in other components.
 *
 * Five windows have an instance of this menu bar:
 * 1. Calculator
 * 2. zWorld
 * 3. FzWorld
 * 4. ThreeDWorld
 * 5. RefxWorld
 * Maybe the menu bar should only be in one?
 * */
final class MMenuBar extends JMenuBar {

	private Calculator calculator;
	private PropertyChangeListener modeListener;
	private PropertyChangeListener precisionListener;

	/** Make a menu bar for complex calculator windows. 
	 * @param calculator The calculator. It has the state. */
	MMenuBar(Calculator calculator){
		this.calculator=calculator;

		//File menu
		JMenu mFile = new JMenu("File");
		JMenuItem menuitem = new JMenuItem("About ...");
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionevent){
				new AboutBox(MMenuBar.this.calculator, "Complex Calculator");
			}
		});
		mFile.add(menuitem);
		menuitem = new JMenuItem("Quit", 'Q');//new MenuShortcut(81));
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionevent){MMenuBar.this.calculator.quit();}
		});
		mFile.add(menuitem);
		add(mFile);
		
		//Mode menu
		//Make a property change listener so that a mode change affects all
		// three instances of the menu bar.
		JMenu mMode = new JMenu("Mode");
		final JCheckBoxMenuItem checkboxmenuitem = new JCheckBoxMenuItem("Calculate");
		final JCheckBoxMenuItem checkboxmenuitem1 = new JCheckBoxMenuItem("z -> f(z)");
		final JCheckBoxMenuItem checkboxmenuitem2 = new JCheckBoxMenuItem("z -> |f(z)|");
		final JCheckBoxMenuItem checkboxmenuitem3 = new JCheckBoxMenuItem("x -> Re(f(x))");
		checkboxmenuitem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					MMenuBar.this.calculator.setMode(CALC);
			}
		});
		mMode.add(checkboxmenuitem);
		checkboxmenuitem1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					MMenuBar.this.calculator.setMode(FZ);
			}
		});
		mMode.add(checkboxmenuitem1);
		checkboxmenuitem2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					MMenuBar.this.calculator.setMode(MODFZ);
			}
		});
		mMode.add(checkboxmenuitem2);
		checkboxmenuitem3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemevent){
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					MMenuBar.this.calculator.setMode(REFX);
			}
		});
		mMode.add(checkboxmenuitem3);

		//XXX These already fire events. Here I just wanted to build the menu.
		final CheckboxMenuItemGroup gMode = new CheckboxMenuItemGroup();
		gMode.add(checkboxmenuitem);
		gMode.add(checkboxmenuitem1);
		gMode.add(checkboxmenuitem2);
		gMode.add(checkboxmenuitem3);

		Mode mode=MMenuBar.this.calculator.getMode();
		gMode.setSelected(mode != CALC ? mode !=FZ ? mode !=MODFZ ? checkboxmenuitem3 : checkboxmenuitem2 : checkboxmenuitem1 : checkboxmenuitem);
		//A listener to select the right mode.
		modeListener=new PropertyChangeListener(){
			@Override public void propertyChange(PropertyChangeEvent evt){
				Mode mode=MMenuBar.this.calculator.getMode();
				gMode.setSelected(mode != CALC ? mode !=FZ ? mode !=MODFZ ? checkboxmenuitem3 : checkboxmenuitem2 : checkboxmenuitem1 : checkboxmenuitem);
			}};
		MMenuBar.this.calculator.addPropertyChangeListener("mode", modeListener);
		add(mMode);

		//Precision menu
		final JMenu mPrecision = new JMenu("Precision");
		final CheckboxMenuItemGroup gPrecision= new CheckboxMenuItemGroup();
		for(int l = 0; l < Calculator.precisions.length; l++){
			JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Integer.toString(Calculator.precisions[l]));
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
			JCheckBoxMenuItem checkboxmenuitem8 = (JCheckBoxMenuItem)mPrecision.getItem(i1);
			if(checkboxmenuitem8.getText().equals(Integer.toString(precision)))
				gPrecision.setSelected(checkboxmenuitem8);
		}
		//A listener to select the right precision.
		precisionListener=new PropertyChangeListener(){
			@Override public void propertyChange(PropertyChangeEvent evt){
				int precision=EC.getPrecision();
				for(int i1 = 0; i1 < mPrecision.getItemCount(); i1++){
					JCheckBoxMenuItem mi = (JCheckBoxMenuItem)mPrecision.getItem(i1);
					if(mi.getText().equals(Integer.toString(precision)))
						gPrecision.setSelected(mi);
				}
			}};
		EC.addPropertyChangeListener("precision", precisionListener);
		add(mPrecision);
	}

	void dispose(){
		calculator.removePropertyChangeListener("mode", modeListener);
		EC.removePropertyChangeListener("precision", modeListener);
	}
}
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
import cat.inspiracio.numbers.EC;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;

/** A menu bar for the Calculator */
final class MMenuBar extends JMenuBar {

	private Calculator calculator;
	private PropertyChangeListener precisionListener;

	/** Make a menu bar for complex calculator windows. 
	 * @param calculator The calculator. It has the state. */
	MMenuBar(Calculator calculator){
		this.calculator=calculator;
		file();
		mode();
		precision();
	}

	private void file(){
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem("About ...");
		item.addActionListener(
			actionevent -> new About(calculator, "Complex Calculator")
		);
		menu.add(item);
		item = new JMenuItem("Quit", 'Q');//new MenuShortcut(81));
		item.addActionListener(actionevent -> calculator.quit());
		menu.add(item);
		add(menu);
	}

	private void mode(){
		JMenu menu = new JMenu("Mode");

		final JRadioButtonMenuItem miCalc = new JRadioButtonMenuItem("Calculate", true);
		final JRadioButtonMenuItem miFz = new JRadioButtonMenuItem("z -> f(z)");
		final JRadioButtonMenuItem miModFz = new JRadioButtonMenuItem("z -> |f(z)|");
		final JRadioButtonMenuItem miReFx = new JRadioButtonMenuItem("x -> Re(f(x))");

		menu.add(miCalc);
		menu.add(miFz);
		menu.add(miModFz);
		menu.add(miReFx);

		ButtonGroup group=new ButtonGroup();
		group.add(miCalc);
		group.add(miFz);
		group.add(miModFz);
		group.add(miReFx);

		miCalc.addItemListener(
			itemevent -> {
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					calculator.setMode(CALC);
			}
		);
		miFz.addItemListener(
			itemevent -> {
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					calculator.setMode(FZ);
			}
		);
		miModFz.addItemListener(
			itemevent -> {
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					calculator.setMode(MODFZ);
			}
		);
		miReFx.addItemListener(
			itemevent -> {
				if(itemevent.getStateChange() == ItemEvent.SELECTED)
					calculator.setMode(REFX);
			}
		);
		add(menu);
	}

	private void precision(){
		final JMenu menu = new JMenu("Precision");

		ButtonGroup group=new ButtonGroup();

		for(int l = 0; l < Calculator.precisions.length; l++){
			final int j =Calculator.precisions[l];
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(Integer.toString(j));
			item.addItemListener(
				itemevent -> {
					if(itemevent.getStateChange() == ItemEvent.SELECTED)EC.setPrecision(j);
				}
			);
			menu.add(item);
			group.add(item);
		}

		//select initial one
		int precision=EC.getPrecision();
		for(int i1 = 0; i1 < menu.getItemCount(); i1++){
			JCheckBoxMenuItem checkboxmenuitem8 = (JCheckBoxMenuItem)menu.getItem(i1);
			if(checkboxmenuitem8.getText().equals(Integer.toString(precision))) {
				//gPrecision.setSelected(checkboxmenuitem8);
			}
		}

		//A listener to select the right precision.
		precisionListener= evt -> {
				int prec=EC.getPrecision();
				for(int i1 = 0; i1 < menu.getItemCount(); i1++){
					JCheckBoxMenuItem mi = (JCheckBoxMenuItem)menu.getItem(i1);
					if(mi.getText().equals(Integer.toString(prec))) {
						//gPrecision.setSelected(mi);
					}
				}
			};
		EC.addPropertyChangeListener("precision", precisionListener);
		add(menu);
	}

}
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

import cat.inspiracio.complex.Complex$;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/** A menu bar for the Calculator */
final class Menus extends JMenuBar {

	private Calculator calculator;
	private PropertyChangeListener precisionListener;

	/** Make a menu bar for complex calculator windows. 
	 * @param calculator The calculator. It has the state. */
	Menus(Calculator calculator){
		this.calculator=calculator;
		file();
		mode();
		precision();
	}

	private void file(){
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem("About ...");
		item.addActionListener(
			actionevent -> new About(calculator)
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

		miCalc.addActionListener(event -> calculator.setMode(CALC));
		miFz.addActionListener( event -> calculator.setMode(FZ) );
		miModFz.addActionListener( event -> calculator.setMode(MODFZ) );
		miReFx.addActionListener( event ->  calculator.setMode(REFX) );
		add(menu);
	}

	private void precision(){
		final JMenu menu = new JMenu("Precision");

		ButtonGroup group=new ButtonGroup();
		int precision= Complex$.MODULE$.getPrecision();
		for(int j : Calculator.precisions){
			String s = Integer.toString(j);
			boolean selected = precision==j;
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(s, selected );
			item.addActionListener(
					event -> Complex$.MODULE$.setPrecision(j)
			);
			menu.add(item);
			group.add(item);
		}

		add(menu);
	}

}
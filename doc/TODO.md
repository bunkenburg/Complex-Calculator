# TODO

* put on web page with installation instructions
* Better initial window positions
    x About
    - mode Calculate
    - mode z -> f(z)
    - mode z -> |f(z)|
    - mode x -> Re(f(x))
    
    Initial windows positions:
    - Calculator: setLocationByPlatform(true)
    - ComplexWorld extends World: //setLocationByPlatform(true)
    - FzWorld extends World: setLocationRelativeTo(this.calculator)
    - RefxWorld: setLocationRelativeTo(calculator); setLocationByPlatform(true);
    - abstract World: setLocationRelativeTo(calculator); setLocationByPlatform(true);
    - ThreeDWorld: setLocationRelativeTo(calculator); setLocationByPlatform(true);
    
* Take complex numbers out and make into Scala library
* Change to Scala
* Better colours, not so dark
* Give it a real icon
* Nice About-menu
* make a real executable application
* Give World windows a toolbar instead of buttons and menus to read
* tests for GUI?

# DONE

* Change from AWT to Swing
* Circle-button and similar are invisible
* Menus disappear when other window is active
* Mode menu is not synchronised in the menu bars of the several windows.
* Precision menu is not synchronised in the menu bars of the several windows.
* Remove the Unicode switching stuff

# WILL NOT DO


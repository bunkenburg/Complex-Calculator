# TODO

* in extreme zooms of plane, factor out a scaling factor like 10\-5 and write it apart
* configurable precision
* These symbols can be used for multiplication without '*': eπi∞
* rewrite parsing using Scala combinator parsing
* |f(z)| mode, z word: remove unused disabled icons
* put into About: date, mvn version, git revision
* World: save movement in preferences
* World: save sphere rotation in preferences
* Sphere: give it zoom
* World: save interaction in preferences
* ThreeDWorld: save movement in preferences
* RefxWorld: save movement in preferences
* f"${c}#precision"
* Complex.toString with rationals
* put on web page with installation instructions
* better expressions: don't always write parenthesis
* Take complex numbers out and make into Scala library
* separate GUI from logic
* make a version inside a web page: GUI is html, logic with scala.js
* make an Android app from it
* multi-project for application, app, web

# DONE

* exponentiation symbol: \ not ^
* Complex.toString and ComplexFormat with reasonable precision to get short numbers
* Complex.toString or format with precision
    - represent in terms of π
    - represent integers
    - implement Complex.toString
    - implement f"${c}#s"
    - implement ComplexFormat extends NumberFormat
* rewrite GUI using scala.swing._
* World: save zoom in preferences
* var only for variable instance variables
* add Edit menu: Cut Copy Paste
* executable JAR must have executable bit
* Sphere: improve moving: keep same number under mouse
* Better initial window positions
* Change from AWT to Swing
* Circle-button and similar are invisible
* Menus disappear when other window is active
* Mode menu is not synchronised in the menu bars of the several windows.
* Precision menu is not synchronised in the menu bars of the several windows.
* Remove the Unicode switching stuff

# WILL NOT DO

* executable for Mac
* executable for Windows
    - maybe with http://launch4j.sourceforge.net/
* executable for linux

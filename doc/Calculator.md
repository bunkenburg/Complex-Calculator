# Calculator

Design of the graphical interface

## Calculator

cat.inspiracio.calculator.Calculator

* About extends JDialog

* Calculator extends JFrame
    - mode
    - cW
    - zW
    - fzW
    - modfzW
    - refxW

* abstract World extends JFrame
    - calculator
    - buttonPanel
    - interaction
    - canvas: plane, sphere
    - prevx, prevy //previous mouse position
        
* ComplexWorld extends World
    - numbers: ECList
    - local interactionChoice
    
* ZWorld extends World
    - interactionChoice: JComboBox
    - fzW
    - modfzW

* FzWorld extends World
    - zW (could be constructor parameter)

* RefxWorld extends JFrame
    - calculator
    - RefxCanvas
    
* abstract WorldRepresentation extends JComponent
    - w: World

* Plane extends WorldRepresentation
* Sphere extends WorldRepresentation

* ThreeDWorld extends JFrame

* Display extends JTextArea
* DoubleBuffer
* Drawing
* enum Interaction
* Matrix44
* Menus extends JMenuBar
    - calculator
* MyKeyListener extends KeyAdapter
    - calculator
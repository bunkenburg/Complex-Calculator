# better display

Now the display for mathematical expressions is just a TextArea and all expressions are just linear Strings, like "e\\πi +1 = 0".

It would be much better if the sidplay showed mathematical expressions in the usual mathematical way.

In particular, exponentiation and division should be written using a two-dimensional layout like this:

![Euler's formula](https://wikimedia.org/api/rest_v1/media/math/render/svg/a7464809a40f9e486de3a454745f572fbf8bb256)

## expressions

    Expression ::= literal
                | i e π ∞
                | x z
                | sin _ // and similar
                | _ !   // postfix
                | - _   // prefix
                | | _ | // infix
                | _ + _
                | _ - _
                | _ * _
                | _ _   // first may be literal, first and second may be constant, variable, function, parenthesis, absolute value.
                | _ / _ // 2d display desired
                | _ \ _ // 2d display desired
                
## displayed shapes

sequence:   _

postfix:    _ !
prefix:     sin _
infix:      ( _ )
binary:     _ x _

exponentiation (2d): 

     _
    _
    
division (2d):

     _
    ---
     _

Here, "_" stands for an editable place.
The display may have several editable places, but at most
one is focused.

An editable place has an editable sequence of characters.
It also has a selection.
If start and end of the selection coincide, then it is just a cursor.

In order to specify the display, make the state precise.
List all possible input events that can affect it,
and say what their effect is on the state.

## state

* Shape
* at most one editable place is focused.

## events

calculator buttons:

* 0 1 2 3 4 5 6 7 8 9 .
* i e π ∞
* + - * / \
* ! sin ...
* del
* clear
* =
* () | 

menus:

* Cut
* Copy
* Paste
* Select All

keyboard:

* all characters that appear on buttons
* other characters
* space tab intro
* backspace delete
* start end backpage nextpage
* arrows: up down left right

mouse:

* click
* drag

## what is the best way to procede?

* Make an object-model of the displayed editor for the expression

or

* show instances on existing class Expression
* lay them out well, in 2d
* add focus traversal
* add editing a part

Expressions

* Constants
* Variable
* Unary
* Binary

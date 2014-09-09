Expression Evaluator
==============

This is a calculator program in Java that evaluates expressions in a very simple integer expression language.   The program takes an input on the command line, computes the result, and prints it to the console.  For example:

`$ java calculator.Main “mult(2, 2)`

will get 

`$ 4`


## Expression Definition

An expression is one of the of the following:
* Numbers: integers between Integer.MIN\_VALUE and Integer.MAX\_VALUE
* Variables: strings of characters, where each character is one of a-z, A-Z
* Arithmetic functions: add, sub, mult, div, each taking two arbitrary expressions as arguments.  In other words, each argument may be any of the expressions on this list.
* A “let” operator for assigning values to variables:
  >let(\<variable name>\, \<value expression\>, \<expression where variable is used\>) 
  As with arithmetic functions,  the value expression and the expression where the variable is used may be an arbitrary expression from this list.


for example,

* add(1, 2)  = 3
* add(1, mult(2, 3)) = 7
* mult(add(2, 2), div(9, 3)) = 12
* let(a, 5, add(a, a))  = 10
* let(a, 5, let(b, mult(a, 10), add(b, a))) = 55
* let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))) = 40


## Assumptions
###1. Variable scope

in 

> let(\<1. variable name\>, \<2. value expression\>, \<3. expression where variable is used\>)


variables defined in \<2\> will not work in \<3\>.
variable defined in \<3\> will  the variable in \<1\> 

###2. Multiple parentheses around expression is legal.
for example,  ((3)),  ((let(a,5,add(a,1)))),  ((a)), they are all legal.


## Algorithm

Basic idea is the make variables definitions ("let" expression) as "Context".  Then run the expression evaluator in traditional way of stacks. And with the reference of context, replace variables with values before push into stack.
